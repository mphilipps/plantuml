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
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.PathIterator;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.eps.EpsGraphicsMacroAndText;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.g2d.DriverTextAsPathG2d;

public class DriverTextEps implements UDriver<EpsGraphics> {

	private final StringBounder stringBounder;
	private final ClipContainer clipContainer;
	private final FontRenderContext fontRenderContext;
	private final EpsStrategy strategy;

	public DriverTextEps(ClipContainer clipContainer, EpsStrategy strategy) {
		this.stringBounder = TextBlockUtils.getDummyStringBounder();
		this.clipContainer = clipContainer;
		this.fontRenderContext = TextBlockUtils.getFontRenderContext();
		this.strategy = strategy;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, EpsGraphics eps) {

		final UClip clip = clipContainer.getClip();
		if (clip != null && clip.isInside(x, y) == false) {
			return;
		}

		final UText shape = (UText) ushape;

		if (strategy == EpsStrategy.WITH_MACRO_AND_TEXT) {
			drawAsText(shape, x, y, param, eps, mapper);
			return;
		}

		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		final UFont font = fontConfiguration.getFont();

		final TextLayout t = new TextLayout(shape.getText(), font.getFont(), fontRenderContext);

		MinMax dim = null;

		if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
			final Color extended = mapper.getMappedColor(fontConfiguration.getExtendedColor());
			if (extended != null) {
				eps.setStrokeColor(extended);
				eps.setFillColor(extended);
				eps.setStrokeWidth("1", 0, 0);
				if (dim == null) {
					dim = getMinMax(x, y, t.getOutline(null).getPathIterator(null));
				}
				eps.epsRectangle(dim.getMinX() - 1, dim.getMinY() - 1, dim.getWidth() + 2, dim.getHeight() + 2, 0, 0);
			}
		}

		eps.setStrokeColor(mapper.getMappedColor(fontConfiguration.getColor()));
		drawPathIterator(eps, x, y, t.getOutline(null).getPathIterator(null));

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			final HtmlColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(mapper.getMappedColor(extended));
			}
			if (dim == null) {
				dim = getMinMax(x, y, t.getOutline(null).getPathIterator(null));
			}
			eps.setStrokeWidth("1.1", 0, 0);
			eps.epsLine(x, y + 1.5, x + dim.getWidth(), y + 1.5);
			eps.setStrokeWidth("1", 0, 0);
		}
		if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
			if (dim == null) {
				dim = getMinMax(x, y, t.getOutline(null).getPathIterator(null));
			}
			final int ypos = (int) (y + 2.5) - 1;
			final HtmlColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(mapper.getMappedColor(extended));
			}
			eps.setStrokeWidth("1.1", 0, 0);
			for (int i = (int) x; i < x + dim.getWidth() - 5; i += 6) {
				eps.epsLine(i, ypos - 0, i + 3, ypos + 1);
				eps.epsLine(i + 3, ypos + 1, i + 6, ypos - 0);
			}
			eps.setStrokeWidth("1", 0, 0);
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final HtmlColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(mapper.getMappedColor(extended));
			}
			if (dim == null) {
				dim = getMinMax(x, y, t.getOutline(null).getPathIterator(null));
			}
			// final FontMetrics fm = font.getFontMetrics();
			final double ypos = (dim.getMinY() + dim.getMaxY() * 2) / 3;
			eps.setStrokeWidth("1.3", 0, 0);
			eps.epsLine(x, ypos, x + dim.getWidth(), ypos);
			eps.setStrokeWidth("1", 0, 0);
		}

	}

	private void drawAsText(UText shape, double x, double y, UParam param, EpsGraphics eps, ColorMapper mapper) {
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		// final FontMetrics fm = g2dummy.getFontMetrics(fontConfiguration.getFont().getFont());
		// final double ypos = y - fm.getDescent() + 0.5;
		final double ypos = y - 1;

		eps.setStrokeColor(mapper.getMappedColor(fontConfiguration.getColor()));
		((EpsGraphicsMacroAndText) eps).drawText(shape.getText(), fontConfiguration, x, ypos);

	}

	static void drawPathIterator(EpsGraphics eps, double x, double y, PathIterator path) {

		eps.newpath();
		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			if (code == PathIterator.SEG_MOVETO) {
				eps.moveto(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_LINETO) {
				eps.lineto(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_CLOSE) {
				eps.closepath();
			} else if (code == PathIterator.SEG_CUBICTO) {
				eps.curveto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
			} else if (code == PathIterator.SEG_QUADTO) {
				eps.quadto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y);
			} else {
				throw new UnsupportedOperationException("code=" + code);
			}

			path.next();
		}

		eps.fill(path.getWindingRule());

	}

	static private MinMax getMinMax(double x, double y, PathIterator path) {

		MinMax result = MinMax.getEmpty(false);

		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			if (code == PathIterator.SEG_MOVETO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_LINETO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_CLOSE) {
			} else if (code == PathIterator.SEG_CUBICTO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
				result = result.addPoint(coord[2] + x, coord[3] + y);
				result = result.addPoint(coord[4] + x, coord[5] + y);
			} else if (code == PathIterator.SEG_QUADTO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
				result = result.addPoint(coord[2] + x, coord[3] + y);
			} else {
				throw new UnsupportedOperationException("code=" + code);
			}
			path.next();
		}

		return result;

	}
}
