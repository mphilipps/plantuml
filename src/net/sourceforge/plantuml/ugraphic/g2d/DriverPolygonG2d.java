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
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverPolygonG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;
	private final EnsureVisible visible;

	public DriverPolygonG2d(double dpiFactor, EnsureVisible visible) {
		this.dpiFactor = dpiFactor;
		this.visible = visible;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UPolygon shape = (UPolygon) ushape;

		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));

		final GeneralPath path = new GeneralPath();

		boolean first = true;
		for (Point2D pt : shape.getPoints()) {
			final double xp = pt.getX() + x;
			final double yp = pt.getY() + y;
			visible.ensureVisible(xp, yp);
			if (first) {
				path.moveTo((float) xp, (float) yp);
			} else {
				path.lineTo((float) xp, (float) yp);
			}
			first = false;
		}

		if (first == false) {
			path.closePath();
		}

		if (shape.getDeltaShadow() != 0) {
			drawShadow(g2d, path, shape.getDeltaShadow(), dpiFactor);
		}

		final HtmlColor back = param.getBackcolor();
		if (back instanceof HtmlColorGradient) {
			final HtmlColorGradient gr = (HtmlColorGradient) back;
			final char policy = gr.getPolicy();
			final GradientPaint paint;
//			final Rectangle2D bound = path.getBounds();
			if (policy == '|') {
				paint = new GradientPaint((float) x, (float) (y + shape.getHeight()) / 2, mapper.getMappedColor(gr
						.getColor1()), (float) (x + shape.getWidth()), (float) (y + shape.getHeight()) / 2,
						mapper.getMappedColor(gr.getColor2()));
			} else if (policy == '\\') {
				paint = new GradientPaint((float) x, (float) (y + shape.getHeight()), mapper.getMappedColor(gr
						.getColor1()), (float) (x + shape.getWidth()), (float) y, mapper.getMappedColor(gr.getColor2()));
			} else if (policy == '-') {
				paint = new GradientPaint((float) (x + shape.getWidth()) / 2, (float) y, mapper.getMappedColor(gr
						.getColor1()), (float) (x + shape.getWidth()) / 2, (float) (y + shape.getHeight()),
						mapper.getMappedColor(gr.getColor2()));
			} else {
				// for /
				paint = new GradientPaint((float) x, (float) y, mapper.getMappedColor(gr.getColor1()),
						(float) (x + shape.getWidth()), (float) (y + shape.getHeight()), mapper.getMappedColor(gr
								.getColor2()));
			}
			g2d.setPaint(paint);
			g2d.fill(path);
		} else if (back!=null) {
			g2d.setColor(mapper.getMappedColor(back));
			DriverRectangleG2d.managePattern(param, g2d);
			g2d.fill(path);
		}

		if (param.getColor() != null) {
			g2d.setColor(mapper.getMappedColor(param.getColor()));
			DriverLineG2d.manageStroke(param, g2d);
			g2d.draw(path);
		}
	}
}
