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

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class FillRoundShape {

	final private double width;
	final private double height;
	final private double corner;
	final private HtmlColor c1;
	final private HtmlColor c2;

	public FillRoundShape(double width, double height, HtmlColor c1, HtmlColor c2, double corner) {
		this.width = width;
		this.height = height;
		this.c1 = c1;
		this.c2 = c2;
		this.corner = corner;

	}

	public void draw(ColorMapper mapper, Graphics2D g2d) {
		final GradientPaint paint = new GradientPaint(0, 0, mapper.getMappedColor(c1), (float) width, (float) height,
				mapper.getMappedColor(c2));
		final RoundRectangle2D r = new RoundRectangle2D.Double(0, 0, width, height, corner * 2, corner * 2);
		g2d.setPaint(paint);
		g2d.fill(r);
	}

	public void drawU(UGraphic ug) {
		final HtmlColorGradient gradient = new HtmlColorGradient(c1, c2, '\\');
		final URectangle r = new URectangle(width, height, corner * 2, corner * 2);
		ug.apply(new UChangeBackColor(gradient)).draw(r);
	}

}
