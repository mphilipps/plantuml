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

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

class GrayComponent extends AbstractComponent {

	private static final UFont NORMAL = new UFont("SansSerif", Font.PLAIN, 7);

	private final ComponentType type;

	public GrayComponent(ComponentType type) {
		this.type = type;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UChangeColor(HtmlColorUtils.BLACK));
		ug.draw(new URectangle(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder)));

		final String n = type.name();
		final int split = 9;
		final List<String> strings = new ArrayList<String>();
		for (int i = 0; i < n.length(); i += split) {
			strings.add(n.substring(i, Math.min(i + split, n.length())));
		}

		final TextBlock textBlock = Display.create(strings).create(FontConfiguration.blackBlueTrue(NORMAL),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		textBlock.drawU(ug);
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 42;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 42;
	}

}
