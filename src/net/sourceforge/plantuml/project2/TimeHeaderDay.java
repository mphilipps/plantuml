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
package net.sourceforge.plantuml.project2;

import java.awt.Font;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimeHeaderDay extends AbstractTextBlock implements TextBlock {

	private final Day start;
	private final Day end;
	private final TimeLine timeline;
	private final double dayWidth;

	private final UFont font = new UFont("Serif", Font.PLAIN, 9);
	private final FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(font);

	public TimeHeaderDay(Day start, Day end, TimeLine timeline, double dayWidth) {
		this.start = start;
		this.end = end;
		this.timeline = timeline;
		this.dayWidth = dayWidth;
	}

	public void drawU(UGraphic ug) {
		int n = 0;
		for (Day d = start; d.compareTo(end) <= 0; d = (Day) timeline.next(d)) {
			final String text = "" + d.getNumDay();
			final TextBlock b = Display.create(text).create(fontConfig, HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final Dimension2D dimText = b.calculateDimension(ug.getStringBounder());
			final double diffX = dayWidth - dimText.getWidth();
			final double diffY = getHeight() - dimText.getHeight();
			ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
			ug = ug.apply(new UChangeBackColor(HtmlColorUtils.WHITE));
			ug.apply(new UTranslate(n * dayWidth, 0)).draw(new URectangle(dayWidth, getHeight()));
			b.drawU(ug.apply(new UTranslate((n * dayWidth + diffX / 2), (diffY / 2))));
			n++;
		}
	}

	private double getHeight() {
		return 20;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		int n = 0;
		for (Day d = start; d.compareTo(end) <= 0; d = (Day) timeline.next(d)) {
			n++;
		}
		return new Dimension2DDouble(n * dayWidth, getHeight());
	}
}
