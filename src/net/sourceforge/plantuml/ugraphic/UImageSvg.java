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
package net.sourceforge.plantuml.ugraphic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UImageSvg implements UShape {

	private final String svg;

	public UImageSvg(String svg) {
		if (svg.startsWith("<?xml")) {
			final int idx = svg.indexOf("<svg");
			svg = svg.substring(idx);
		}
		this.svg = svg;
	}

	public final String getSvg() {
		if (svg.startsWith("<svg")) {
			final int idx = svg.indexOf(">");
			return "<svg>" + svg.substring(idx + 1);
		}
		return svg;
	}

	private int getData(String name) {
		final Pattern p = Pattern.compile("(?i)" + name + "\\W+(\\d+)");
		final Matcher m = p.matcher(svg);
		if (m.find()) {
			final String s = m.group(1);
			return Integer.parseInt(s);
		}
		throw new IllegalStateException("Cannot find " + name);
	}

	public int getHeight() {
		return getData("height");
	}

	public int getWidth() {
		return getData("width");
	}

}
