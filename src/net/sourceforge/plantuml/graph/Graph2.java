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
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.StringBounderUtils;

public class Graph2 {

	private final Elastane elastane;
	private int widthCell;
	private int heightCell;

	public Graph2(Board board) {
		board.normalize();

		for (ANode n : board.getNodes()) {
			final Dimension2D dim = images(n).getDimension(StringBounderUtils.asStringBounder());
			widthCell = Math.max(widthCell, (int) dim.getWidth());
			heightCell = Math.max(heightCell, (int) dim.getHeight());
		}
		final Galaxy4 galaxy = new Galaxy4(board, widthCell, heightCell);
		elastane = new Elastane(galaxy);

		for (ANode n : board.getNodes()) {
			final Dimension2D dim = images(n).getDimension(StringBounderUtils.asStringBounder());
			elastane.addBox(n, (int) dim.getWidth(), (int) dim.getHeight());
		}

		final List<ALink> links = new ArrayList<ALink>(board.getLinks());
		Collections.sort(links, board.getLinkComparator());
		for (ALink link : links) {
			galaxy.addLink(link);
		}

		elastane.init();

	}

	private AbstractEntityImage images(ANode n) {
		return new EntityImageFactory().createEntityImage((IEntity)n.getUserData());
	}

	public Dimension2D getDimension() {
		return elastane.getDimension();

	}

	public void draw(final Graphics2D g2d) {
		elastane.draw(g2d);
	}

}
