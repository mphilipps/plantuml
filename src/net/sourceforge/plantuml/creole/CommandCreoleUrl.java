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

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class CommandCreoleUrl implements Command {

	private final Pattern pattern;
	private final ISkinSimple skinParam;

	public static Command create(ISkinSimple skinParam) {
		return new CommandCreoleUrl(skinParam, "^(" + UrlBuilder.getRegexp() + ")");
	}

	private CommandCreoleUrl(ISkinSimple skinParam, String p) {
		this.pattern = MyPattern.cmpile(p);
		this.skinParam = skinParam;

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
		final UrlBuilder urlBuilder = new UrlBuilder(skinParam.getValue("topurl"), ModeUrl.STRICT);
		final Url url = urlBuilder.getUrl(m.group(1));
		stripe.addUrl(url);

//		final int size = Integer.parseInt(m.group(2));
//		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
//		final FontConfiguration fc2 = fc1.changeSize(size);
//		// final FontConfiguration fc2 = new AddStyle(style, null).apply(fc1);
//		stripe.setActualFontConfiguration(fc2);
		// stripe.analyzeAndAdd("AZERTY");
//		stripe.setActualFontConfiguration(fc1);
		return line.substring(m.group(1).length());
	}
}
