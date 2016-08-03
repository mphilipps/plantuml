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
package net.sourceforge.plantuml.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class StartUtils {

	public static final String PAUSE_PATTERN = "(?i)((?:\\W|\\<[^<>]*\\>)*)@unpause";
	public static final String START_PATTERN = "(?i)((?:[^\\w~]|\\<[^<>]*\\>)*)@start";
	

	public static boolean isArobaseStartDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@start");
	}

	public static boolean isArobaseEndDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@end");
	}

	public static boolean isArobasePauseDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@pause");
	}

	public static boolean isArobaseUnpauseDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@unpause");
	}

	private static final Pattern append = MyPattern.cmpile("^\\W*@append");

	public static CharSequence2 getPossibleAppend(CharSequence2 s) {
		final Matcher m = append.matcher(s);
		if (m.find()) {
			return s.subSequence(m.group(0).length(), s.length()).trin();
			//return StringUtils.trin(s.toString().substring(m.group(0).length()));
		}
		return null;
	}

}
