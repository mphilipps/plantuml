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
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.webp.Portrait;
import net.sourceforge.plantuml.webp.Portraits;

public class DateEventUtils {

	public static TextBlock addEvent(TextBlock textBlock, HtmlColor color) {
		final String today = new SimpleDateFormat("MM-dd").format(new Date());
		final String todayDayOfWeek = new SimpleDateFormat("MM-dd-u").format(new Date());

		if ("11-05".equals(today)) {
			final List<String> asList = Arrays.asList("<u>November 5th, 1955",
					"Doc Brown's discovery of the Flux Capacitor, that makes time-travel possible.");
			return TextBlockUtils.mergeTB(textBlock, getComment(asList, color), HorizontalAlignment.LEFT);
		} else if ("08-29".equals(today)) {
			final List<String> asList = Arrays.asList("<u>August 29th, 1997",
					"Skynet becomes self-aware at 02:14 AM Eastern Time.");
			return TextBlockUtils.mergeTB(textBlock, getComment(asList, color), HorizontalAlignment.LEFT);
		} else if ("06-29".equals(today)) {
			final List<String> asList = Arrays.asList("<u>June 29th, 1975",
					"\"It was the first time in history that anyone had typed",
					"a character on a keyboard and seen it show up on their",
					"own computer's screen right in front of them.\"", "\t\t\t\t\t\t\t\t\t\t<i>Steve Wozniak");
			return TextBlockUtils.mergeTB(textBlock, getComment(asList, color), HorizontalAlignment.LEFT);
		} else if ("01-07".equals(today) || "01-08-1".equals(todayDayOfWeek)) {
			return addCharlie(textBlock);
		} else if ("11-13".equals(today) || "11-14-1".equals(todayDayOfWeek)) {
			return addMemorial(textBlock, color);
		}
		return textBlock;
	}

	private static TextBlock addMemorial(TextBlock textBlock, HtmlColor color) {
		final Portrait portrait = Portraits.getOne();
		if (portrait == null) {
			return textBlock;
		}
		final BufferedImage im = portrait.getBufferedImage();
		if (im == null) {
			return textBlock;
		}

		final String name = portrait.getName();
		final String quote = portrait.getQuote();
		final String age = "" + portrait.getAge() + " years old";
		final UFont font = new UFont("SansSerif", Font.BOLD, 12);
		TextBlock comment = Display.create(name, age, quote).create(
				new FontConfiguration(font, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
		comment = TextBlockUtils.withMinWidth(TextBlockUtils.withMargin(comment, 4, 4), 800, HorizontalAlignment.LEFT);

		final TextBlock bottom0 = getComment(
				Arrays.asList("A thought for those who died in Paris the 13th November 2015."), color);
		final TextBlock bottom1 = new AbstractTextBlock() {
			private double margin = 10;

			public void drawU(UGraphic ug) {
				ug = ug.apply(new UTranslate(0, margin));
				ug.draw(new UImage(im));
				if (ug instanceof LimitFinder) {
					return;
				}
				Portraits.nextOne();
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(im.getWidth(), margin + im.getHeight());
			}
		};
		final TextBlock bottom = TextBlockUtils.mergeTB(bottom0,
				TextBlockUtils.mergeLR(bottom1, comment, VerticalAlignment.CENTER), HorizontalAlignment.LEFT);
		return TextBlockUtils.mergeTB(textBlock, bottom, HorizontalAlignment.LEFT);
	}

	private static TextBlock addCharlie(TextBlock textBlock) {
		final TextBlock charlie = new AbstractTextBlock() {
			private final BufferedImage charlie = PSystemVersion.getCharlieImage();

			public void drawU(UGraphic ug) {
				ug.draw(new UImage(charlie));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(charlie.getWidth(), charlie.getHeight());
			}
		};
		return TextBlockUtils.mergeTB(charlie, textBlock, HorizontalAlignment.LEFT);

	}

	public static TextBlock getComment(final List<String> asList, HtmlColor color) {
		final UFont font = new UFont("SansSerif", Font.BOLD, 14);
		TextBlock comment = Display.create(asList).create(
				new FontConfiguration(font, color, HtmlColorUtils.BLUE, true), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
		comment = TextBlockUtils.withMargin(comment, 4, 4);
		comment = new TextBlockBordered(comment, color);
		comment = TextBlockUtils.withMargin(comment, 10, 10);
		return comment;
	}
}
