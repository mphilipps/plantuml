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
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernParticipant extends AbstractTextualComponent {

	private final int shadowview = 3;
	private final HtmlColor blue1;
	private final HtmlColor blue2;

	public ComponentBlueModernParticipant(HtmlColor blue1, HtmlColor blue2, FontConfiguration font,
			Display stringsToDisplay, ISkinSimple spriteContainer) {
		super(stringsToDisplay, font, HorizontalAlignment.CENTER, 7, 7, 7,
				spriteContainer, 0, false, null, null);
		this.blue1 = blue1;
		this.blue2 = blue2;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();

		final ShadowShape shadowShape = new ShadowShape(getTextWidth(stringBounder), getTextHeight(stringBounder), 10);
		final UGraphic ugShadow = ug.apply(new UTranslate(shadowview, shadowview)).apply(new UChangeColor(null));
		// ug.translate(shadowview, shadowview);
		shadowShape.drawU(ugShadow);
		// ug.translate(-shadowview, -shadowview);

		final FillRoundShape shape = new FillRoundShape(getTextWidth(stringBounder), getTextHeight(stringBounder),
				blue1, blue2, 10);
		shape.drawU(ug);

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), getMarginY())));
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + shadowview;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

}
