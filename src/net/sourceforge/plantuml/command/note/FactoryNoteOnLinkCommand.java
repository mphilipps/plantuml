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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public final class FactoryNoteOnLinkCommand implements SingleMultiFactoryCommand<CucaDiagram> {

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)?[%s]*on[%s]+link"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$"));
	}

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)?[%s]*on[%s]+link"), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	public Command<CucaDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<CucaDiagram>(getRegexConcatMultiLine(), MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^[%s]*end[%s]?note$";
			}

			public CommandExecutionResult executeNow(final CucaDiagram system, BlocLines lines) {
				final String line0 = lines.getFirst499().toString();
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();
				if (lines.size() > 0) {
					final RegexResult arg = getStartingPattern().matcher(line0);
					return executeInternal(system, lines, arg);
				}
				return CommandExecutionResult.error("No note defined");
			}

		};
	}

	public Command<CucaDiagram> createSingleLine() {
		return new SingleLineCommand2<CucaDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final CucaDiagram system, RegexResult arg) {
				final BlocLines note = BlocLines.getWithNewlines(arg.get("NOTE", 0));
				return executeInternal(system, note, arg);
			}
		};
	}

	private CommandExecutionResult executeInternal(CucaDiagram diagram, BlocLines note, final RegexResult arg) {
		final Link link = diagram.getLastLink();
		if (link == null) {
			return CommandExecutionResult.error("No link defined");
		}
		Position position = Position.BOTTOM;
		if (arg.get("POSITION", 0) != null) {
			position = Position.valueOf(StringUtils.goUpperCase(arg.get("POSITION", 0)));
		}
		Url url = null;
		if (note.size() > 0) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			url = urlBuilder.getUrl(note.getFirst499().toString());
		}
		if (url != null) {
			note = note.subExtract(1, 0);
		}
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		link.addNote(note.toDisplay(), position, colors);
		return CommandExecutionResult.ok();
	}

}
