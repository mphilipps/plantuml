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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UFont;

public abstract class AbstractTextualComponent extends AbstractComponent {

	private final Display strings;

	private final int marginX1;
	private final int marginX2;
	private final int marginY;

	private final TextBlock textBlock;

	private final UFont font;
	private final HtmlColor fontColor;

	public AbstractTextualComponent(CharSequence label, FontConfiguration font,
			HorizontalAlignment horizontalAlignment, int marginX1, int marginX2, int marginY,
			ISkinSimple spriteContainer, double maxMessageSize, UFont fontForStereotype,
			HtmlColor htmlColorForStereotype) {
		this(Display.getWithNewlines(label == null ? "" : label.toString()), font, horizontalAlignment, marginX1,
				marginX2, marginY, spriteContainer, maxMessageSize, false, fontForStereotype, htmlColorForStereotype);
	}

	public AbstractTextualComponent(Display strings, FontConfiguration font, HorizontalAlignment horizontalAlignment,
			int marginX1, int marginX2, int marginY, ISkinSimple spriteContainer, double maxMessageSize,
			boolean enhanced, UFont fontForStereotype, HtmlColor htmlColorForStereotype) {
		this.font = font.getFont();
		this.fontColor = font.getColor();
		this.marginX1 = marginX1;
		this.marginX2 = marginX2;
		this.marginY = marginY;
		this.strings = strings;

		if (strings.size() == 1 && strings.get(0).length() == 0) {
			textBlock = new TextBlockEmpty();
		} else if (enhanced) {
			textBlock = new BodyEnhanced2(strings, FontParam.NOTE, spriteContainer, HorizontalAlignment.LEFT, font);
		} else {
			textBlock = strings.create(font, horizontalAlignment, spriteContainer, maxMessageSize, CreoleMode.FULL,
					fontForStereotype, htmlColorForStereotype);
		}
	}

	final protected TextBlock getTextBlock() {
		return textBlock;
	}

	final protected double getPureTextWidth(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getWidth();
	}

	final public double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + marginX1 + marginX2;
	}

	// // For cache
	// private Dimension2D size;
	//
	// private Dimension2D getSize(StringBounder stringBounder, final TextBlock textBlock) {
	// if (size == null) {
	// size = textBlock.calculateDimension(stringBounder);
	// }
	// return size;
	// }

	final protected double getTextHeight(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getHeight() + 2 * marginY;
	}

	final protected Display getLabels() {
		return strings;
	}

	final protected int getMarginX1() {
		return marginX1;
	}

	final protected int getMarginX2() {
		return marginX2;
	}

	final protected int getMarginY() {
		return marginY;
	}

	final protected UFont getFont() {
		return font;
	}

	protected HtmlColor getFontColor() {
		return fontColor;
	}

}
