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

import java.awt.geom.Dimension2D;

public class URectangle extends AbstractShadowable implements Scalable {

	private final double width;
	private final double height;
	private final double rx;
	private final double ry;

	public UShape getScaled(double scale) {
		if (scale == 1) {
			return this;
		}
		final AbstractShadowable result = new URectangle(width * scale, height * scale, rx * scale, ry * scale);
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public URectangle(double width, double height) {
		this(width, height, 0, 0);
	}

	public URectangle(double width, double height, double rx, double ry) {
		// if (height == 0) {
		// throw new IllegalArgumentException();
		// }
		if (width == 0) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.height = height;
		this.rx = rx;
		this.ry = ry;
	}

	public URectangle(Dimension2D dim) {
		this(dim.getWidth(), dim.getHeight());
	}

	@Override
	public String toString() {
		return "width=" + width + " height=" + height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getRx() {
		return rx;
	}

	public double getRy() {
		return ry;
	}

	public URectangle clip(UClip clip) {
		return this;
	}

	public MinMax getMinMax() {
		return MinMax.fromMax(width, height);
	}

}
