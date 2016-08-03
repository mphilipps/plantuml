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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MainTileAdapter extends AbstractTextBlock implements TextBlock {

	private final MainTile mainTile;
	private Dimension2D cacheDimension;

	public MainTileAdapter(MainTile mainTile) {
		if (mainTile == null) {
			throw new IllegalArgumentException();
		}
		this.mainTile = mainTile;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (cacheDimension == null) {
			final double width = mainTile.getMaxX(stringBounder).getCurrentValue()
					- mainTile.getMinX(stringBounder).getCurrentValue();

			final int factor = mainTile.isShowFootbox() ? 2 : 1;
			final double height = mainTile.getPreferredHeight(stringBounder) + factor
					* mainTile.getLivingSpaces().getHeadHeight(stringBounder);

			cacheDimension = new Dimension2DDouble(width, height);
		}
		return cacheDimension;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		final double height = mainTile.getPreferredHeight(stringBounder);
		final LivingSpaces livingSpaces = mainTile.getLivingSpaces();

		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		mainTile.drawU(ug.apply(new UTranslate(0, headHeight)));
		livingSpaces.drawLifeLines(ug.apply(new UTranslate(0, headHeight)), height, context);
		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);
		if (mainTile.isShowFootbox()) {
			livingSpaces.drawHeads(ug.apply(new UTranslate(0, height + headHeight)), context, VerticalAlignment.TOP);
		}
		mainTile.drawForeground(ug.apply(new UTranslate(0, headHeight)));
	}

	public Real getMinX(StringBounder stringBounder) {
		return mainTile.getMinX(stringBounder);
	}

}
