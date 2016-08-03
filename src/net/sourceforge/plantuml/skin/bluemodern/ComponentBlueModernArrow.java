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

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentBlueModernArrow extends AbstractComponentBlueModernArrow {

	public ComponentBlueModernArrow(HtmlColor foregroundColor, boolean useUnderlineForHyperlink, FontConfiguration font, Display stringsToDisplay, ArrowConfiguration arrowConfiguration,
			ISkinSimple spriteContainer) {
		super(foregroundColor, font, stringsToDisplay, arrowConfiguration, spriteContainer);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		ug = ug.apply(new UChangeColor(getForegroundColor()));
		ug = ug.apply(new UChangeBackColor(getForegroundColor()));

		final int x2 = (int) dimensionToUse.getWidth();

		if (getArrowConfiguration().isDotted()) {
			ug = stroke(ug, 5, 2);
		} else {
			ug = ug.apply(new UStroke(2));
		}

		ug.apply(new UTranslate(2, textHeight)).draw(new ULine(x2 - 4, 0));
		ug = ug.apply(new UStroke());

		final int direction = getDirection();
		final UPolygon polygon = new UPolygon();

		if (getArrowConfiguration().isAsync()) {
			ug = ug.apply(new UStroke(1.5));
			if (direction == 1) {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.apply(new UTranslate(x2 - getArrowDeltaX2(), textHeight - getArrowDeltaY2())).draw(new ULine(getArrowDeltaX2(),
					getArrowDeltaY2()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.apply(new UTranslate(x2 - getArrowDeltaX2(), textHeight + getArrowDeltaY2())).draw(new ULine(getArrowDeltaX2(),
					-getArrowDeltaY2()));
				}
			} else {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					ug.apply(new UTranslate(getArrowDeltaX2(), textHeight - getArrowDeltaY2())).draw(new ULine(-getArrowDeltaX2(),
					getArrowDeltaY2()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					ug.apply(new UTranslate(getArrowDeltaX2(), textHeight + getArrowDeltaY2())).draw(new ULine(-getArrowDeltaX2(),
					-getArrowDeltaY2()));
				}
			}
			ug = ug.apply(new UStroke());
		} else if (direction == 1) {
			createPolygonNormal(textHeight, x2, polygon);
		} else {
			createPolygonReverse(textHeight, polygon);
		}
		ug.draw(polygon);

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), 0)));
	}

	private void createPolygonReverse(final int textHeight, final UPolygon polygon) {
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight);
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY());
		} else {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY());
		}
	}

	private void createPolygonNormal(final int textHeight, final int x2, final UPolygon polygon) {
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight);
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY());
		} else {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY());
		}
	}

	protected int getDirection(Graphics2D g2d) {
		return getDirection();
	}

	protected int getDirection() {
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return 1;
		}
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
			return -1;
		}
		throw new IllegalStateException();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + 2 * getPaddingY();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder);
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getArrowConfiguration().getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

}
