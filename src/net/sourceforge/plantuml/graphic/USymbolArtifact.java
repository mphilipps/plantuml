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
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolArtifact extends USymbol {

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.ARTIFACT;
	}

	private void drawArtifact(UGraphic ug, double widthTotal, double heightTotal, boolean shadowing) {

		final URectangle form = new URectangle(widthTotal, heightTotal);
		if (shadowing) {
			form.setDeltaShadow(4);
		}

		ug.draw(form);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		final double heightSymbol = 14;
		polygon.addPoint(0, heightSymbol);
		final double widthSymbol = 12;
		polygon.addPoint(widthSymbol, heightSymbol);
		final int cornersize = 6;
		polygon.addPoint(widthSymbol, cornersize);
		polygon.addPoint(widthSymbol - cornersize, 0);
		polygon.addPoint(0, 0);
		// if (shadowing) {
		// polygon.setDeltaShadow(3.0);
		// }

		final double xSymbol = widthTotal - widthSymbol - 5;
		final double ySymbol = 5;

		ug.apply(new UTranslate(xSymbol, ySymbol)).draw(polygon);
		ug.apply(new UTranslate(xSymbol + widthSymbol - cornersize, ySymbol)).draw(new ULine(0, cornersize));
		ug.apply(new UTranslate(xSymbol + widthSymbol, ySymbol + cornersize)).draw(new ULine(-cornersize, 0));

	}

	private Margin getMargin() {
		return new Margin(10, 10 + 10, 10 + 3, 10);
	}
	
	public boolean manageHorizontalLine() {
		return true;
	}


	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				final Stencil stencil = new Stencil() {
					public double getStartingX(StringBounder stringBounder, double y) {
						return 0;
					}

					public double getEndingX(StringBounder stringBounder, double y) {
						return dim.getWidth();
					}
				};
				ug = new UGraphicStencil(ug, stencil, new UStroke());
				ug = symbolContext.apply(ug);
				drawArtifact(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	public TextBlock asBig(final TextBlock title, final TextBlock stereotype, final double width, final double height,
			final SymbolContext symbolContext) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawArtifact(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug.apply(new UTranslate(posStereo, 2)));
				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 2 + dimStereo.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

}
