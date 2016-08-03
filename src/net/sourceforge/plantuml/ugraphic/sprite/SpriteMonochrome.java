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
package net.sourceforge.plantuml.ugraphic.sprite;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;

public class SpriteMonochrome implements Sprite {

	private final int width;
	private final int height;
	private final int grayLevel;
	private final int pixels[][];

	SpriteMonochrome(int width, int height, int grayLevel) {
		if (grayLevel != 2 && grayLevel != 4 && grayLevel != 8 && grayLevel != 16) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.height = height;
		this.grayLevel = grayLevel;
		this.pixels = new int[height][width];
	}

	void setPixel(int x, int y, int level) {
		if (x < 0 || x >= width) {
			return;
		}
		if (y < 0 || y >= height) {
			return;
		}
		if (level < 0 || level >= grayLevel) {
			throw new IllegalArgumentException("level=" + level + " grayLevel=" + grayLevel);
		}
		pixels[y][x] = level;
	}

	public int getHeight() {
		return height;
	}

	int getWidth() {
		return width;
	}

	public UImage toUImage(ColorMapper colorMapper, HtmlColor backcolor, HtmlColor color) {
		final BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		if (backcolor == null) {
			backcolor = HtmlColorUtils.WHITE;
		}
		if (color == null) {
			color = HtmlColorUtils.BLACK;
		}
		final HtmlColorGradient gradient = new HtmlColorGradient(backcolor, color, '\0');
		for (int col = 0; col < width; col++) {
			for (int line = 0; line < height; line++) {
				final double coef = 1.0 * pixels[line][col] / (grayLevel - 1);
				final Color c = gradient.getColor(colorMapper, coef);
				im.setRGB(col, line, c.getRGB());
			}
		}
		return new UImage(im);
	}

	public TextBlock asTextBlock(final HtmlColor color, final double scale) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final UImage image = toUImage(ug.getColorMapper(), ug.getParam().getBackcolor(), color);
				ug.draw(image.scale(scale));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(getWidth() * scale, getHeight() * scale);
			}
		};
	}

}
