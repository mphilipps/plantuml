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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public class UFont {

	private final Font font;
	private final String family;

	public UFont(String fontFamily, int fontStyle, int fontSize) {
		this(new Font(fontFamily, fontStyle, fontSize), fontFamily);
	}

	private UFont(Font font, String family) {
		this.font = font;
		this.family = family;
	}

	public final Font getFont() {
		return font;
	}

	public FontConfiguration toFont2(HtmlColor color, boolean useUnderlineForHyperlink, HtmlColor hyperlinkColor,
			int tabSize) {
		return new FontConfiguration(this, color, hyperlinkColor, useUnderlineForHyperlink, tabSize);
	}

	public UFont scaled(double scale) {
		if (scale == 1) {
			return this;
		}
		final float current = font.getSize2D();
		return deriveSize((float) (current * scale));
	}

	public UFont deriveSize(float size) {
		return new UFont(font.deriveFont(size), family);
	}

	public UFont deriveStyle(int style) {
		return new UFont(font.deriveFont(style), family);
	}

	public int getStyle() {
		return font.getStyle();
	}

	public int getSize() {
		return font.getSize();
	}

	public double getSize2D() {
		return font.getSize2D();
	}

	public boolean isBold() {
		return font.isBold();
	}

	public boolean isItalic() {
		return font.isItalic();
	}

	public String getFamily(UFontContext context) {
		if (context == UFontContext.EPS) {
			if (family == null) {
				return "Times-Roman";
			}
			return font.getPSName();
		}
		if (context == UFontContext.SVG) {
			if (family.equalsIgnoreCase("sansserif")) {
				return "sans-serif";
			}
			return family;
		}
		return family;
	}

	@Override
	public String toString() {
		return font.toString()/* + " " + font.getPSName() */;
	}

	@Override
	public int hashCode() {
		return font.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UFont == false) {
			return false;
		}
		return this.font.equals(((UFont) obj).font);
	}

	@Deprecated
	public static UFont getCurrentFont(Graphics2D g2d) {
		// return new UFont(g2d.getFont(), g2d.getFont().getFontName());
		throw new UnsupportedOperationException();
	}

	public LineMetrics getLineMetrics(Graphics2D gg, String text) {
		final FontRenderContext frc = gg.getFontRenderContext();
		return font.getLineMetrics(text, frc);
	}

	public FontMetrics getFontMetrics() {
		return TextBlockUtils.getFontMetrics(getFont());
	}

}
