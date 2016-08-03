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
package net.sourceforge.plantuml.command.note.sequence;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.note.SingleMultiFactoryCommand;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.EventWithDeactivate;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public final class FactorySequenceNoteOnArrowCommand implements SingleMultiFactoryCommand<SequenceDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*"), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("POSITION", "(right|left)[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("$"));
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^[%s]*"), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("POSITION", "(right|left)[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$"));
	}

	public Command<SequenceDiagram> createSingleLine() {
		return new SingleLineCommand2<SequenceDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final SequenceDiagram system, RegexResult arg) {
				return executeInternal(system, arg, BlocLines.getWithNewlines(arg.get("NOTE", 0)));
			}

		};
	}

	public Command<SequenceDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<SequenceDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^[%s]*end[%s]?note$";
			}

			public CommandExecutionResult executeNow(final SequenceDiagram system, BlocLines lines) {
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();
				return executeInternal(system, line0, lines);
			}

		};
	}

	private CommandExecutionResult executeInternal(SequenceDiagram system, final RegexResult line0, BlocLines lines) {
		final EventWithDeactivate m = system.getLastEventWithDeactivate();
		if (m instanceof AbstractMessage) {
			final NotePosition position = NotePosition.valueOf(StringUtils.goUpperCase(line0.get("POSITION", 0)));
			final Url url;
			if (lines.size() > 0) {
				final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
				url = urlBuilder.getUrl(lines.getFirst499().toString());
			} else {
				url = null;
			}
			if (url != null) {
				lines = lines.subExtract(1, 0);
			}

			final NoteStyle style = NoteStyle.getNoteStyle(line0.get("STYLE", 0));
			((AbstractMessage) m).setNote(lines.toDisplay(), position, style, line0.get("COLOR", 0), url);
		}

		return CommandExecutionResult.ok();
	}

}
