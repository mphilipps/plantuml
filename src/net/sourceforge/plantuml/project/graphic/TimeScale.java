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
package net.sourceforge.plantuml.project.graphic;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.Day;
import net.sourceforge.plantuml.project.Instant;
import net.sourceforge.plantuml.project.Month;
import net.sourceforge.plantuml.project.Project;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TimeScale {

	private final UFont font = new UFont("Serif", Font.PLAIN, 9);
	private final Project project;
	private final FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(font);

	public TimeScale(Project project) {
		this.project = project;
	}

	public void draw(UGraphic ug, final double x, double y) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double monthHeight = getMonthHeight(stringBounder);
		final double caseWidth = getCaseWidth(stringBounder);
		final double caseHeight = getCaseHeight(stringBounder);
		final int nb = getNbCase();

		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
		ug.apply(new UTranslate(x, y)).draw(new URectangle(nb * caseWidth, monthHeight));
		final Instant end = project.getEnd();

		Month printed = null;

		double curx = x;
		for (Instant cur = project.getStart(); cur.compareTo(end) <= 0; cur = cur.next(project.getDayClose())) {
			final Day d = cur.getDay();
			if (printed == null || d.getMonth() != printed) {
				ug.apply(new UTranslate(curx, y)).draw(new ULine(0, monthHeight));
				printed = d.getMonth();
				final TextBlock b = Display.create(printed.name()).create(fontConfig, HorizontalAlignment.LEFT,
						new SpriteContainerEmpty());
				final Dimension2D dim = b.calculateDimension(stringBounder);
				b.drawU(ug.apply(new UTranslate(curx, (y + (monthHeight - dim.getHeight()) / 2))));
			}
			curx += caseWidth;
		}

		curx = x;
		y += monthHeight;
		ug.apply(new UTranslate(x, y)).draw(new URectangle(nb * caseWidth, caseHeight));

		for (Instant cur = project.getStart(); cur.compareTo(end) <= 0; cur = cur.next(project.getDayClose())) {
			final Day d = cur.getDay();
			final TextBlock b = Display.create("" + d.getNumDay()).create(fontConfig, HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final Dimension2D dim = b.calculateDimension(stringBounder);
			b.drawU(ug.apply(new UTranslate((curx + (caseWidth - dim.getWidth()) / 2), (y + (caseHeight - dim
					.getHeight()) / 2))));
			curx += caseWidth;
			ug.apply(new UTranslate(curx, y)).draw(new ULine(0, caseHeight));
		}
	}

	public SortedMap<Instant, Double> getAbscisse(StringBounder stringBounder) {
		final SortedMap<Instant, Double> pos = new TreeMap<Instant, Double>();
		final double caseWidth = getCaseWidth(stringBounder);
		final Instant end = project.getEnd();
		double x = 0;
		for (Instant cur = project.getStart(); cur.compareTo(end) <= 0; cur = cur.next(project.getDayClose())) {
			pos.put(cur, x);
			x += caseWidth;
		}
		return Collections.unmodifiableSortedMap(pos);
	}

	private int getNbCase() {
		int result = 0;
		final Instant end = project.getEnd();
		for (Instant cur = project.getStart(); cur.compareTo(end) <= 0; cur = cur.next(project.getDayClose())) {
			result++;
		}
		return result;
	}

	private double getCaseWidth(StringBounder stringBounder) {
		final Dimension2D dim00 = stringBounder.calculateDimension(font, "00");
		return dim00.getWidth() + 3;
	}

	private double getCaseHeight(StringBounder stringBounder) {
		final Dimension2D dim00 = stringBounder.calculateDimension(font, "00");
		return dim00.getHeight() + 3;
	}

	private double getMonthHeight(StringBounder stringBounder) {
		final Dimension2D dimZZ = stringBounder.calculateDimension(font, "ZZ");
		return dimZZ.getHeight() + 3;
	}

	public double getWidth(StringBounder stringBounder) {
		return getCaseWidth(stringBounder) * getNbCase();
	}

	public double getHeight(StringBounder stringBounder) {
		return getCaseHeight(stringBounder) + getMonthHeight(stringBounder);
	}

}
