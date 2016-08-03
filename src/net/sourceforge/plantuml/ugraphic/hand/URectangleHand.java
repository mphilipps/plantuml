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
package net.sourceforge.plantuml.ugraphic.hand;

import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class URectangleHand {

	final private UPolygon poly;

	public URectangleHand(URectangle rectangle) {
		final double width = rectangle.getWidth();
		final double height = rectangle.getHeight();
		final HandJiggle jiggle;
		final double rx = Math.min(rectangle.getRx() / 2, width / 2);
		final double ry = Math.min(rectangle.getRy() / 2, height / 2);
		// System.err.println("rx=" + rx + " ry=" + ry);
		if (rx == 0 && ry == 0) {
			jiggle = new HandJiggle(0, 0, 1.5);
			jiggle.lineTo(width, 0);
			jiggle.lineTo(width, height);
			jiggle.lineTo(0, height);
			jiggle.lineTo(0, 0);
		} else {
			jiggle = new HandJiggle(rx, 0, 1.5);
			jiggle.lineTo(width - rx, 0);
			jiggle.arcTo(-Math.PI / 2, 0, width - rx, ry, rx, ry);
			jiggle.lineTo(width, height - ry);
			jiggle.arcTo(0, Math.PI / 2, width - rx, height - ry, rx, ry);
			jiggle.lineTo(rx, height);
			jiggle.arcTo(Math.PI / 2, Math.PI, rx, height - ry, rx, ry);
			jiggle.lineTo(0, ry);
			jiggle.arcTo(Math.PI, 3 * Math.PI / 2, rx, ry, rx, ry);
		}

		this.poly = jiggle.toUPolygon();
		this.poly.setDeltaShadow(rectangle.getDeltaShadow());
	}

	public Shadowable getHanddrawn() {
		return this.poly;
	}

}
