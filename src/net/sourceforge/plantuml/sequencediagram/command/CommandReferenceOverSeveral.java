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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.StringUtils;

public class CommandReferenceOverSeveral extends SingleLineCommand2<SequenceDiagram> {

	public CommandReferenceOverSeveral() {
		super(getConcat());
	}

	private static RegexConcat getConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("REF", "ref(#\\w+)?[%s]+over[%s]+"), //
				new RegexLeaf("PARTS",
						"(([\\p{L}0-9_.@]+|[%g][^%g]+[%g])([%s]*,[%s]*([\\p{L}0-9_.@]+|[%g][^%g]+[%g]))*)"), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("URL", "(?:\\[\\[([^|]*)(?:\\|([^|]*))?\\]\\])?"), //
				new RegexLeaf("TEXT", "(.*)"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, RegexResult arg) {
		final HtmlColor backColorElement = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("REF", 0));
		// final HtmlColor backColorGeneral = HtmlColorSet.getInstance().getColorIfValid(arg.get("REF").get(1));

		final List<String> participants = StringUtils.splitComma(arg.get("PARTS", 0));
		final String url = arg.get("URL", 0);
		final String title = arg.get("URL", 1);
		final String text = StringUtils.trin(arg.get("TEXT", 0));

		final List<Participant> p = new ArrayList<Participant>();
		for (String s : participants) {
			p.add(diagram.getOrCreateParticipant(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s)));
		}

		final Display strings = Display.getWithNewlines(text);

		Url u = null;
		if (url != null) {
			u = new Url(url, title);
		}

		final HtmlColor backColorGeneral = null;
		final Reference ref = new Reference(p, u, strings, backColorGeneral, backColorElement);
		diagram.addReference(ref);
		return CommandExecutionResult.ok();
	}

}
