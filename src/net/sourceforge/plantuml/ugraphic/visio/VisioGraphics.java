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
package net.sourceforge.plantuml.ugraphic.visio;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.golem.MinMaxDouble;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;

public class VisioGraphics {

	private final List<VisioShape> shapes = new ArrayList<VisioShape>();
	private final MinMaxDouble limits = new MinMaxDouble();

	public void createVsd(OutputStream os) throws IOException {
		final double width = toInches(limits.getMaxX());
		final double height = toInches(limits.getMaxY());

		out(os, "<?xml version='1.0' encoding='utf-8' ?>");
		out(os,
				"<VisioDocument xml:space='preserve' xmlns='http://schemas.microsoft.com/visio/2003/core' xmlns:vx='http://schemas.microsoft.com/visio/2006/extension' xmlns:v14='http://schemas.microsoft.com/office/visio/2010/extension'>");
		out(os, "<DocumentProperties>");
		out(os, "<Creator>PlantUML</Creator>");
		out(os, "</DocumentProperties>");
		out(os, "<DocumentSheet NameU='TheDoc' LineStyle='0' FillStyle='0' TextStyle='0'>");
		out(os, "</DocumentSheet>");
		out(os, "<Masters/>");
		out(os, "<Pages>");
		out(os, "<Page ID='0' NameU='Page-1' Name='Page 1' ViewScale='1' ViewCenterX='" + (width / 2)
				+ "' ViewCenterY='" + (height / 2) + "'>");
		out(os, "<PageSheet LineStyle='0' FillStyle='0' TextStyle='0'>");
		out(os, "<PageProps>");
		out(os, "<PageWidth Unit='IN_F'>" + width + "</PageWidth>");
		out(os, "<PageHeight Unit='IN_F'>" + height + "</PageHeight>");
		out(os, "<PageScale Unit='IN_F'>1</PageScale>");
		out(os, "<DrawingScale Unit='IN_F'>2</DrawingScale>"); // change for scale
		out(os, "<DrawingSizeType>3</DrawingSizeType>");
		out(os, "<DrawingScaleType>0</DrawingScaleType>");
		out(os, "<InhibitSnap>0</InhibitSnap>");
		out(os, "</PageProps>");
		out(os, "</PageSheet>");
		out(os, "<Shapes>");
		for (VisioShape sh : shapes) {
			sh.yReverse(height).print(os);
			// sh.print(os);
		}
		out(os, "</Shapes>");
		out(os, "</Page>");
		out(os, "</Pages>");
		out(os, "</VisioDocument>");
	}

	private void out(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
		os.write("\n".getBytes());
	}

	private double toInches(double val) {
		return val / 72.0;
	}

	private void ensureVisible(double x, double y) {
		limits.manage(x, y);
	}

	public void rectangle(double x, double y, double width, double height) {
		ensureVisible(x, y);
		ensureVisible(x + width, y + height);
		final VisioRectangle rect = VisioRectangle.createInches(shapes.size() + 1, x, y, width, height);
		shapes.add(rect);
	}

	public void text(String text, double x, double y, String family, int fontSize, double width, double height,
			Map<String, String> attributes) {
		// System.err.println("x=" + x);
		// System.err.println("y=" + y);
		// System.err.println("text=" + text);
		// System.err.println("family=" + family);
		// System.err.println("fontSize=" + fontSize);
		// System.err.println("width=" + width);
		// System.err.println("attributes=" + attributes);
		ensureVisible(x, y);
		final VisioText txt = VisioText.createInches(shapes.size() + 1, text, fontSize, x, y, width, height);
		shapes.add(txt);

	}

	public void line(double x1, double y1, double x2, double y2) {
		ensureVisible(x1, y1);
		if (x1 == x2 && y1 == y2) {
			return;
		}
		ensureVisible(x2, y2);
		final VisioLine line = VisioLine.createInches(shapes.size() + 1, x1, y1, x2, y2);
		shapes.add(line);
	}

	private void line(Point2D p1, Point2D p2) {
		line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public void upath(final double x, final double y, UPath path) {
		double lx = x;
		double ly = y;
		for (USegment seg : path) {
			final USegmentType type = seg.getSegmentType();
			final double coord[] = seg.getCoord();
			if (type == USegmentType.SEG_MOVETO) {
				lx = coord[0] + x;
				ly = coord[1] + y;
			} else if (type == USegmentType.SEG_LINETO) {
				line(lx, ly, coord[0] + x, coord[1] + y);
				lx = coord[0] + x;
				ly = coord[1] + y;
			} else if (type == USegmentType.SEG_QUADTO) {
				line(lx, ly, coord[2] + x, coord[3] + y);
				lx = coord[2] + x;
				ly = coord[3] + y;
			} else if (type == USegmentType.SEG_CUBICTO) {
				line(lx, ly, coord[4] + x, coord[5] + y);
				// linePoint(lx, ly, coord[0] + x, coord[1] + y);
				// linePoint(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y);
				// linePoint(coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
				lx = coord[4] + x;
				ly = coord[5] + y;
			} else if (type == USegmentType.SEG_CLOSE) {
				// Nothing
			} else {
				Log.println("unknown " + seg);
			}

		}

	}

	public void polygon(UPolygon poly) {
		Point2D last = null;
		for (Point2D pt : poly.getPoints()) {
			if (last != null) {
				line(last, pt);
			}
			last = pt;
		}
	}

}