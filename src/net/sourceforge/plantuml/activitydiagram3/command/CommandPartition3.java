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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandPartition3 extends SingleLineCommand2<ActivityDiagram3> {

	public CommandPartition3() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("partition"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("BACKCOLOR", "(?:(#\\w+)[%s]+)?"), //
				new RegexLeaf("TITLECOLOR", "(?:(#\\w+)[%s]+)?"), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|\\S+)"), //
				new RegexLeaf("[%s]*\\{?$"));
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, RegexResult arg) {
		final String partitionTitle = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));
		final HtmlColor titleColor = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("TITLECOLOR", 0));

		final HtmlColor backColorInSkinparam = diagram.getSkinParam().getHtmlColor(ColorParam.partitionBackground,
				null, false);
		final HtmlColor backColor;
		if (backColorInSkinparam == null) {
			backColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("BACKCOLOR", 0));
		} else {
			backColor = backColorInSkinparam;

		}

		HtmlColor borderColor = diagram.getSkinParam().getHtmlColor(ColorParam.partitionBorder, null, false);
		if (borderColor == null) {
			borderColor = HtmlColorUtils.BLACK;
		}

		diagram.startGroup(Display.getWithNewlines(partitionTitle), backColor, titleColor, borderColor);

		return CommandExecutionResult.ok();
	}
}
