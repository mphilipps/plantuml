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
import net.sourceforge.plantuml.graphic.FontPosition;

public class CommandCreoleExposantChange implements Command {

	private final Pattern pattern;
	private final FontPosition position;

	private CommandCreoleExposantChange(String p, FontPosition position) {
		this.pattern = MyPattern.cmpile(p);
		this.position = position;
	}

	public static Command create(FontPosition position) {
		return new CommandCreoleExposantChange("^(?i)(" + "\\<" + position.getHtmlTag() + "\\>" + "(.*?)\\</"
				+ position.getHtmlTag() + "\\>)", position);
	}

	// public static Command createLegacyEol(FontStyle style) {
	// return new CommandCreoleExposantChange("^(" + style.getActivationPattern() + "(.+))$", style);
	// }

	public int matchingSize(String line) {
		final Matcher m = pattern.matcher(line);
		if (m.find() == false) {
			return 0;
		}
		return m.group(2).length();
	}

	public String executeAndGetRemaining(String line, StripeSimple stripe) {
		final Matcher m = pattern.matcher(line);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
		final FontConfiguration fc2 = fc1.changeFontPosition(position);

		stripe.setActualFontConfiguration(fc2);
		stripe.analyzeAndAdd(m.group(2));
		stripe.setActualFontConfiguration(fc1);
		return line.substring(m.group(1).length());
	}

}
