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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class USymbolFolder extends USymbol {

	private final static int marginTitleX1 = 3;
	private final static int marginTitleX2 = 3;
	private final static int marginTitleX3 = 7;
	private final static int marginTitleY0 = 0;
	private final static int marginTitleY1 = 3;
	private final static int marginTitleY2 = 3;

	private final SkinParameter skinParameter;

	public USymbolFolder(SkinParameter skinParameter) {
		this.skinParameter = skinParameter;
	}

	@Override
	public SkinParameter getSkinParameter() {
		return skinParameter;
	}

	private void drawFolder(UGraphic ug, double width, double height, Dimension2D dimTitle, boolean shadowing) {

		final double wtitle;
		if (dimTitle.getWidth() == 0) {
			wtitle = Math.max(30, width / 4);
		} else {
			wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		}
		final double htitle = getHTitle(dimTitle);

		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 0);
		shape.addPoint(wtitle, 0);

		shape.addPoint(wtitle + marginTitleX3, htitle);
		shape.addPoint(width, htitle);
		shape.addPoint(width, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 0);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(shape);
		ug.apply(new UTranslate(0, htitle)).draw(new ULine(wtitle + marginTitleX3, 0));
	}

	private double getHTitle(Dimension2D dimTitle) {
		final double htitle;
		if (dimTitle.getWidth() == 0) {
			htitle = 10;
		} else {
			htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		}
		return htitle;
	}

	private Margin getMargin() {
		return new Margin(10, 10 + 10, 10 + 3, 10);
	}

	public TextBlock asSmall(final TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				final Dimension2D dimName = name.calculateDimension(ug.getStringBounder());
				drawFolder(ug, dim.getWidth(), dim.getHeight(), dimName, symbolContext.isShadowing());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				name.drawU(ug.apply(new UTranslate(4, 3)));
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1() + dimName.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimName = name.calculateDimension(stringBounder);
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimName, dimStereo, dimLabel));
			}
		};
	}

	public TextBlock asBig(final TextBlock title, final TextBlock stereotype, final double width, final double height,
			final SymbolContext symbolContext) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dim = calculateDimension(stringBounder);
				ug = symbolContext.apply(ug);
				final Dimension2D dimTitle = title.calculateDimension(stringBounder);
				drawFolder(ug, dim.getWidth(), dim.getHeight(), dimTitle, symbolContext.isShadowing());
				title.drawU(ug.apply(new UTranslate(4, 2)));
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final double posStereo = (width - dimStereo.getWidth()) / 2;

				stereotype.drawU(ug.apply(new UTranslate(4 + posStereo, 2 + getHTitle(dimTitle))));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}

		};
	}

}
