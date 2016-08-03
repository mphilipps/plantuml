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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

class EntityImageUsecase extends AbstractEntityImage {

	final private TextBlock name;

	public EntityImageUsecase(IEntity entity) {
		super(entity);
		this.name = entity.getDisplay().create(FontConfiguration.blackBlueTrue(getFont14()),
				HorizontalAlignment.CENTER, new SpriteContainerEmpty());
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		// final double eps = Math.sqrt(nameDim.getWidth() /
		// nameDim.getHeight());
		// final double diag = Math.sqrt(nameDim.getWidth() * nameDim.getWidth()
		// + nameDim.getHeight()
		// * nameDim.getHeight());
		// return new Dimension2DDouble(diag * eps, diag / eps);
		final double eps = 1.7;
		return new Dimension2DDouble(nameDim.getWidth() * eps, nameDim.getHeight() * eps);
	}

	@Override
	public void draw(ColorMapper colorMapper, Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder());

		// Shape ellipse = new Ellipse2D.Double(0, 0, dimTotal.getWidth(),
		// dimTotal.getHeight());
		final GeneralPath ellipse = new GeneralPath();
		final double h = dimTotal.getHeight();
		final double w = dimTotal.getWidth();
		ellipse.append(new QuadCurve2D.Double(0, h / 2, 0, 0, w / 2, 0), true);
		ellipse.append(new QuadCurve2D.Double(w / 2, 0, w, 0, w, h / 2), true);
		ellipse.append(new QuadCurve2D.Double(w, h / 2, w, h, w / 2, h), true);
		ellipse.append(new QuadCurve2D.Double(w / 2, h, 0, h, 0, h / 2), true);
		g2d.setColor(colorMapper.getMappedColor(getYellow()));
		g2d.fill(ellipse);

		g2d.setColor(colorMapper.getMappedColor(getRed()));
		g2d.draw(ellipse);

		// final Dimension2D nameDim = name.calculateDimension(StringBounderUtils.asStringBounder(g2d));
		// final double posx = (w - nameDim.getWidth()) / 2;
		// final double posy = (h - nameDim.getHeight()) / 2;
		// final Shape rect = new Rectangle2D.Double(posx, posy, nameDim.getWidth(), nameDim.getHeight());
		// g2d.draw(rect);

		g2d.setColor(Color.BLACK);
		// name.drawTOBEREMOVED(colorMapper, g2d, posx, posy);
	}
}
