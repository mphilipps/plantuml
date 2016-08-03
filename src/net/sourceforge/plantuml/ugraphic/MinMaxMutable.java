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

import net.sourceforge.plantuml.Dimension2DDouble;

public class MinMaxMutable {

	private double maxX;
	private double maxY;
	private double minX;
	private double minY;

	public static MinMaxMutable getEmpty(boolean initToZero) {
		if (initToZero) {
			return new MinMaxMutable(0, 0, 0, 0);
		}
		return new MinMaxMutable(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	}

	public boolean isInfinity() {
		return minX == Double.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "X=" + minX + " " + maxX + " Y=" + minY + " " + maxY;
	}

	private MinMaxMutable(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void addPoint(double x, double y) {
		this.maxX = Math.max(x, maxX);
		this.maxY = Math.max(y, maxY);
		this.minX = Math.min(x, minX);
		this.minY = Math.min(y, minY);
	}

	public static MinMaxMutable fromMax(double maxX, double maxY) {
		final MinMaxMutable result = MinMaxMutable.getEmpty(true);
		result.addPoint(maxX, maxY);
		return result;
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMaxY() {
		return maxY;
	}

	public final double getMinX() {
		return minX;
	}

	public final double getMinY() {
		return minY;
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(maxX - minX, maxY - minY);
	}

}
