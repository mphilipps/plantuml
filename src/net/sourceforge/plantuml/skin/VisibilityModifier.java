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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public enum VisibilityModifier {
	PRIVATE_FIELD(ColorParam.iconPrivate, null), PROTECTED_FIELD(ColorParam.iconProtected, null), PACKAGE_PRIVATE_FIELD(
			ColorParam.iconPackage, null), PUBLIC_FIELD(ColorParam.iconPublic, null),

	PRIVATE_METHOD(ColorParam.iconPrivate, ColorParam.iconPrivateBackground), PROTECTED_METHOD(
			ColorParam.iconProtected, ColorParam.iconProtectedBackground), PACKAGE_PRIVATE_METHOD(
			ColorParam.iconPackage, ColorParam.iconPackageBackground), PUBLIC_METHOD(ColorParam.iconPublic,
			ColorParam.iconPublicBackground);

	private final ColorParam foregroundParam;
	private final ColorParam backgroundParam;

	private VisibilityModifier(ColorParam foreground, ColorParam background) {
		this.foregroundParam = foreground;
		this.backgroundParam = background;
	}

	public UDrawable getUDrawable(final int size, final HtmlColor foregroundColor, final HtmlColor backgoundColor) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug, size, foregroundColor, backgoundColor, 0, 0);
			}

		};
	}

	public TextBlock getUBlock(final int size, final HtmlColor foregroundColor, final HtmlColor backgoundColor) {
		return new AbstractTextBlock() {

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(size + 1, size + 1);
			}
			
			@Override
			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder) {
				return null;
			}

			public void drawU(UGraphic ug) {
				drawInternal(ug, size, foregroundColor, backgoundColor, 0, 0);
			}
		};
	}

	private void drawInternal(UGraphic ug, int size, final HtmlColor foregroundColor, final HtmlColor backgoundColor,
			double x, double y) {
		ug = ug.apply(new UChangeBackColor(backgoundColor)).apply(new UChangeColor(foregroundColor));
		size = ensureEven(size);
		switch (this) {
		case PACKAGE_PRIVATE_FIELD:
			drawTriangle(ug, false, size, x, y);
			break;

		case PRIVATE_FIELD:
			drawSquare(ug, false, size, x, y);
			break;

		case PROTECTED_FIELD:
			drawDiamond(ug, false, size, x, y);
			break;

		case PUBLIC_FIELD:
			drawCircle(ug, false, size, x, y);
			break;

		case PACKAGE_PRIVATE_METHOD:
			drawTriangle(ug, true, size, x, y);
			break;

		case PRIVATE_METHOD:
			drawSquare(ug, true, size, x, y);
			break;

		case PROTECTED_METHOD:
			drawDiamond(ug, true, size, x, y);
			break;

		case PUBLIC_METHOD:
			drawCircle(ug, true, size, x, y);
			break;

		default:
			throw new IllegalStateException();
		}
	}

	private void drawSquare(UGraphic ug, boolean filled, int size, double x, double y) {
		ug.apply(new UTranslate(x + 2, y + 2)).draw(new URectangle(size - 4, size - 4));
	}

	private void drawCircle(UGraphic ug, boolean filled, int size, double x, double y) {
		ug.apply(new UTranslate(x + 2, y + 2)).draw(new UEllipse(size - 4, size - 4));
	}

	static private int ensureEven(int n) {
		if (n % 2 == 1) {
			n--;
		}
		return n;
	}

	private void drawDiamond(UGraphic ug, boolean filled, int size, double x, double y) {
		final UPolygon poly = new UPolygon();
		size -= 2;
		poly.addPoint(size / 2.0, 0);
		poly.addPoint(size, size / 2.0);
		poly.addPoint(size / 2.0, size);
		poly.addPoint(0, size / 2.0);
		ug.apply(new UTranslate(x + 1, y)).draw(poly);
	}

	private void drawTriangle(UGraphic ug, boolean filled, int size, double x, double y) {
		final UPolygon poly = new UPolygon();
		size -= 2;
		poly.addPoint(size / 2.0, 1);
		poly.addPoint(0, size - 1);
		poly.addPoint(size, size - 1);
		ug.apply(new UTranslate(x + 1, y)).draw(poly);
	}

	public static boolean isVisibilityCharacter(char c) {
		if (c == '-') {
			return true;
		}
		if (c == '#') {
			return true;
		}
		if (c == '+') {
			return true;
		}
		if (c == '~') {
			return true;
		}
		return false;
	}

	public static VisibilityModifier getVisibilityModifier(char c, boolean isField) {
		if (isField) {
			return getVisibilityModifierForField(c);
		}
		return getVisibilityModifierForMethod(c);
	}

	private static VisibilityModifier getVisibilityModifierForField(char c) {
		if (c == '-') {
			return VisibilityModifier.PRIVATE_FIELD;
		}
		if (c == '#') {
			return VisibilityModifier.PROTECTED_FIELD;
		}
		if (c == '+') {
			return VisibilityModifier.PUBLIC_FIELD;
		}
		if (c == '~') {
			return VisibilityModifier.PACKAGE_PRIVATE_FIELD;
		}
		return null;
	}

	private static VisibilityModifier getVisibilityModifierForMethod(char c) {
		if (c == '-') {
			return VisibilityModifier.PRIVATE_METHOD;
		}
		if (c == '#') {
			return VisibilityModifier.PROTECTED_METHOD;
		}
		if (c == '+') {
			return VisibilityModifier.PUBLIC_METHOD;
		}
		if (c == '~') {
			return VisibilityModifier.PACKAGE_PRIVATE_METHOD;
		}
		return null;
	}

	public final ColorParam getForeground() {
		return foregroundParam;
	}

	public final ColorParam getBackground() {
		return backgroundParam;
	}

}
