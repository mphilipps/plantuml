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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public abstract class AbstractComponent implements Component {

	final protected void stroke(Graphics2D g2d, float dash, float thickness) {
		final float[] style = { dash, dash };
		g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, style, 0));
	}

	final protected UGraphic stroke(UGraphic ug, double dashVisible, double dashSpace, double thickness) {
		return ug.apply(new UStroke(dashVisible, dashSpace, thickness));
	}

	final protected void stroke(Graphics2D g2d, float dash) {
		stroke(g2d, dash, 1);
	}

	final protected UGraphic stroke(UGraphic ug, double dashVisible, double dashSpace) {
		return stroke(ug, dashVisible, dashSpace, 1);
	}

	abstract protected void drawInternalU(UGraphic ug, Area area);

	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
	}

	public final void drawU(UGraphic ug, Area area, Context2D context) {
		ug = ug.apply(new UTranslate(getPaddingX(), getPaddingY()));
		if (context.isBackground()) {
			drawBackgroundInternalU(ug, area);
		} else {
			drawInternalU(ug, area);
		}
	}

	public double getPaddingX() {
		return 0;
	}

	public double getPaddingY() {
		return 0;
	}

	public abstract double getPreferredWidth(StringBounder stringBounder);

	public abstract double getPreferredHeight(StringBounder stringBounder);

	public final Dimension2D getPreferredDimension(StringBounder stringBounder) {
		final double w = getPreferredWidth(stringBounder);
		final double h = getPreferredHeight(stringBounder);
		return new Dimension2DDouble(w, h);
	}

}
