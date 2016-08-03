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
package net.sourceforge.plantuml;

public class CharSequence2Impl implements CharSequence2 {

	private final CharSequence s;
	private final LineLocation location;

	public CharSequence2Impl(CharSequence s, LineLocation location) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		this.s = s;
		this.location = location;
	}

	public static CharSequence2 errorPreprocessor(CharSequence s, LineLocation lineLocation) {
		return new CharSequence2Impl(s, lineLocation);
	}

	public int length() {
		return s.length();
	}

	public char charAt(int index) {
		return s.charAt(index);
	}

	public CharSequence2 subSequence(int start, int end) {
		return new CharSequence2Impl(s.subSequence(start, end), location);
	}

	public CharSequence toCharSequence() {
		return s;
	}

	@Override
	public String toString() {
		return s.toString();
	}

	public String toString2() {
		return s.toString();
	}

	public LineLocation getLocation() {
		return location;
	}

	public CharSequence2 trin() {
		return new CharSequence2Impl(StringUtils.trin(s.toString()), location);
	}

	public boolean startsWith(String start) {
		return s.toString().startsWith(start);
	}

}
