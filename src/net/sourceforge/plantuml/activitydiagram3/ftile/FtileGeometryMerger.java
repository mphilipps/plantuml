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

public class FtileGeometryMerger {

	private final FtileGeometry result;

	public FtileGeometryMerger(FtileGeometry geo1, FtileGeometry geo2) {
		final double left = Math.max(geo1.getLeft(), geo2.getLeft());
		final double dx1 = left - geo1.getLeft();
		final double dx2 = left - geo2.getLeft();
		final double width = Math.max(geo1.getWidth() + dx1, geo2.getWidth() + dx2);
		final double height = geo1.getHeight() + geo2.getHeight();

		if (geo2.hasPointOut()) {
			result = new FtileGeometry(width, height, left, geo1.getInY(), geo2.getOutY() + geo1.getHeight());
		} else {
			result = new FtileGeometry(width, height, left, geo1.getInY());
		}
	}

	public final FtileGeometry getResult() {
		return result;
	}
}
