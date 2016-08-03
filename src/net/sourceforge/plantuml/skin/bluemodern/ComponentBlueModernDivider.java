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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernDivider extends AbstractTextualComponent {

	private final HtmlColor background1;
	private final HtmlColor background2;
	private final HtmlColor borderColor;

	public ComponentBlueModernDivider(FontConfiguration font, HtmlColor background1, HtmlColor background2,
			HtmlColor borderColor, Display stringsToDisplay, ISkinSimple spriteContainer) {
		super(stringsToDisplay, font, HorizontalAlignment.CENTER, 4, 4, 4,
				spriteContainer, 0, false, null, null);
		this.background1 = background1;
		this.background2 = background2;
		this.borderColor = borderColor;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final double textWidth = getTextWidth(stringBounder);
		final double textHeight = getTextHeight(stringBounder);

		final double deltaX = 6;
		final double xpos = (dimensionToUse.getWidth() - textWidth - deltaX) / 2;
		final double ypos = (dimensionToUse.getHeight() - textHeight) / 2;

		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.BLACK));
		ug = ug.apply(new UStroke(2));

		ug.apply(new UTranslate(0, dimensionToUse.getHeight() / 2 - 1)).draw(new ULine(dimensionToUse.getWidth(), 0));
		ug.apply(new UTranslate(0, dimensionToUse.getHeight() / 2 + 2)).draw(new ULine(dimensionToUse.getWidth(), 0));

		final FillRoundShape shape = new FillRoundShape(textWidth + deltaX, textHeight, background1, background2, 5);
		shape.drawU(ug.apply(new UTranslate(xpos, ypos)));

		ug = ug.apply(new UChangeColor(borderColor));
		ug = ug.apply(new UChangeBackColor(null));
		ug.apply(new UTranslate(xpos, ypos)).draw(new URectangle(textWidth + deltaX, textHeight, 5, 5));
		ug = ug.apply(new UStroke());

		textBlock.drawU(ug.apply(new UTranslate(xpos + deltaX, ypos + getMarginY())));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + 30;
	}

}
