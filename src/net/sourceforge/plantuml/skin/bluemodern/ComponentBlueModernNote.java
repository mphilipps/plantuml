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

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

final public class ComponentBlueModernNote extends AbstractTextualComponent {

	private final int shadowview = 4;
	private final int cornersize = 10;
	private final HtmlColor back;
	private final HtmlColor foregroundColor;

	public ComponentBlueModernNote(HtmlColor back, HtmlColor foregroundColor, FontConfiguration font,
			Display strings, ISkinSimple spriteContainer) {
		super(strings, font, HorizontalAlignment.LEFT, 6, 15, 5, spriteContainer, 0, false,
				null, null);
		this.back = back;
		this.foregroundColor = foregroundColor;
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder) + 2 * getPaddingX();
		return result;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 2 * getPaddingY();
	}

	@Override
	public double getPaddingX() {
		return 9;
	}

	@Override
	public double getPaddingY() {
		return 9;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double textHeight = getTextHeight(stringBounder);

		final double textWidth = getTextWidth(stringBounder);

		final ShadowShape shadowShape = new ShadowShape(textWidth, textHeight, 3);
		shadowShape.drawU(ug.apply(new UTranslate(shadowview, shadowview)));

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, textHeight);
		polygon.addPoint(textWidth, textHeight);
		polygon.addPoint(textWidth, cornersize);
		polygon.addPoint(textWidth - cornersize, 0);
		polygon.addPoint(0, 0);

		ug = ug.apply(new UChangeBackColor(back));
		ug = ug.apply(new UChangeColor(foregroundColor));
		ug.draw(polygon);

		ug.apply(new UTranslate(textWidth - cornersize, 0)).draw(new ULine(0, cornersize));
		ug.apply(new UTranslate(textWidth, cornersize)).draw(new ULine(-cornersize, 0));

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));

	}

}
