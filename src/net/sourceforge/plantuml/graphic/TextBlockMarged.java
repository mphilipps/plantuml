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
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockMarged extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;
	private final double x1;
	private final double x2;
	private final double y1;
	private final double y2;

	public TextBlockMarged(TextBlock textBlock, double x1, double x2, double y1, double y2) {
		this.textBlock = textBlock;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, x1 + x2, y1 + y2);
	}

	public void drawU(UGraphic ug) {
		final UTranslate translate = new UTranslate(x1, y1);
		textBlock.drawU(ug.apply(translate));
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder) {
		final Rectangle2D parent = textBlock.getInnerPosition(member, stringBounder);
		if (parent == null) {
			return null;
		}
		final UTranslate translate = new UTranslate(x1, y1);
		return translate.apply(parent);
	}

}
