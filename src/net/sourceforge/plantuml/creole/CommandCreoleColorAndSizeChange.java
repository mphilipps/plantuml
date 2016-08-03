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
package net.sourceforge.plantuml.creole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSet;

public class CommandCreoleColorAndSizeChange implements Command {

	private final Pattern pattern;

	public static final String fontPattern = "\\<font(?:[%s]+size[%s]*=[%s]*[%g]?(\\d+)[%g]?|[%s]+color[%s]*=[%s]*[%g]?(#[0-9a-fA-F]{6}|\\w+)[%g]?)+[%s]*\\>";

	public static Command create() {
		return new CommandCreoleColorAndSizeChange("^(?i)(" + fontPattern + "(.*?)\\</font\\>)");
	}

	public static Command createEol() {
		return new CommandCreoleColorAndSizeChange("^(?i)(" + fontPattern + "(.*))$");
	}

	private CommandCreoleColorAndSizeChange(String p) {
		this.pattern = MyPattern.cmpile(p);

	}

	public int matchingSize(String line) {
		final Matcher m = pattern.matcher(line);
		if (m.find() == false) {
			return 0;
		}
		return m.group(1).length();
	}

	public String executeAndGetRemaining(String line, StripeSimple stripe) {
		final Matcher m = pattern.matcher(line);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		// for (int i = 1; i <= m.groupCount(); i++) {
		// System.err.println("i=" + i + " " + m.group(i));
		// }

		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
		FontConfiguration fc2 = fc1;
		if (m.group(2) != null) {
			fc2 = fc2.changeSize(Integer.parseInt(m.group(2)));
		}
		if (m.group(3) != null) {
			final HtmlColor color = HtmlColorSet.getInstance().getColorIfValid(m.group(3));
			fc2 = fc2.changeColor(color);
		}

		stripe.setActualFontConfiguration(fc2);
		stripe.analyzeAndAdd(m.group(4));
		stripe.setActualFontConfiguration(fc1);
		return line.substring(m.group(1).length());
	}
}
