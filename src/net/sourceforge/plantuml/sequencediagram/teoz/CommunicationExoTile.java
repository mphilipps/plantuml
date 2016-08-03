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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.rose.ComponentRoseArrow;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CommunicationExoTile implements TileWithUpdateStairs {

	private final LivingSpace livingSpace;
	private final MessageExo message;
	private final Skin skin;
	private final ISkinParam skinParam;
	private final TileArguments tileArguments;

	public Event getEvent() {
		return message;
	}

	public CommunicationExoTile(LivingSpace livingSpace, MessageExo message, Skin skin, ISkinParam skinParam,
			TileArguments tileArguments) {
		this.tileArguments = tileArguments;
		this.livingSpace = livingSpace;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	private Component getComponent(StringBounder stringBounder) {
		ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		if (message.getType().getDirection() == -1) {
			arrowConfiguration = arrowConfiguration.reverse();
		}
		final Component comp = skin.createComponent(ComponentType.ARROW, arrowConfiguration, skinParam,
				message.getLabel());
		return comp;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		double x1 = getPoint1Value(stringBounder);
		double x2 = getPoint2Value(stringBounder);
		final int level = livingSpace.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
		if (level > 0) {
			if (message.getType().isRightBorder()) {
				x1 += CommunicationTile.LIVE_DELTA_SIZE * level;
			} else {
				x2 += CommunicationTile.LIVE_DELTA_SIZE * (level - 2);
			}
		}

		final ArrowConfiguration arrowConfiguration = message.getArrowConfiguration();
		final MessageExoType type = message.getType();
		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_LEFT) {
			x1 += ComponentRoseArrow.diamCircle / 2 + 2;
		}
		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_LEFT) {
			x1 += ComponentRoseArrow.diamCircle / 2 + 2;
		}
		if (arrowConfiguration.getDecoration2() == ArrowDecoration.CIRCLE && type == MessageExoType.TO_RIGHT) {
			x2 -= ComponentRoseArrow.diamCircle / 2 + 2;
		}
		if (arrowConfiguration.getDecoration1() == ArrowDecoration.CIRCLE && type == MessageExoType.FROM_RIGHT) {
			x2 -= ComponentRoseArrow.diamCircle / 2 + 2;
		}

		final Area area = new Area(x2 - x1, dim.getHeight());
		ug = ug.apply(new UTranslate(x1, 0));
		comp.drawU(ug, area, (Context2D) ug);
	}

	private boolean isShortArrow() {
		return message.isShortArrow();
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getHeight();
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getWidth();
	}

	public void addConstraints(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();

		if (message.getType().isRightBorder()) {

		} else {
			livingSpace.getPosC(stringBounder).ensureBiggerThan(tileArguments.getOrigin().addFixed(width));
		}

		// final Real point1 = getPoint1(stringBounder);
		// if (message.getType().isRightBorder()) {
		// final Real point2 = point1.addFixed(width);
		// } else {
		// final Real point2 = getPoint2(stringBounder);
		// if (point1.getCurrentValue() < point2.getCurrentValue()) {
		// point2.ensureBiggerThan(point1.addFixed(width));
		// } else {
		// point1.ensureBiggerThan(point2.addFixed(width));
		// }
		// }
	}

	public void updateStairs(StringBounder stringBounder, double y) {
		final ArrowComponent comp = (ArrowComponent) getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double arrowY = comp.getStartPoint(stringBounder, dim).getY();

		livingSpace.addStepForLivebox(getEvent(), y + arrowY);

	}

	private Real getPoint1(final StringBounder stringBounder) {
		if (message.getType().isRightBorder()) {
			return livingSpace.getPosC(stringBounder);
		}
		return tileArguments.getOrigin();
	}

	private double getPoint1Value(final StringBounder stringBounder) {
		if (message.getType().isRightBorder()) {
			return livingSpace.getPosC(stringBounder).getCurrentValue();
		}
		if (isShortArrow()) {
			return getPoint2Value(stringBounder) - getPreferredWidth(stringBounder);
		}
		return tileArguments.getBorder1();
	}

	private double getPoint2Value(final StringBounder stringBounder) {
		if (message.getType().isRightBorder()) {
			if (isShortArrow()) {
				return getPoint1Value(stringBounder) + getPreferredWidth(stringBounder);
			}
			return tileArguments.getBorder2();
		}
		return livingSpace.getPosC(stringBounder).getCurrentValue();
	}

	public Real getMinX(StringBounder stringBounder) {
		return getPoint1(stringBounder);
	}

	public Real getMaxX(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();
		return getPoint1(stringBounder).addFixed(width);
	}

}
