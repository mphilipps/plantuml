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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class Opale extends AbstractTextBlock implements TextBlock {

	private final int cornersize = 10;
	private final HtmlColor noteBackgroundColor;
	private final HtmlColor borderColor;
	private final int marginX1 = 6;
	private final int marginX2 = 15;
	private final int marginY = 5;
	private final boolean withShadow;
	private Direction strategy;
	private Point2D pp1;
	private Point2D pp2;
	private final boolean withLink;

	private final TextBlock textBlock;

	public Opale(HtmlColor borderColor, HtmlColor noteBackgroundColor, TextBlock textBlock, boolean withShadow,
			boolean withLink) {
		this.noteBackgroundColor = noteBackgroundColor;
		this.withLink = withLink;

		this.withShadow = withShadow;
		this.borderColor = borderColor;
		this.textBlock = textBlock;
	}

	public void setOpale(Direction strategy, Point2D pp1, Point2D pp2) {
		this.strategy = strategy;
		this.pp1 = pp1;
		this.pp2 = pp2;
	}

	final private double getWidth(StringBounder stringBounder) {
		return textBlock.calculateDimension(stringBounder).getWidth() + marginX1 + marginX2;
	}

	final private double getHeight(StringBounder stringBounder) {
		final Dimension2D size = textBlock.calculateDimension(stringBounder);
		return size.getHeight() + 2 * marginY;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double height = getHeight(stringBounder);
		final double width = getWidth(stringBounder);
		return new Dimension2DDouble(width, height);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final UPolygon polygon = getPolygonNormal(stringBounder);
		if (withShadow) {
			polygon.setDeltaShadow(4);
		}
		ug = ug.apply(new UChangeBackColor(noteBackgroundColor)).apply(new UChangeColor(borderColor));
		ug.draw(polygon);

		if (withLink) {
			final UShape polygonOpale;
			if (strategy == Direction.LEFT) {
				polygonOpale = getPolygonLeft(stringBounder, pp1, pp2);
			} else if (strategy == Direction.RIGHT) {
				polygonOpale = getPolygonRight(stringBounder, pp1, pp2);
			} else if (strategy == Direction.UP) {
				polygonOpale = getPolygonUp(stringBounder, pp1, pp2);
			} else if (strategy == Direction.DOWN) {
				polygonOpale = getPolygonDown(stringBounder, pp1, pp2);
			} else {
				throw new IllegalArgumentException();
			}
			ug.draw(polygonOpale);
		}
		ug.apply(new UTranslate(getWidth(stringBounder) - cornersize, 0)).draw(new ULine(0, cornersize));
		ug.apply(new UTranslate(getWidth(stringBounder), cornersize)).draw(new ULine(-cornersize, 0));
		textBlock.drawU(ug.apply(new UTranslate(marginX1, marginY)));
	}

	private UPolygon getPolygonNormal(final StringBounder stringBounder) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), cornersize);
		polygon.addPoint(getWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private final double delta = 4;

	private UPolygon getPolygonLeft(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);

		double y1 = pp1.getY() - delta;
		y1 = MathUtils.limitation(y1, 0, getHeight(stringBounder) - 2 * delta);
		polygon.addPoint(0, y1);
		polygon.addPoint(pp2.getX(), pp2.getY());
		polygon.addPoint(0, y1 + 2 * delta);

		polygon.addPoint(0, getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), cornersize);
		polygon.addPoint(getWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private UPolygon getPolygonRight(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), getHeight(stringBounder));

		double y1 = pp1.getY() - delta;
		y1 = MathUtils.limitation(y1, cornersize, getHeight(stringBounder) - 2 * delta);
		polygon.addPoint(getWidth(stringBounder), y1 + 2 * delta);
		polygon.addPoint(pp2.getX(), pp2.getY());
		polygon.addPoint(getWidth(stringBounder), y1);

		polygon.addPoint(getWidth(stringBounder), cornersize);
		polygon.addPoint(getWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private UPolygon getPolygonUp(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), cornersize);
		polygon.addPoint(getWidth(stringBounder) - cornersize, 0);

		double x1 = pp1.getX() - delta;
		x1 = MathUtils.limitation(x1, 0, getWidth(stringBounder) - cornersize);
		polygon.addPoint(x1 + 2 * delta, 0);
		polygon.addPoint(pp2.getX(), pp2.getY());

		polygon.addPoint(x1, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private UPolygon getPolygonDown(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getHeight(stringBounder));

		double x1 = pp1.getX() - delta;
		x1 = MathUtils.limitation(x1, 0, getWidth(stringBounder));
		polygon.addPoint(x1, getHeight(stringBounder));
		polygon.addPoint(pp2.getX(), pp2.getY());
		polygon.addPoint(x1 + 2 * delta, getHeight(stringBounder));

		polygon.addPoint(getWidth(stringBounder), getHeight(stringBounder));
		polygon.addPoint(getWidth(stringBounder), cornersize);
		polygon.addPoint(getWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	public final int getMarginX1() {
		return marginX1;
	}

}
