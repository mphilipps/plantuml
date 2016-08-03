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
package net.sourceforge.plantuml.graph;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MethodsOrFieldsArea {

	private final UFont font;
	private final List<String> strings = new ArrayList<String>();

	public MethodsOrFieldsArea(List<Member> attributes, UFont font) {
		this.font = font;
		for (Member att : attributes) {
			this.strings.add(att.getDisplay(false));
		}
	}

	public VisibilityModifier getVisibilityModifier() {
		throw new UnsupportedOperationException();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double x = 0;
		double y = 0;
		for (String s : strings) {
			final TextBlock bloc = createTextBlock(s);
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			y += dim.getHeight();
			x = Math.max(dim.getWidth(), x);
		}
		return new Dimension2DDouble(x, y);
	}

	private TextBlock createTextBlock(String s) {
		return Display.create(s).create(FontConfiguration.blackBlueTrue(font), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	public void draw(UGraphic ug, double x, double y) {
		for (String s : strings) {
			final TextBlock bloc = createTextBlock(s);
			bloc.drawU(ug.apply(new UTranslate(x, y)));
			y += bloc.calculateDimension(ug.getStringBounder()).getHeight();
		}
	}

}
