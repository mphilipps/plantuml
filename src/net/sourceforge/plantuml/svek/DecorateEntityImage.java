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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class DecorateEntityImage extends AbstractTextBlock implements TextBlockBackcolored {

	private final TextBlock original;
	private final HorizontalAlignment horizontal1;
	private final TextBlock text1;
	private final HorizontalAlignment horizontal2;
	private final TextBlock text2;

	private double deltaX;
	private double deltaY;

	public static DecorateEntityImage addTop(TextBlock original, TextBlock text, HorizontalAlignment horizontal) {
		return new DecorateEntityImage(original, text, horizontal, null, null);
	}

	public static DecorateEntityImage addBottom(TextBlock original, TextBlock text, HorizontalAlignment horizontal) {
		return new DecorateEntityImage(original, null, null, text, horizontal);
	}

	public static DecorateEntityImage add(TextBlock original, TextBlock text, HorizontalAlignment horizontal,
			VerticalAlignment verticalAlignment) {
		if (verticalAlignment == VerticalAlignment.TOP) {
			return addTop(original, text, horizontal);
		}
		return addBottom(original, text, horizontal);
	}

	public DecorateEntityImage(TextBlock original, TextBlock text1, HorizontalAlignment horizontal1, TextBlock text2,
			HorizontalAlignment horizontal2) {
		this.original = original;
		this.horizontal1 = horizontal1;
		this.text1 = text1;
		this.horizontal2 = horizontal2;
		this.text2 = text2;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimOriginal = original.calculateDimension(stringBounder);
		final Dimension2D dimText1 = getTextDim(text1, stringBounder);
		final Dimension2D dimText2 = getTextDim(text2, stringBounder);
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double yImage = dimText1.getHeight();
		final double yText2 = yImage + dimOriginal.getHeight();

		final double xImage = (dimTotal.getWidth() - dimOriginal.getWidth()) / 2;

		if (text1 != null) {
			final double xText1 = getTextX(dimText1, dimTotal, horizontal1);
			text1.drawU(ug.apply(new UTranslate(xText1, 0)));
		}
		original.drawU(ug.apply(new UTranslate(xImage, yImage)));
		deltaX = xImage;
		deltaY = yImage;
		if (text2 != null) {
			final double xText2 = getTextX(dimText2, dimTotal, horizontal2);
			text2.drawU(ug.apply(new UTranslate(xText2, yText2)));
		}
	}

	private Dimension2D getTextDim(TextBlock text, StringBounder stringBounder) {
		if (text == null) {
			return new Dimension2DDouble(0, 0);
		}
		return text.calculateDimension(stringBounder);
	}

	private double getTextX(final Dimension2D dimText, final Dimension2D dimTotal, HorizontalAlignment h) {
		if (h == HorizontalAlignment.CENTER) {
			return (dimTotal.getWidth() - dimText.getWidth()) / 2;
		} else if (h == HorizontalAlignment.LEFT) {
			return 0;
		} else if (h == HorizontalAlignment.RIGHT) {
			return dimTotal.getWidth() - dimText.getWidth();
		} else {
			throw new IllegalStateException();
		}
	}

	public HtmlColor getBackcolor() {
		if (original instanceof TextBlockBackcolored) {
			return ((TextBlockBackcolored) original).getBackcolor();
		}
		throw new UnsupportedOperationException();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimOriginal = original.calculateDimension(stringBounder);
		final Dimension2D dimText = Dimension2DDouble.mergeTB(getTextDim(text1, stringBounder),
				getTextDim(text2, stringBounder));
		return Dimension2DDouble.mergeTB(dimOriginal, dimText);
	}

	public final double getDeltaX() {
		if (original instanceof DecorateEntityImage) {
			return deltaX + ((DecorateEntityImage) original).deltaX;
		}
		return deltaX;
	}

	public final double getDeltaY() {
		if (original instanceof DecorateEntityImage) {
			return deltaY + ((DecorateEntityImage) original).deltaY;
		}
		return deltaY;
	}

}
