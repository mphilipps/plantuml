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
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public final class FactorySequenceNoteOverSeveralCommand implements SingleMultiFactoryCommand<SequenceDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat( //
				new RegexLeaf("^"), //
				new RegexLeaf("VMERGE", "(/)?[%s]*"), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)[%s]+over[%s]+"), //
				new RegexLeaf("P1", "([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*\\,[%s]*"), //
				new RegexLeaf("P2", "([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("$") //
		);
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat( //
				new RegexLeaf("^"), //
				new RegexLeaf("VMERGE", "(/)?[%s]*"), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)[%s]+over[%s]+"), //
				new RegexLeaf("P1", "([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*\\,[%s]*"), //
				new RegexLeaf("P2", "([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$"));
	}
	
	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}


	public Command<SequenceDiagram> createSingleLine() {
		return new SingleLineCommand2<SequenceDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final SequenceDiagram system, RegexResult arg) {
				final BlocLines strings = BlocLines.getWithNewlines(arg.get("NOTE", 0));

				return executeInternal(system, arg, strings);
			}

		};
	}

	public Command<SequenceDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<SequenceDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^end[%s]?(note|hnote|rnote)$";
			}

			public CommandExecutionResult executeNow(final SequenceDiagram system, BlocLines lines) {
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();
				return executeInternal(system, line0, lines);
			}

		};
	}

	private CommandExecutionResult executeInternal(SequenceDiagram diagram, final RegexResult line0, BlocLines lines) {
		final Participant p1 = diagram.getOrCreateParticipant(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(line0.get("P1", 0)));
		final Participant p2 = diagram.getOrCreateParticipant(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(line0.get("P2", 0)));

		if (lines.size() > 0) {
			final boolean tryMerge = line0.get("VMERGE", 0) != null;
			final Note note = new Note(p1, p2, lines.toDisplay());
			final Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
			note.setColors(colors);
			// note.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0)));
			note.setStyle(NoteStyle.getNoteStyle(line0.get("STYLE", 0)));
			diagram.addNote(note, tryMerge);
		}
		return CommandExecutionResult.ok();
	}

}
