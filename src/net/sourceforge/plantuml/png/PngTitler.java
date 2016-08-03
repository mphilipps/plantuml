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
package net.sourceforge.plantuml.png;

import java.awt.Font;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PngTitler {

	private final HtmlColor textColor;
	private final HtmlColor hyperlinkColor;
	private final Display text;
	private final int fontSize;
	private final String fontFamily;
	private final HorizontalAlignment horizontalAlignment;
	private final boolean useUnderlineForHyperlink;

	public PngTitler(HtmlColor textColor, Display text, int fontSize, String fontFamily,
			HorizontalAlignment horizontalAlignment, HtmlColor hyperlinkColor, boolean useUnderlineForHyperlink) {
		this.textColor = textColor;
		this.text = text;
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
		this.horizontalAlignment = horizontalAlignment;
		this.hyperlinkColor = hyperlinkColor;
		this.useUnderlineForHyperlink = useUnderlineForHyperlink;

	}

	public Dimension2D getTextDimension(StringBounder stringBounder) {
		final TextBlock textBloc = getTextBlock();
		if (textBloc == null) {
			return null;
		}
		return textBloc.calculateDimension(stringBounder);
	}

	public TextBlock getTextBlock() {
		if (Display.isNull(text) || text.size() == 0) {
			return null;
		}
		final UFont normalFont = new UFont(fontFamily, Font.PLAIN, fontSize);
		return text.create(new FontConfiguration(normalFont, textColor, hyperlinkColor, useUnderlineForHyperlink),
				horizontalAlignment, new SpriteContainerEmpty());
	}

	private double getOffsetX(double imWidth, StringBounder stringBounder) {
		final TextBlock textBloc = getTextBlock();
		if (textBloc == null) {
			return 0;
		}
		final Dimension2D dimText = textBloc.calculateDimension(stringBounder);

		if (imWidth >= dimText.getWidth()) {
			return 0;
		}
		return (dimText.getWidth() - imWidth) / 2;
	}

	private double getOffsetY(StringBounder stringBounder) {
		final TextBlock textBloc = getTextBlock();
		if (textBloc == null) {
			return 0;
		}
		final Dimension2D dimText = textBloc.calculateDimension(stringBounder);
		final double height = dimText.getHeight();
		return height;
	}
}
