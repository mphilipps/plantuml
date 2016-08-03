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
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackageWithUSymbol extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageWithUSymbol() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("SYMBOL", "(package|rectangle|node|artifact|folder|frame|cloud|database|storage|component|card|together)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|[^#%s{}]*)"), //
				new RegexLeaf("AS", "(?:[%s]+as[%s]+([\\p{L}0-9_.]+))?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("[%s]*\\{$"));
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, RegexResult arg) {
		final Code code;
		final String display;
		final String name = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));
		if (arg.get("AS", 0) == null) {
			if (name.length() == 0) {
				code = UniqueSequence.getCode("##");
				display = null;
			} else {
				code = Code.of(name);
				display = code.getFullName();
			}
		} else {
			display = name;
			code = Code.of(arg.get("AS", 0));
		}
		final IGroup currentPackage = diagram.getCurrentGroup();
		final IEntity p = diagram.getOrCreateGroup(code, Display.getWithNewlines(display),
				GroupType.PACKAGE, currentPackage);
		p.setUSymbol(USymbol.getFromString(arg.get("SYMBOL", 0)));
		final String stereotype = arg.get("STEREOTYPE", 0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype, false));
		}
		final String color = arg.get("COLOR", 0);
		if (color != null) {
			p.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
