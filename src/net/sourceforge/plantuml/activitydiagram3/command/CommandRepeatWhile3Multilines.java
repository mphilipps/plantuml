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
package net.sourceforge.plantuml.activitydiagram3.command;

import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines3;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class CommandRepeatWhile3Multilines extends CommandMultilines3<ActivityDiagram3> {

	public CommandRepeatWhile3Multilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public RegexConcat getPatternEnd2() {
		return new RegexConcat(//
				new RegexLeaf("TEST1", "(.*)"), new RegexLeaf("\\)"), //
				new RegexLeaf(";?$"));
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(//
				new RegexLeaf("^"), //
				new RegexLeaf("repeat[%s]?while"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("\\("), //
				new RegexLeaf("TEST1", "(.*)$"));
	}

	@Override
	public CommandExecutionResult executeNow(ActivityDiagram3 diagram, BlocLines lines) {
		lines = lines.trim(false);
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
		final RegexResult lineLast = getPatternEnd2().matcher(lines.getLast499().toString());

		// System.err.println("line0=" + line0);
		// System.err.println("linesLast=" + lineLast);

		//
		// final HtmlColor color = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0));

		final String test = line0.get("TEST1", 0);
		Display testDisplay = Display.getWithNewlines(test);
		for (CharSequence s : lines.subExtract(1, 1)) {
			testDisplay = testDisplay.add(s);
		}
		final String trailTest = lineLast.get("TEST1", 0);
		if (StringUtils.isEmpty(trailTest) == false) {
			testDisplay = testDisplay.add(trailTest);
		}

		Display yes = Display.NULL;// Display.getWithNewlines("arg.getLazzy(\"WHEN\", 0)");
		final Display out = Display.NULL; // Display.getWithNewlines("arg.getLazzy(\"OUT\", 0)");
		final HtmlColor linkColor = null; // diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR",
											// 0));
		final Display linkLabel = Display.NULL; // Display.getWithNewlines("arg.get(\"LABEL\", 0)");
		final List<Display> splitted = testDisplay.splitMultiline(MyPattern.cmpile("\\)[%s]*(is|equals?)[%s]*\\(",
				Pattern.CASE_INSENSITIVE));
		if (splitted.size() == 2) {
			testDisplay = splitted.get(0);
			yes = splitted.get(1);

		}

		return diagram.repeatWhile(testDisplay, yes, out, linkLabel, linkColor);
	}

}
