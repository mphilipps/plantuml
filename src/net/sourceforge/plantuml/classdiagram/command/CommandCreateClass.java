/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of PlantUML.
 *
 * Licensed under The MIT License (Massachusetts Institute of Technology License)
 * 
 * See http://opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public class CommandCreateClass extends SingleLineCommand2<ClassDiagram> {

	public static final String CODE = "[^%s{}%g<>]+";
	public static final String CODE_NO_DOTDOT = "[^%s{}%g<>:]+";

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClass() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", //
						"(interface|enum|annotation|abstract[%s]+class|abstract|class)[%s]+"), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("DISPLAY1", "[%g](.+)[%g]"), //
								new RegexLeaf("[%s]+as[%s]+"), //
								new RegexLeaf("CODE1", "(" + CODE + ")")), //
						new RegexConcat(//
								new RegexLeaf("CODE2", "(" + CODE + ")"), //
								new RegexLeaf("[%s]+as[%s]+"), // //
								new RegexLeaf("DISPLAY2", "[%g](.+)[%g]")), //
						new RegexLeaf("CODE3", "(" + CODE + ")"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				new RegexLeaf("GENERIC", "(?:[%s]*\\<(" + GenericRegexProducer.PATTERN + ")\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREO", "(\\<{2}.*\\>{2})?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("LINECOLOR", "(?:##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?)?"), //
				new RegexLeaf("EXTENDS", "([%s]+(extends)[%s]+(" + CommandCreateClassMultilines.CODES + "))?"), //
				new RegexLeaf("IMPLEMENTS", "([%s]+(implements)[%s]+(" + CommandCreateClassMultilines.CODES + "))?"), //
				new RegexLeaf("$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, RegexResult arg) {
		final LeafType type = LeafType.getLeafType(StringUtils.goUpperCase(arg.get("TYPE", 0)));
		final Code code = Code.of(arg.getLazzy("CODE", 0)).eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
		final String display = arg.getLazzy("DISPLAY", 0);

		final String stereotype = arg.get("STEREO", 0);
		final String generic = arg.get("GENERIC", 0);
		final ILeaf entity;
		if (diagram.leafExist(code)) {
			entity = diagram.getOrCreateLeaf(code, type, null);
			entity.muteToType(type, null);
		} else {
			entity = diagram.createLeaf(code, Display.getWithNewlines(display), type, null);
		}
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}
		if (generic != null) {
			entity.setGeneric(generic);
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HtmlColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		entity.setColors(colors);

		// entity.setSpecificColorTOBEREMOVED(ColorType.LINE, lineColor);
		// entity.setSpecificColorTOBEREMOVED(ColorType.HEADER, colors.getColor(ColorType.HEADER));
		//
		// if (colors.getLineStyle() != null) {
		// entity.setSpecificLineStroke(LinkStyle.getStroke(colors.getLineStyle()));
		// }
		//
		// if (arg.get("LINECOLOR", 0) != null) {
		// entity.applyStroke(arg.get("LINECOLOR", 0));
		// }

		// manageExtends(diagram, arg, entity);
		CommandCreateClassMultilines.manageExtends("EXTENDS", diagram, arg, entity);
		CommandCreateClassMultilines.manageExtends("IMPLEMENTS", diagram, arg, entity);

		return CommandExecutionResult.ok();
	}
	// public static void manageExtends(ClassDiagram system, RegexResult arg, final IEntity entity) {
	// if (arg.get("EXTENDS", 1) != null) {
	// final Mode mode = arg.get("EXTENDS", 1).equalsIgnoreCase("extends") ? Mode.EXTENDS : Mode.IMPLEMENTS;
	// final Code other = Code.of(arg.get("EXTENDS", 2));
	// LeafType type2 = LeafType.CLASS;
	// if (mode == Mode.IMPLEMENTS) {
	// type2 = LeafType.INTERFACE;
	// }
	// if (mode == Mode.EXTENDS && entity.getEntityType() == LeafType.INTERFACE) {
	// type2 = LeafType.INTERFACE;
	// }
	// final IEntity cl2 = system.getOrCreateLeaf(other, type2, null);
	// LinkType typeLink = new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS);
	// if (type2 == LeafType.INTERFACE && entity.getEntityType() != LeafType.INTERFACE) {
	// typeLink = typeLink.getDashed();
	// }
	// final Link link = new Link(cl2, entity, typeLink, null, 2, null, null, system.getLabeldistance(),
	// system.getLabelangle());
	// system.addLink(link);
	// }
	// }

}
