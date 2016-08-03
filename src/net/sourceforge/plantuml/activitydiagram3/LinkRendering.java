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
package net.sourceforge.plantuml.activitydiagram3;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class LinkRendering {

	private final HtmlColor color;
	private Display display = Display.NULL;

	public LinkRendering(HtmlColor color) {
		this.color = color;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Display getDisplay() {
		return display;
	}

	public HtmlColor getColor() {
		return color;
	}

	@Override
	public String toString() {
		return super.toString() + " " + display + " " + color;
	}

	public static HtmlColor getColor(LinkRendering inLinkRendering, HtmlColor defaultColor) {
		if (inLinkRendering == null || inLinkRendering.getColor() == null) {
			return defaultColor;
		}
		return inLinkRendering.getColor();
	}

	public static HtmlColor getColor(HtmlColor col, HtmlColor defaultColor) {
		if (col == null) {
			return defaultColor;
		}
		return col;
	}

	public static Display getDisplay(LinkRendering linkRendering) {
		if (linkRendering == null) {
			return Display.NULL;
		}
		return linkRendering.getDisplay();
	}

}
