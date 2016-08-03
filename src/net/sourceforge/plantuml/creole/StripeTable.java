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
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class StripeTable implements Stripe {

	static enum Mode {
		HEADER, NORMAL
	};

	private FontConfiguration fontConfiguration;
	final private ISkinSimple skinParam;
	final private AtomTable table;
	final private Atom marged;
	final private StripeStyle stripeStyle = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	public StripeTable(FontConfiguration fontConfiguration, ISkinSimple skinParam, String line) {
		this.fontConfiguration = fontConfiguration;
		this.skinParam = skinParam;
		this.table = new AtomTable(fontConfiguration.getColor());
		this.marged = new AtomWithMargin(table, 2, 2);
		analyzeAndAddInternal(line, Mode.HEADER);
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom> singletonList(marged);
	}

	static Atom asAtom(List<StripeSimple> cells, double padding) {
		final Sheet sheet = new Sheet(HorizontalAlignment.LEFT);
		for (StripeSimple cell : cells) {
			sheet.add(cell);
		}
		return new SheetBlock1(sheet, 0, padding);
	}

	private HtmlColor getBackColor(String line) {
		if (CreoleParser.doesStartByColor(line)) {
			final int idx1 = line.indexOf('#');
			final int idx2 = line.indexOf('>');
			if (idx2 == -1) {
				throw new IllegalStateException();
			}
			final String color = line.substring(idx1, idx2);
			return skinParam.getIHtmlColorSet().getColorIfValid(color);
		}
		return null;
	}

	private String withouBackColor(String line) {
		final int idx2 = line.indexOf('>');
		if (idx2 == -1) {
			throw new IllegalStateException();
		}
		return line.substring(idx2 + 1);
	}

	private void analyzeAndAddInternal(String line, Mode mode) {
		HtmlColor lineBackColor = getBackColor(line);
		if (lineBackColor != null) {
			line = withouBackColor(line);
		}
		table.newLine(lineBackColor);
		for (final StringTokenizer st = new StringTokenizer(line, "|"); st.hasMoreTokens();) {
			String v = st.nextToken();
			HtmlColor cellBackColor = getBackColor(v);
			if (cellBackColor != null) {
				v = withouBackColor(v);
			}
			if (mode == Mode.HEADER && v.startsWith("=")) {
				v = v.substring(1);
			}
			final List<String> lines = getWithNewlinesInternal(v);
			final List<StripeSimple> cells = new ArrayList<StripeSimple>();
			for (String s : lines) {
				final StripeSimple cell = new StripeSimple(getFontConfiguration(mode), stripeStyle,
						new CreoleContext(), skinParam, CreoleMode.FULL);
				cell.analyzeAndAdd(s);
				cells.add(cell);
			}
			table.addCell(asAtom(cells, skinParam.getPadding()), cellBackColor);
		}
	}

	static List<String> getWithNewlinesInternal(String s) {
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == '\\') {
					current.append(c2);
				} else {
					current.append(c);
					current.append(c2);
				}
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return result;
	}

	private FontConfiguration getFontConfiguration(Mode mode) {
		if (mode == Mode.NORMAL) {
			return fontConfiguration;
		}
		return fontConfiguration.bold();
	}

	public void analyzeAndAddNormal(String line) {
		analyzeAndAddInternal(line, Mode.NORMAL);
	}

}
