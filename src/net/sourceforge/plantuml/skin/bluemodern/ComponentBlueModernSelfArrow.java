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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernSelfArrow extends AbstractComponentBlueModernArrow {

	private final double arrowWidth = 45;

	public ComponentBlueModernSelfArrow(HtmlColor foregroundColor, FontConfiguration font, Display stringsToDisplay, ArrowConfiguration arrowConfiguration, ISkinSimple spriteContainer) {
		super(foregroundColor, font, stringsToDisplay, arrowConfiguration, spriteContainer);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		ug = ug.apply(new UChangeBackColor(getForegroundColor())).apply(new UChangeColor(getForegroundColor()));
		final double x2 = arrowWidth - 3;

		if (getArrowConfiguration().isDotted()) {
			ug = stroke(ug, 5, 2);
		} else {
			ug = ug.apply(new UStroke(2));
		}

		ug.apply(new UTranslate(0, textHeight)).draw(new ULine(x2, 0));

		final int textAndArrowHeight = (int) (textHeight + getArrowOnlyHeight(stringBounder));

		ug.apply(new UTranslate(x2, textHeight)).draw(new ULine(0, textAndArrowHeight - textHeight));
		ug.apply(new UTranslate(x2, textAndArrowHeight)).draw(new ULine(2 - x2, 0));

		ug = ug.apply(new UStroke());

		final int delta = (int) getArrowOnlyHeight(stringBounder);

		if (getArrowConfiguration().isAsync()) {
			if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
				ug.apply(new UStroke(1.5)).apply(new UTranslate(getArrowDeltaX2(), textHeight - getArrowDeltaY2() + delta)).draw(new ULine(-getArrowDeltaX2(), getArrowDeltaY2()));
			}
			if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
				ug.apply(new UStroke(1.5)).apply(new UTranslate(getArrowDeltaX2(), textHeight + getArrowDeltaY2() + delta)).draw(new ULine(-getArrowDeltaX2(), -getArrowDeltaY2()));
			}
		} else {
			final UPolygon polygon = getPolygon(textHeight, delta);
			ug.draw(polygon);
		}

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), 0)));

	}

	private UPolygon getPolygon(final int textHeight, final int delta) {
		final UPolygon polygon = new UPolygon();
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY() + delta);
			polygon.addPoint(0, textHeight + delta);
			polygon.addPoint(getArrowDeltaX(), textHeight + delta);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight + delta);
			polygon.addPoint(0, textHeight + delta);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY() + delta);
		} else {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY() + delta);
			polygon.addPoint(0, textHeight + delta);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY() + delta);
		}
		return polygon;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + getArrowOnlyHeight(stringBounder) + 2 * getPaddingY();
	}

	private double getArrowOnlyHeight(StringBounder stringBounder) {
		return 13;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(getTextWidth(stringBounder), arrowWidth);
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		final int textAndArrowHeight = (int) (textHeight + getArrowOnlyHeight(stringBounder));
		return new Point2D.Double(getPaddingX(), textAndArrowHeight + getPaddingY());
	}

}
