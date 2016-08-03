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

import net.sourceforge.plantuml.graphic.UGraphicDelegator;

public class UGraphicCompress extends UGraphicDelegator {

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new UGraphicCompress(getUg(), compressionTransform, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke || change instanceof UChangeBackColor || change instanceof UChangeColor) {
			return new UGraphicCompress(getUg().apply(change), compressionTransform, translate);
		}
		throw new UnsupportedOperationException();
	}

	private final CompressionTransform compressionTransform;
	private final UTranslate translate;

	public UGraphicCompress(UGraphic ug, CompressionTransform compressionTransform) {
		this(ug, compressionTransform, new UTranslate());
	}

	private UGraphicCompress(UGraphic ug, CompressionTransform compressionTransform, UTranslate translate) {
		super(ug);
		this.compressionTransform = compressionTransform;
		this.translate = translate;
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (shape instanceof ULine) {
			drawLine(x, y, (ULine) shape);
		} else {
			getUg().apply(new UTranslate(x, ct(y))).draw(shape);
		}
	}

	private void drawLine(double x, double y, ULine shape) {
		drawLine(x, ct(y), x + shape.getDX(), ct(y + shape.getDY()));
	}

	private double ct(double v) {
		return compressionTransform.transform(v);
	}

	private void drawLine(double x1, double y1, double x2, double y2) {
		if (y1 > y2) {
			drawLine(x2, y2, x1, y1);
			return;
		}
		assert y1 <= y2;
		final double xmin = Math.min(x1, x2);
		final double xmax = Math.max(x1, x2);
		final double ymin = Math.min(y1, y2);
		final double ymax = Math.max(y1, y2);
		if (x2 >= x1) {
			getUg().apply(new UTranslate(xmin, ymin)).draw(new ULine(xmax - xmin, ymax - ymin));
		} else {
			getUg().apply(new UTranslate(xmax, ymin)).draw(new ULine(-(xmax - xmin), ymax - ymin));
		}
	}

}
