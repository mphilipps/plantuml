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

import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;

public class UGraphicCompress2 extends UGraphicDelegator {

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new UGraphicCompress2(getUg(), compressionTransform, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke || change instanceof UChangeBackColor || change instanceof UChangeColor) {
			return new UGraphicCompress2(getUg().apply(change), compressionTransform, translate);
		}
		throw new UnsupportedOperationException();
	}

	private final CompressionTransform compressionTransform;
	private final UTranslate translate;

	public UGraphicCompress2(UGraphic ug, CompressionTransform compressionTransform) {
		this(ug, compressionTransform, new UTranslate());
	}

	private UGraphicCompress2(UGraphic ug, CompressionTransform compressionTransform, UTranslate translate) {
		super(ug);
		this.compressionTransform = compressionTransform;
		this.translate = translate;
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (shape instanceof ULine) {
			drawLine(x, y, (ULine) shape);
		} else if (shape instanceof Snake) {
			drawSnake(x, y, (Snake) shape);
		} else {
			getUg().apply(new UTranslate(ct(x), y)).draw(shape);
		}
	}

	private void drawSnake(double x, double y, Snake shape) {
		final Snake transformed = shape.translate(new UTranslate(x, y)).transformX(compressionTransform);
		getUg().draw(transformed);
	}

	private void drawLine(double x, double y, ULine shape) {
		drawLine(ct(x), y, ct(x + shape.getDX()), y + shape.getDY());
	}

	private double ct(double v) {
		return compressionTransform.transform(v);
	}

	private void drawLine(double x1, double y1, double x2, double y2) {
		final double xmin = Math.min(x1, x2);
		final double xmax = Math.max(x1, x2);
		final double ymin = Math.min(y1, y2);
		final double ymax = Math.max(y1, y2);
		getUg().apply(new UTranslate(xmin, ymin)).draw(new ULine(xmax - xmin, ymax - ymin));
	}

}
