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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementRadioCheckbox extends AbstractElement {

	private static final int RECTANGLE = 10;
	private static final int ELLIPSE = 10;
	private static final int ELLIPSE2 = 4;
	private final TextBlock block;
	private final int margin = 20;
	private final double stroke = 1.5;
	private final boolean radio;
	private final boolean checked;

	public ElementRadioCheckbox(List<String> text, UFont font, boolean radio, boolean checked,
			ISkinSimple spriteContainer) {
		final FontConfiguration config = FontConfiguration.blackBlueTrue(font);
		this.block = Display.create(text).create(config, HorizontalAlignment.LEFT, spriteContainer);
		this.radio = radio;
		this.checked = checked;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D dim = block.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, margin, 0);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		block.drawU(ug.apply(new UTranslate(margin, 0)));

		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		final double height = dim.getHeight();

		ug = ug.apply(new UStroke(stroke));
		if (radio) {
			ug.apply(new UTranslate(2, (height - ELLIPSE) / 2)).draw(new UEllipse(ELLIPSE, ELLIPSE));
			if (checked) {
				ug.apply(new UChangeBackColor(ug.getParam().getColor()))
						.apply(new UTranslate(2 + (ELLIPSE - ELLIPSE2) / 2, (height - ELLIPSE2) / 2))
						.draw(new UEllipse(ELLIPSE2, ELLIPSE2));
			}
		} else {
			ug.apply(new UTranslate(2, (height - RECTANGLE) / 2)).draw(new URectangle(RECTANGLE, RECTANGLE));
			if (checked) {
				final UPolygon poly = new UPolygon();
				poly.addPoint(0, 0);
				poly.addPoint(3, 3);
				poly.addPoint(10, -6);
				poly.addPoint(3, 1);
				ug.apply(new UChangeBackColor(ug.getParam().getColor())).apply(new UTranslate(3, 6)).draw(poly);
			}
		}
	}
}