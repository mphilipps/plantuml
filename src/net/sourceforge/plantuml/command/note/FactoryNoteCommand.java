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
package net.sourceforge.plantuml.command.note;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public final class FactoryNoteCommand implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*(note)[%s]+"), //
				new RegexLeaf("CODE", "as[%s]+([\\p{L}0-9_.]+)"), //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("$") //
		);
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("DISPLAY", "[%g]([^%g]+)[%g][%s]+as[%s]+"), //
				new RegexLeaf("CODE", "([\\p{L}0-9_.]+)[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("$") //
		);

	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		return new SingleLineCommand2<AbstractEntityDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final AbstractEntityDiagram system, RegexResult arg) {
				final String display = arg.get("DISPLAY", 0);
				return executeInternal(system, arg, BlocLines.getWithNewlines(display));
			}

		};
	}

	public Command<AbstractEntityDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^[%s]*end[%s]?note$";
			}

			public CommandExecutionResult executeNow(final AbstractEntityDiagram system, BlocLines lines) {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();
				return executeInternal(system, line0, lines);
			}
		};
	}

	private CommandExecutionResult executeInternal(AbstractEntityDiagram diagram, RegexResult arg, BlocLines display) {
		final Code code = Code.of(arg.get("CODE", 0));
		if (diagram.leafExist(code)) {
			return CommandExecutionResult.error("Note already created: " + code.getFullName());
		}
		final IEntity entity = diagram.createLeaf(code, display.toDisplay(), LeafType.NOTE, null);
		assert entity != null;
		entity.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
		return CommandExecutionResult.ok();
	}

}
