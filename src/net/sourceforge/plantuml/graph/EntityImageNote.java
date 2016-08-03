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
import java.awt.Polygon;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

class EntityImageNote extends AbstractEntityImage {

	final private TextBlock text;

	private final int xMargin = 10;
	private final int yMargin = 10;

	public EntityImageNote(IEntity entity) {
		super(entity);
		this.text = entity.getDisplay().create(FontConfiguration.blackBlueTrue(getFont14()),
				HorizontalAlignment.CENTER, new SpriteContainerEmpty());
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = text.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, 2 * xMargin, 2 * yMargin);
	}

	@Override
	public void draw(ColorMapper colorMapper, Graphics2D g2d) {
		final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder());

		final int width = (int) dimTotal.getWidth();
		final int height = (int) dimTotal.getHeight();

		final Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(width - xMargin, 0);
		p.addPoint(width, yMargin);
		p.addPoint(width, height);
		p.addPoint(0, height);

		g2d.setColor(colorMapper.getMappedColor(getYellowNote()));
		g2d.fill(p);

		g2d.setColor(colorMapper.getMappedColor(getRed()));
		g2d.draw(p);
		g2d.drawLine(width - xMargin, 0, width - xMargin, yMargin);
		g2d.drawLine(width - xMargin, yMargin, width, yMargin);

		g2d.setColor(Color.BLACK);
		// text.drawTOBEREMOVED(colorMapper, g2d, xMargin, yMargin);

	}
}
