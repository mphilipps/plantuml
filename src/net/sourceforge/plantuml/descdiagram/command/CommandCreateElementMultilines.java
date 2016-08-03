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

import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public class CommandCreateElementMultilines extends CommandMultilines2<DescriptionDiagram> {

	private final int type;

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateElementMultilines(int type) {
		super(getRegexConcat(type), MultilinesStrategy.REMOVE_STARTING_QUOTE);
		this.type = type;
	}

	@Override
	public String getPatternEnd() {
		if (type == 0) {
			return "(?i)^(.*)[%g]$";
		}
		if (type == 1) {
			return "(?i)^(.*)\\]$";
		}
		throw new IllegalArgumentException();
	}

	private static RegexConcat getRegexConcat(int type) {
		if (type == 0) {
			return new RegexConcat(new RegexLeaf("^"), //
					new RegexLeaf("TYPE", "(usecase|database|artifact)[%s]+"), //
					new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
					new RegexLeaf("[%s]*"), //
					new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
					new RegexLeaf("[%s]*"), //
					ColorParser.exp1(), //
					new RegexLeaf("[%s]*"), //
					new RegexLeaf("DESC", "as[%s]*[%g](.*)$"));
		}
		if (type == 1) {
			return new RegexConcat(new RegexLeaf("^"), //
					new RegexLeaf("TYPE", "(package|usecase|database|artifact)[%s]+"), //
					new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
					new RegexLeaf("[%s]*"), //
					new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
					new RegexLeaf("[%s]*"), //
					ColorParser.exp1(), //
					new RegexLeaf("[%s]*"), //
					new RegexLeaf("DESC", "\\[(.*)$"));
		}
		throw new IllegalArgumentException();
	}

	public CommandExecutionResult executeNow(DescriptionDiagram diagram, BlocLines lines) {
		lines = lines.trim(false);
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
		final String symbol = StringUtils.goUpperCase(line0.get("TYPE", 0));
		final LeafType type;
		final USymbol usymbol;

		if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("package")) {
			type = LeafType.DESCRIPTION;
			usymbol = USymbol.PACKAGE;
		} else if (symbol.equalsIgnoreCase("database")) {
			type = LeafType.DESCRIPTION;
			usymbol = USymbol.DATABASE;
		} else if (symbol.equalsIgnoreCase("artifact")) {
			type = LeafType.DESCRIPTION;
			usymbol = USymbol.ARTIFACT;
		} else {
			throw new IllegalStateException();
		}

		final Code code = Code.of(line0.get("CODE", 0));
		final List<String> lineLast = StringUtils.getSplit(MyPattern.cmpile(getPatternEnd()), lines.getLast499()
				.toString());
		lines = lines.subExtract(1, 1);
		Display display = lines.toDisplay();
		final String descStart = line0.get("DESC", 0);
		if (StringUtils.isNotEmpty(descStart)) {
			display = display.addFirst(descStart);
		}

		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			display = display.add(lineLast.get(0));
		}

		final String stereotype = line0.get("STEREO", 0);

		final ILeaf result = diagram.createLeaf(code, display, type, usymbol);
		result.setUSymbol(usymbol);
		if (stereotype != null) {
			result.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}

		result.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0)));

		return CommandExecutionResult.ok();
	}

}
