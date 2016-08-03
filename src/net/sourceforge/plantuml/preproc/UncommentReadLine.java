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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.utils.StartUtils;

public class UncommentReadLine implements ReadLine {

	private final ReadLine raw;
	private final Pattern start;
	private final Pattern unpause;
	private String headerToRemove;
	private boolean paused;

	public UncommentReadLine(ReadLine source) {
		this.raw = source;
		this.start = MyPattern.cmpile(StartUtils.START_PATTERN);
		this.unpause = MyPattern.cmpile(StartUtils.PAUSE_PATTERN);
	}

	public CharSequence2 readLine() throws IOException {
		final CharSequence2 result = raw.readLine();

		if (result == null) {
			return null;
		}

		final Matcher m = start.matcher(result);
		if (m.find()) {
			headerToRemove = m.group(1);
		}
		if (paused) {
			final Matcher m2 = unpause.matcher(result);
			if (m2.find()) {
				headerToRemove = m2.group(1);
			}
		}
		if (headerToRemove != null && headerToRemove.startsWith(result.toString2())) {
			return new CharSequence2Impl("", result.getLocation());
		}
		if (headerToRemove != null && result.toString2().startsWith(headerToRemove)) {
			return result.subSequence(headerToRemove.length(), result.length());
		}
		return result;
	}

	public void close() throws IOException {
		this.raw.close();
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
