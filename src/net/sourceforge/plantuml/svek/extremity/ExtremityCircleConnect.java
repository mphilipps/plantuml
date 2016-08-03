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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ExtremityCircleConnect extends Extremity {

	private final double px;
	private final double py;
	private final Point2D dest;
	private final double radius = 6;
	private final double radius2 = 10;
	private final double ortho;

	@Override
	public Point2D somePoint() {
		return dest;
	}

	public ExtremityCircleConnect(Point2D p1, double ortho) {
		this.px = p1.getX() - radius;
		this.py = p1.getY() - radius;
		this.dest = new Point2D.Double(p1.getX(), p1.getY());
		this.ortho = ortho;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UStroke(1.5)).apply(new UChangeBackColor(HtmlColorUtils.WHITE));
		ug.apply(new UTranslate(dest.getX() - radius, dest.getY() - radius)).draw(new UEllipse(radius * 2, radius * 2));
		
		final double deg = -ortho * 180 / Math.PI + 90 - 45;
		final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, deg, 90);
		ug.apply(new UTranslate(dest.getX() - radius2, dest.getY() - radius2)).draw(arc1);
	}

	// private Point2D getPointOnCircle(double angle) {
	// final double x = px + radius + radius2 * Math.cos(angle);
	// final double y = py + radius + radius2 * Math.sin(angle);
	// return new Point2D.Double(x, y);
	// }
	//
	// static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
	// final double dx = p2.getX() - p1.getX();
	// final double dy = p2.getY() - p1.getY();
	// ug.draw(x + p1.getX(), y + p1.getY(), new ULine(dx, dy));
	//
	// }

}
