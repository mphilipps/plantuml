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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Log;

public class SvekUtils {

	static public void traceSvgString(String svg) throws IOException {
		traceString(new File("svek.svg"), svg);
	}

	static public void traceDotString(String dot) throws IOException {
		traceString(new File("svek.dot"), dot);
	}

	static private void traceString(final File f, String svg) throws IOException {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(f));
			pw.print(svg);
			Log.info("Creating file " + f);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	static class PointListIterator implements Iterator<List<Point2D.Double>> {

		private final String text;
		private final double yDelta;
		private int pos = 0;

		public PointListIterator(String text, double yDelta) {
			this.text = text;
			this.yDelta = yDelta;
		}

		public boolean hasNext() {
			return true;
		}

		public List<Point2D.Double> next() {
			final List<Point2D.Double> result = extractPointsList(text, pos, yDelta);
			pos = text.indexOf(pointsString, pos) + pointsString.length() + 1;
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	final private static String pointsString = "points=\"";

	public static List<Point2D.Double> extractPointsList(final String svg, final int starting, double yDelta) {
		final int p2 = svg.indexOf(pointsString, starting);
		final int p3 = svg.indexOf("\"", p2 + pointsString.length());
		final String points = svg.substring(p2 + pointsString.length(), p3);
		final List<Point2D.Double> pointsList = getPoints(points, yDelta);
		return pointsList;
	}

	public static List<Point2D.Double> extractD(final String svg, final int starting, double yDelta) {
		final int p2 = svg.indexOf("d=\"", starting);
		final int p3 = svg.indexOf("\"", p2 + "d=\"".length());
		final String points = svg.substring(p2 + "d=\"".length(), p3);
		final List<Point2D.Double> pointsList = getPoints(points, yDelta);
		return pointsList;
	}

	static public double getValue(String svg, int starting, String varName) {
		final String varNameString = varName + "=\"";
		int p1 = svg.indexOf(varNameString, starting);
		if (p1 == -1) {
			throw new IllegalStateException();
		}
		p1 += varNameString.length();
		final int p2 = svg.indexOf('\"', p1);
		return Double.parseDouble(svg.substring(p1, p2));

	}

	public static double getMaxX(List<Point2D.Double> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x > result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	public static double getMinX(List<Point2D.Double> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x < result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	public static Point2D.Double getMinXY(List<Point2D.Double> points) {
		return new Point2D.Double(getMinX(points), getMinY(points));
	}

	public static double getMaxY(List<Point2D.Double> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y > result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	public static double getMinY(List<Point2D.Double> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y < result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	static private List<Point2D.Double> getPoints(String points, double yDelta) {
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		final StringTokenizer st = new StringTokenizer(points, " MC");
		while (st.hasMoreTokens()) {
			final String t = st.nextToken();
			final StringTokenizer st2 = new StringTokenizer(t, ",");
			final double x = Double.parseDouble(st2.nextToken());
			final double y = Double.parseDouble(st2.nextToken()) + yDelta;
			result.add(new Point2D.Double(x, y));
		}
		return result;
	}

	public static void println(StringBuilder sb) {
		sb.append('\n');
	}

	public static String pixelToInches(double pixel) {
		final double v = pixel / 72.0;
		return String.format(Locale.US, "%6.6f", v);
	}

}
