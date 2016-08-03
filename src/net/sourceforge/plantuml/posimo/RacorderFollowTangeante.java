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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RacorderFollowTangeante extends RacorderAbstract implements Racorder {

	public DotPath getRacordIn(Rectangle2D rect, Line2D tangeante) {

// Log.println("rect x=" + rect.getX() + " y=" + rect.getY() + " w=" + rect.getWidth() + " h="
//				+ rect.getHeight());
// Log.println("tangeante (" + tangeante.getX1() + "," + tangeante.getY1() + ") (" + tangeante.getX2()
//				+ "," + tangeante.getY2() + ")");

		final DotPath result = new DotPath();

		// final Point2D inter = BezierUtils.intersect((Line2D.Double)
		// tangeante, rect);
		Point2D inter = new LineRectIntersection(tangeante, rect).getIntersection();
// Log.println("inter=" + inter);

		if (inter == null) {
			final Point2D p1 = new Point2D.Double(rect.getMinX(), rect.getMinY());
			final Point2D p2 = new Point2D.Double(rect.getMaxX(), rect.getMinY());
			final Point2D p3 = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
			final Point2D p4 = new Point2D.Double(rect.getMinX(), rect.getMaxY());

			inter = LineRectIntersection.getCloser(tangeante.getP1(), p1, p2, p3, p4);
		}

		final CubicCurve2D.Double curv = new CubicCurve2D.Double(tangeante.getX1(), tangeante.getY1(),
				tangeante.getX1(), tangeante.getY1(), inter.getX(), inter.getY(), inter.getX(), inter.getY());
		return result.addAfter(curv);
	}

}
