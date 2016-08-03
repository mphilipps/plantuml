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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graph2.IInflationTransform;
import net.sourceforge.plantuml.graph2.InflationTransform2;
import net.sourceforge.plantuml.graph2.Plan;
import net.sourceforge.plantuml.graph2.Polyline2;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;

public class Graph5 {

	private final int spaceWidth = 40;
	private final int spaceHeight = 40;

	private final double margin = 20;
	private final Board board;

	private int maxRow;
	private int maxCol;

	private final Plan plan = new Plan();
	private final IInflationTransform inflationTransform = new InflationTransform2();
	// private final IInflationTransform inflationTransform = new
	// IdentityInflationTransform();

	private AbstractEntityImage getImage(ANode n) {
		return new EntityImageFactory().createEntityImage((IEntity) n.getUserData());
	}

	public Graph5(Board board) {
		board.normalize();
		this.board = board;
		for (ANode n : board.getNodes()) {
			maxRow = Math.max(maxRow, n.getRow());
			maxCol = Math.max(maxCol, board.getCol(n));
			final Point2D.Double pos = getPosition(n);
			plan.addPoint2D(pos);
			// Log.println("n=" + n + " pos=" + pos);
		}
		for (ANode n : board.getNodes()) {
			final AbstractEntityImage image = getImage(n);
			final Point2D.Double pos = getPosition(n);
			final int widthCell = (int) image.getDimension(StringBounderUtils.asStringBounder())
					.getWidth() + 20;
			final int heightCell = (int) image.getDimension(StringBounderUtils.asStringBounder())
					.getHeight() + 20;
			inflationTransform.addInflationX(pos.getX(), widthCell);
			inflationTransform.addInflationY(pos.getY(), heightCell);
		}

		// Log.println("inflationTransform=" + inflationTransform);

	}

	public Point2D.Double getPosition(ANode node) {
		return new Point2D.Double(board.getCol(node) * spaceWidth, node.getRow() * spaceHeight);
	}

	public Dimension2D getDimension() {
		final double width = spaceWidth * maxCol;
		final int height = spaceWidth * maxRow;
		return new Dimension2DDouble(width + 2 * margin + inflationTransform.getTotalInflationX(), height + 2 * margin
				+ inflationTransform.getTotalInflationY());

	}

	public void draw(final Graphics2D g2d) {
		g2d.translate(margin, margin);
		g2d.setColor(Color.BLUE);

		for (ALink link : getSortedLinks()) {
			final Point2D start = getPosition(link.getNode1());
			final Point2D end = getPosition(link.getNode2());

			final List<Line2D.Double> lines = buildPath(start, end);
			final Polyline2 polyline = buildPolyline(start, end, lines);
			polyline.draw(g2d);
		}

		for (ANode n : board.getNodes()) {
			final AbstractEntityImage image = getImage(n);
			Point2D pos = getPosition(n);
			pos = inflationTransform.inflatePoint2D(pos);
			final double x = pos.getX() - image.getDimension(StringBounderUtils.asStringBounder()).getWidth() / 2;
			final double y = pos.getY() - image.getDimension(StringBounderUtils.asStringBounder()).getHeight() / 2;
			g2d.translate(x, y);
			image.draw(new ColorMapperIdentity(), g2d);
			g2d.translate(-x, -y);
		}

	}

	private Polyline2 buildPolyline(final Point2D start, final Point2D end, final List<Line2D.Double> lines) {
		final Polyline2 polyline = new Polyline2(inflationTransform.inflatePoint2D(start), inflationTransform
				.inflatePoint2D(end));
		final List<Line2D.Double> list = inflationTransform.inflate(lines);
		for (Line2D.Double l1 : list) {
			polyline.addLine(l1);
		}
		return polyline;
	}

	private List<Line2D.Double> buildPath(final Point2D start, final Point2D end) {
		Point2D current = start;
		final List<Point2D.Double> interm = plan.getIntermediatePoints(start, end);
		final List<Line2D.Double> lines = new ArrayList<Line2D.Double>();
		for (final Point2D.Double inter : interm) {
			plan.addPoint2D(inter);
			lines.add(new Line2D.Double(current, inter));
			plan.createLink(current, inter);
			current = inter;
		}
		lines.add(new Line2D.Double(current, end));
		plan.createLink(current, end);
		return lines;
	}

	private List<ALink> getSortedLinks() {
		final Map<ALink, Double> lengths = new LinkedHashMap<ALink, Double>();
		for (ALink link : board.getLinks()) {
			final Point2D.Double p1 = getPosition(link.getNode1());
			final Point2D.Double p2 = getPosition(link.getNode2());
			lengths.put(link, p1.distance(p2));
		}
		final List<ALink> all = new ArrayList<ALink>(lengths.keySet());
		Collections.sort(all, new Comparator<ALink>() {
			public int compare(ALink l1, ALink l2) {
				final double diff = lengths.get(l1) - lengths.get(l2);
				return (int) Math.signum(diff);
			}
		});
		return all;
	}

}
