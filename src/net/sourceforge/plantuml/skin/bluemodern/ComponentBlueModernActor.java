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
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernActor extends AbstractTextualComponent {

	private final StickMan stickman;
	private final boolean head;

	public ComponentBlueModernActor(HtmlColor backgroundColor, HtmlColor foregroundColor, FontConfiguration font,
			Display stringsToDisplay, boolean head, ISkinSimple spriteContainer) {
		super(stringsToDisplay, font, HorizontalAlignment.CENTER, 3, 3, 0,
				spriteContainer, 0, false, null, null);
		this.head = head;
		stickman = new StickMan(backgroundColor, foregroundColor);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		ug = ug.apply(new UChangeColor(getFontColor()));
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final double delta = (getPreferredWidth(stringBounder) - stickman.getPreferredWidth()) / 2;

		if (head) {
			textBlock
					.drawU(ug.apply(new UTranslate(getTextMiddlePostion(stringBounder), stickman.getPreferredHeight())));
			ug = ug.apply(new UTranslate(delta, 0));
		} else {
			textBlock.drawU(ug.apply(new UTranslate(getTextMiddlePostion(stringBounder), 0)));
			ug = ug.apply(new UTranslate(delta, getTextHeight(stringBounder)));
		}
		stickman.drawU(ug);

	}

	private double getTextMiddlePostion(StringBounder stringBounder) {
		return (getPreferredWidth(stringBounder) - getTextWidth(stringBounder)) / 2.0;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return stickman.getPreferredHeight() + getTextHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(stickman.getPreferredWidth(), getTextWidth(stringBounder));
	}

}
