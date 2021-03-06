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
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.color.ColorParser;

public final class FactoryTipOnEntityCommand implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private final IRegex partialPattern;

	// private final boolean withBracket;

	public FactoryTipOnEntityCommand(IRegex partialPattern/* , boolean withBracket */) {
		this.partialPattern = partialPattern;
		// this.withBracket = withBracket;
	}

	private RegexConcat getRegexConcatMultiLine(IRegex partialPattern, final boolean withBracket) {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left)"), //
				new RegexLeaf("[%s]+of[%s]+"), partialPattern, //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf(withBracket ? "[%s]*\\{" : "[%s]*"), //
				new RegexLeaf("$") //
		);
	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		throw new UnsupportedOperationException();
	}

	public Command<AbstractEntityDiagram> createMultiLine(final boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(partialPattern, withBracket),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				if (withBracket) {
					return "(?i)^(\\})$";
				}
				return "(?i)^[%s]*(end[%s]?note)$";
			}

			public CommandExecutionResult executeNow(final AbstractEntityDiagram system, BlocLines lines) {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();

				Url url = null;
				if (lines.size() > 0) {
					final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"),
							ModeUrl.STRICT);
					url = urlBuilder.getUrl(lines.getFirst499().toString());
				}
				if (url != null) {
					lines = lines.subExtract(1, 0);
				}

				return executeInternal(line0, system, url, lines);
			}
		};
	}

	private CommandExecutionResult executeInternal(RegexResult line0, AbstractEntityDiagram diagram, Url url,
			BlocLines lines) {

		final String pos = line0.get("POSITION", 0);

		final Code code = Code.of(line0.get("ENTITY", 0));
		final String member = line0.get("ENTITY", 1);
		if (code == null) {
			return CommandExecutionResult.error("Nothing to note to");
		}
		final IEntity cl1 = diagram.getOrCreateLeaf(code, null, null);
		final Position position = Position.valueOf(StringUtils.goUpperCase(pos)).withRankdir(
				diagram.getSkinParam().getRankdir());

		final Code codeTip = code.addSuffix("$$$" + position.name());
		IEntity tips = diagram.getLeafsget(codeTip);
		if (tips == null) {
			tips = diagram.getOrCreateLeaf(codeTip, LeafType.TIPS, null);
			final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();
			final Link link;
			if (position == Position.RIGHT) {
				link = new Link(cl1, (IEntity) tips, type, Display.NULL, 1);
			} else {
				link = new Link((IEntity) tips, cl1, type, Display.NULL, 1);
			}
			diagram.addLink(link);
		}
		tips.putTip(member, lines.toDisplay());

		// final IEntity note = diagram.createLeaf(UniqueSequence.getCode("GMN"), Display.create(s), LeafType.NOTE,
		// null);
		// note.setSpecificBackcolor(diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0)));
		// if (url != null) {
		// note.addUrl(url);
		// }
		//
		// final Position position = Position.valueOf(StringUtils.goUpperCase(pos)).withRankdir(
		// diagram.getSkinParam().getRankdir());
		// final Link link;
		//
		// final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getDashed();
		// if (position == Position.RIGHT) {
		// link = new Link(cl1, note, type, null, 1);
		// link.setHorizontalSolitary(true);
		// } else if (position == Position.LEFT) {
		// link = new Link(note, cl1, type, null, 1);
		// link.setHorizontalSolitary(true);
		// } else if (position == Position.BOTTOM) {
		// link = new Link(cl1, note, type, null, 2);
		// } else if (position == Position.TOP) {
		// link = new Link(note, cl1, type, null, 2);
		// } else {
		// throw new IllegalArgumentException();
		// }
		// diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
