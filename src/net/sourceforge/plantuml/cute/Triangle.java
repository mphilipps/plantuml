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
package net.sourceforge.plantuml.cute;

import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Triangle implements CuteShape {

	private final CutePath cutePath;

	public Triangle(VarArgs varArgs) {
		this(varArgs.getPointList("points"));
	}

	private Triangle(CutePath cutePath) {
		this.cutePath = cutePath;
		// if (points.size() != 3) {
		// throw new IllegalArgumentException();
		// }
	}

	public Triangle rotateZoom(final RotationZoom angle) {
		if (angle.isNone()) {
			return this;
		}
		return new Triangle(cutePath.rotateZoom(angle));
	}

	public void drawU(UGraphic ug) {
		cutePath.drawU(ug);
		// ug = ug.apply(new UChangeBackColor(null)).apply(new UChangeColor(HtmlColorUtils.BLACK));
		// cutePath.withNoTension().drawU(
		// ug.apply(new UChangeBackColor(null)).apply(new UChangeColor(HtmlColorUtils.BLACK)));

	}
}
