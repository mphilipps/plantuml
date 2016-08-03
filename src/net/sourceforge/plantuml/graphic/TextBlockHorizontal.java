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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockHorizontal extends AbstractTextBlock implements TextBlock {

	private final TextBlock b1;
	private final TextBlock b2;
	private final VerticalAlignment alignment;

	public TextBlockHorizontal(TextBlock b1, TextBlock b2, VerticalAlignment alignment) {
		this.b1 = b1;
		this.b2 = b2;
		this.alignment = alignment;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim1 = b1.calculateDimension(stringBounder);
		final Dimension2D dim2 = b2.calculateDimension(stringBounder);
		return Dimension2DDouble.mergeLR(dim1, dim2);
	}

	public void drawU(UGraphic ug) {
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		final Dimension2D dimb1 = b1.calculateDimension(ug.getStringBounder());
		final Dimension2D dimb2 = b2.calculateDimension(ug.getStringBounder());
		final Dimension2D dim1 = b1.calculateDimension(ug.getStringBounder());
		if (alignment == VerticalAlignment.CENTER) {
			b1.drawU(ug.apply(new UTranslate(0, ((dim.getHeight() - dimb1.getHeight()) / 2))));
			b2.drawU(ug.apply(new UTranslate(dim1.getWidth(), ((dim.getHeight() - dimb2.getHeight()) / 2))));
		} else {
			b1.drawU(ug);
			b2.drawU(ug.apply(new UTranslate(dim1.getWidth(), 0)));
		}
	}

}
