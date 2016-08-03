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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Swimlane implements SpecificBackcolorable {

	private final String name;
	private Display display;

	private UTranslate translate = new UTranslate();
	private double totalWidth;

	public Swimlane(String name) {
		this.name = name;
		this.display = Display.getWithNewlines(name);

	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display label) {
		this.display = label;
	}

	public final UTranslate getTranslate() {
		return translate;
	}

	public final void setTranslateAndWidth(UTranslate translate, double totalWidth) {
		this.translate = translate;
		this.totalWidth = totalWidth;
	}

	public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HtmlColor color) {
		if (color != null) {
			this.colors = colors.add(type, color);
		}
	}

	private Colors colors = Colors.empty();

	public final double getTotalWidth() {
		return totalWidth;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

}
