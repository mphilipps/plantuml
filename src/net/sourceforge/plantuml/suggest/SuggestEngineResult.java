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
package net.sourceforge.plantuml.suggest;

import net.sourceforge.plantuml.StringUtils;


public class SuggestEngineResult {

	private final SuggestEngineStatus status;
	private final String suggestedLine;

	public static final SuggestEngineResult CANNOT_CORRECT = new SuggestEngineResult(SuggestEngineStatus.CANNOT_CORRECT);
	public static final SuggestEngineResult SYNTAX_OK = new SuggestEngineResult(SuggestEngineStatus.SYNTAX_OK);

	private SuggestEngineResult(SuggestEngineStatus status) {
		if (status == SuggestEngineStatus.ONE_SUGGESTION) {
			throw new IllegalArgumentException();
		}
		this.status = status;
		this.suggestedLine = null;
	}

	@Override
	public String toString() {
		return status + " " + suggestedLine;
	}

	@Override
	public int hashCode() {
		return status.hashCode() + (suggestedLine == null ? 0 : suggestedLine.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		final SuggestEngineResult this2 = (SuggestEngineResult) obj;
		return status.equals(this2.status) && sameString(suggestedLine, this2.suggestedLine);
	}

	private static boolean sameString(String a, String b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null || b != null) {
			return false;
		}
		return a.equals(b);
	}

	public SuggestEngineResult(String suggestedLine) {
		if (StringUtils.trin(suggestedLine).length() == 0) {
			throw new IllegalArgumentException();
		}
		this.status = SuggestEngineStatus.ONE_SUGGESTION;
		this.suggestedLine = suggestedLine;
	}

	public final SuggestEngineStatus getStatus() {
		return status;
	}

	public final String getSuggestedLine() {
		return suggestedLine;
	}

}
