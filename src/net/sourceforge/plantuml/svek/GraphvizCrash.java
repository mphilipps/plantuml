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

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.fun.IconLoader;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.QuoteUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.Version;

public class GraphvizCrash extends AbstractTextBlock implements IEntityImage {

	private static final UFont font = new UFont("SansSerif", Font.PLAIN, 12);

	private final GraphicStrings graphicStrings;
	private final BufferedImage flashCode;

	public GraphvizCrash(String text) {
		final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
		flashCode = utils.exportFlashcode(text);
		this.graphicStrings = new GraphicStrings(init(), font, HtmlColorUtils.BLACK, HtmlColorUtils.WHITE,
				UAntiAliasing.ANTI_ALIASING_ON, IconLoader.getRandom(), GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT);
	}

	private List<String> init() {
		final List<String> strings = new ArrayList<String>();
		strings.add("An error has occured!");
		final String quote = QuoteUtils.getSomeQuote();
		strings.add("<i>" + quote);
		strings.add(" ");
		strings.add("For some reason, dot/Graphviz has crashed.");
		strings.add("This has been generated with PlantUML (" + Version.versionString() + ").");
		strings.add(" ");
		addProperties(strings);
		strings.add(" ");
		try {
			final String dotVersion = GraphvizUtils.dotVersion();
			strings.add("Default dot version: " + dotVersion);
		} catch (Throwable e) {
			strings.add("Cannot determine dot version: " + e.toString());
		}
		strings.add(" ");
		strings.add("You should send this diagram and this image to <b>plantuml@gmail.com</b> or");
		strings.add("post to <b>http://plantuml.com/qa</b> to solve this issue.");
		strings.add("You can try to turn arround this issue by simplifing your diagram.");
		if (flashCode != null) {
			addDecodeHint(strings);
		}

		return strings;
	}

	public static void addDecodeHint(final List<String> strings) {
		strings.add(" ");
		strings.add(" Diagram source: (Use http://zxing.org/w/decode.jspx to decode the flashcode)");
	}

	public static void addProperties(final List<String> strings) {
		addTextProperty(strings, "os.version");
		addTextProperty(strings, "os.name");
		addTextProperty(strings, "java.vm.vendor");
		addTextProperty(strings, "java.vm.version");
		addTextProperty(strings, "java.version");
		addTextProperty(strings, "user.language");
	}

	private static void addTextProperty(final List<String> strings, String prop) {
		strings.add(prop + ": " + System.getProperty(prop));
	}

	public boolean isHidden() {
		return false;
	}

	public HtmlColor getBackcolor() {
		return HtmlColorUtils.WHITE;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D result = graphicStrings.calculateDimension(stringBounder);
		if (flashCode != null) {
			result = Dimension2DDouble.mergeTB(result,
					new Dimension2DDouble(flashCode.getWidth(), flashCode.getHeight()));
		}
		return result;
	}

	public void drawU(UGraphic ug) {
		graphicStrings.drawU(ug);
		if (flashCode != null) {
			final double h = graphicStrings.calculateDimension(ug.getStringBounder()).getHeight();
			ug = ug.apply(new UTranslate(0, h));
			ug.draw(new UImage(flashCode));
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
