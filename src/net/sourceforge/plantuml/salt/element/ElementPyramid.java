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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.salt.Cell;
import net.sourceforge.plantuml.salt.Positionner2;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementPyramid extends AbstractElement {

	private int rows;
	private int cols;
	private final TableStrategy tableStrategy;
	private final Map<Element, Cell> positions1;
	private final Map<Cell, Element> positions2 = new HashMap<Cell, Element>();

	private double rowsStart[];
	private double colsStart[];

	public ElementPyramid(Positionner2 positionner, TableStrategy tableStrategy) {
		positions1 = positionner.getAll();
		for (Map.Entry<Element, Cell> ent : positions1.entrySet()) {
			positions2.put(ent.getValue(), ent.getKey());
		}

		this.rows = positionner.getNbRows();
		this.cols = positionner.getNbCols();
		this.tableStrategy = tableStrategy;

		for (Cell c : positions1.values()) {
			rows = Math.max(rows, c.getMaxRow());
			cols = Math.max(cols, c.getMaxCol());
		}

	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		init(stringBounder);
		return new Dimension2DDouble(colsStart[colsStart.length - 1], rowsStart[rowsStart.length - 1]);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		init(ug.getStringBounder());
		final Grid grid = new Grid(rowsStart, colsStart, tableStrategy);
		for (Map.Entry<Element, Cell> ent : positions1.entrySet()) {
			final Element elt = ent.getKey();
			final Cell cell = ent.getValue();
			final double xcell = colsStart[cell.getMinCol()];
			final double ycell = rowsStart[cell.getMinRow()];
			final double width = colsStart[cell.getMaxCol() + 1] - colsStart[cell.getMinCol()] - 1;
			final double height = rowsStart[cell.getMaxRow() + 1] - rowsStart[cell.getMinRow()] - 1;
			grid.addCell(cell);
			elt.drawU(ug.apply(new UTranslate(xcell + 1, ycell + 1)), zIndex, new Dimension2DDouble(width, height));
		}
		if (zIndex == 0) {
			grid.drawU(ug, 0, 0);
		}
	}

	private void init(StringBounder stringBounder) {
		if (rowsStart != null) {
			return;
		}
		rowsStart = new double[rows + 1];
		colsStart = new double[cols + 1];
		final List<Cell> all = new ArrayList<Cell>(positions1.values());
		Collections.sort(all, new LeftFirst());
		for (Cell cell : all) {
			final Element elt = positions2.get(cell);
			final Dimension2D dim = elt.getPreferredDimension(stringBounder, 0, 0);
			ensureColWidth(cell.getMinCol(), cell.getMaxCol() + 1, dim.getWidth() + 2);
		}
		Collections.sort(all, new TopFirst());
		for (Cell cell : all) {
			final Element elt = positions2.get(cell);
			final Dimension2D dim = elt.getPreferredDimension(stringBounder, 0, 0);
			ensureRowHeight(cell.getMinRow(), cell.getMaxRow() + 1, dim.getHeight() + 2);
		}
	}

	private void ensureColWidth(int first, int last, double width) {
		final double actual = colsStart[last] - colsStart[first];
		final double missing = width - actual;
		if (missing > 0) {
			for (int i = last; i < colsStart.length; i++) {
				colsStart[i] += missing;
			}
		}
	}

	private void ensureRowHeight(int first, int last, double height) {
		final double actual = rowsStart[last] - rowsStart[first];
		final double missing = height - actual;
		if (missing > 0) {
			for (int i = last; i < rowsStart.length; i++) {
				rowsStart[i] += missing;
			}
		}
	}

	public final int getNbRows() {
		return rows + 1;
	}

	public final int getNbCols() {
		return cols + 1;
	}

}
