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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GroupingGraphicalElementElse extends GroupingGraphicalElement implements InGroupable {

	private final Component compElse;
	private final Lazy afterY;
	private final boolean parallel;

	public GroupingGraphicalElementElse(double startingY, Component compElse, InGroupableList inGroupableList,
			boolean parallel, Lazy afterY) {
		super(startingY, inGroupableList);
		this.parallel = parallel;
		this.compElse = compElse;
		this.afterY = afterY;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = getInGroupableList().getMinX(stringBounder);
		final double x2 = getInGroupableList().getMaxX(stringBounder);
		ug = ug.apply(new UTranslate(x1, getStartingY()));

		final double height = afterY.getNow() - getStartingY();
		if (height <= 0) {
			return;
		}
		final Dimension2D dim = new Dimension2DDouble(x2 - x1, height);

		if (parallel == false) {
			compElse.drawU(ug, new Area(dim), context);
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		if (parallel) {
			return 0;
		}
		return compElse.getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return compElse.getPreferredWidth(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return getStartingX(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return getMinX(stringBounder) + getPreferredWidth(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

}
