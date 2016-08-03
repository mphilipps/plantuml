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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class MainTile implements Tile, Bordered {

	private final double startingY = 8;
	private final Real min;
	private final Real max;
	private final boolean isShowFootbox;

	private final List<Tile> tiles = new ArrayList<Tile>();
	private final LivingSpaces livingSpaces;

	public MainTile(SequenceDiagram diagram, Englobers englobers, TileArguments tileArguments) {

		this.livingSpaces = tileArguments.getLivingSpaces();

		final List<Real> min2 = new ArrayList<Real>();
		final List<Real> max2 = new ArrayList<Real>();

		min2.add(tileArguments.getOrigin());
		max2.add(tileArguments.getOrigin());

		if (englobers.size() > 0) {
			min2.add(englobers.getMinX(tileArguments.getStringBounder()));
			max2.add(englobers.getMaxX(tileArguments.getStringBounder()));
		}

		// tiles.add(new EmptyTile(8, tileArguments));

		tiles.addAll(TileBuilder.buildSeveral(diagram.events().iterator(), tileArguments, null));

		for (Tile tile : tiles) {
			min2.add(tile.getMinX(tileArguments.getStringBounder()));
			max2.add(tile.getMaxX(tileArguments.getStringBounder()));
		}

		for (LivingSpace livingSpace : livingSpaces.values()) {
			max2.add(livingSpace.getPosD(tileArguments.getStringBounder()));
			max2.add(livingSpace.getPosC2(tileArguments.getStringBounder()));
		}

		this.min = RealUtils.min(min2);
		this.max = RealUtils.max(max2);

		this.isShowFootbox = diagram.isShowFootbox();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final LiveBoxFinder liveBoxFinder = new LiveBoxFinder(stringBounder);

		drawUInternal(liveBoxFinder, false);
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, true);
		drawUInternal(interceptor, false);
	}

	public void drawForeground(UGraphic ug) {
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, false);
		drawUInternal(interceptor, false);
	}

	private double drawUInternal(UGraphic ug, boolean trace) {
		final StringBounder stringBounder = ug.getStringBounder();
		final List<YPositionedTile> positionedTiles = new ArrayList<YPositionedTile>();
		final double y = GroupingTile.fillPositionelTiles(stringBounder, startingY, tiles, positionedTiles);
		for (YPositionedTile tile : positionedTiles) {
			tile.drawU(ug);
		}
		// System.err.println("MainTile::drawUInternal finalY=" + y);
		return y;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(limitFinder, false);
		final double finalY = drawUInternal(interceptor, false);
		final double result = Math.max(limitFinder.getMinMax().getDimension().getHeight(), finalY) + 10;
		// System.err.println("MainTile::getPreferredHeight=" + result);
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		for (Tile tile : tiles) {
			tile.addConstraints(stringBounder);
		}
	}

	public Real getMinX(StringBounder stringBounder) {
		return min;
	}

	public Real getMaxX(StringBounder stringBounder) {
		return max;
	}

	public Event getEvent() {
		return null;
	}

	public boolean isShowFootbox() {
		return isShowFootbox;
	}

	public LivingSpaces getLivingSpaces() {
		return livingSpaces;
	}

	public double getBorder1() {
		return min.getCurrentValue();
	}

	public double getBorder2() {
		return max.getCurrentValue();
	}

}
