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

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class UClip implements UChange {

	private final double x;
	private final double y;
	private final double width;
	private final double height;

	public UClip(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "CLIP x=" + x + " y=" + y + " w=" + width + " h=" + height;
	}

	public UClip translate(double dx, double dy) {
		return new UClip(x + dx, y + dy, width, height);
	}
	
	public UClip translate(UTranslate translate) {
		return translate(translate.getDx(), translate.getDy());
	}


	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final double getWidth() {
		return width;
	}

	public final double getHeight() {
		return height;
	}

	public boolean isInside(double xp, double yp) {
		if (xp < x) {
			assert getClippedX(xp) != xp;
			return false;
		}
		if (xp > x + width) {
			assert getClippedX(xp) != xp;
			return false;
		}
		if (yp < y) {
			assert getClippedY(yp) != yp;
			return false;
		}
		if (yp > y + height) {
			assert getClippedY(yp) != yp;
			return false;
		}
		assert getClippedX(xp) == xp;
		assert getClippedY(yp) == yp;
		return true;
	}

	public Rectangle2D.Double getClippedRectangle(Rectangle2D.Double r) {
		return (Rectangle2D.Double) r.createIntersection(new Rectangle2D.Double(x, y, width, height));
	}

	public Line2D.Double getClippedLine(Line2D.Double line) {
		if (isInside(line.x1, line.y1) && isInside(line.x2, line.y2)) {
			return line;
		}
		if (isInside(line.x1, line.y1) == false && isInside(line.x2, line.y2) == false) {
			return null;
		}
		if (line.x1 != line.x2 && line.y1 != line.y2) {
			return null;
		}
		assert line.x1 == line.x2 || line.y1 == line.y2;
		if (line.y1 == line.y2) {
			final double newx1 = getClippedX(line.x1);
			final double newx2 = getClippedX(line.x2);
			return new Line2D.Double(newx1, line.y1, newx2, line.y2);
		}
		if (line.x1 == line.x2) {
			final double newy1 = getClippedY(line.y1);
			final double newy2 = getClippedY(line.y2);
			return new Line2D.Double(line.x1, newy1, line.x2, newy2);
		}
		throw new IllegalStateException();
	}

	private double getClippedX(double xp) {
		if (xp < x) {
			return x;
		}
		if (xp > x + width) {
			return x + width;
		}
		return xp;
	}

	private double getClippedY(double yp) {
		if (yp < y) {
			return y;
		}
		if (yp > y + height) {
			return y + height;
		}
		return yp;
	}

}
