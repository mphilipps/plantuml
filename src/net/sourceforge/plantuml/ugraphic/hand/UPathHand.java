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

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;

public class UPathHand {

	private final UPath path;
	private final double defaultVariation = 4.0;

	public UPathHand(UPath source) {

		final UPath jigglePath = new UPath();

		Point2D last = new Point2D.Double();

		for (USegment segment : source) {
			final USegmentType type = segment.getSegmentType();
			if (type == USegmentType.SEG_MOVETO) {
				final double x = segment.getCoord()[0];
				final double y = segment.getCoord()[1];
				jigglePath.moveTo(x, y);
				last = new Point2D.Double(x, y);

			} else if (type == USegmentType.SEG_LINETO) {
				final double x = segment.getCoord()[0];
				final double y = segment.getCoord()[1];
				final HandJiggle jiggle = new HandJiggle(last.getX(), last.getY(), defaultVariation);
				jiggle.lineTo(x, y);
				for (USegment seg2 : jiggle.toUPath()) {
					if (seg2.getSegmentType() == USegmentType.SEG_LINETO) {
						jigglePath.lineTo(seg2.getCoord()[0], seg2.getCoord()[1]);
					}
				}
				last = new Point2D.Double(x, y);
			} else {
				this.path = source;
				return;
			}
		}
		this.path = jigglePath;
	}

	public UPath getHanddrawn() {
		return this.path;
	}

}
