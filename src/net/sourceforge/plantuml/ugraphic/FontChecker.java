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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svg.SvgGraphics;

public class FontChecker {

	final private UFont font;
	private static final Set<String> SQUARRE = new HashSet<String>(Arrays.asList("MI=I=XM=I=IX",
			"MI=I=XM=I=IXMI=I=XM=I=IX"));

	public FontChecker(UFont font) {
		this.font = font;
	}

	public boolean isCharOk(char c) {
		return SQUARRE.contains(getCharDesc(c)) == false;
	}

	static private String getType(int type, double oldX, double oldY, double x, double y) {
		if (type == PathIterator.SEG_CLOSE) {
			return "X";
		}
		if (type == PathIterator.SEG_LINETO) {
			if (oldX == x) {
				return "I";
			}
			if (oldY == y) {
				return "=";
			}
			return "L";
		}
		if (type == PathIterator.SEG_MOVETO) {
			return "M";
		}
		if (type == PathIterator.SEG_QUADTO) {
			return "Q";
		}
		if (type == PathIterator.SEG_CUBICTO) {
			return "C";
		}
		throw new IllegalArgumentException();
	}

	public String getCharDesc(char c) {
		final TextLayout t = new TextLayout("" + c, font.getFont(), TextBlockUtils.getFontRenderContext());
		final Shape sh = t.getOutline(null);
		final double current[] = new double[6];
		final PathIterator it = sh.getPathIterator(null);
		int sum = 0;
		final StringBuilder result = new StringBuilder();
		while (it.isDone() == false) {
			final double oldX = current[0];
			final double oldY = current[1];
			final int nb = it.currentSegment(current);
			sum += nb;
			result.append(getType(nb, oldX, oldY, current[0], current[1]));
			it.next();
		}
		return result.toString();
	}

	public String getCharDescVerbose(char c) {
		final TextLayout t = new TextLayout("" + c, font.getFont(), TextBlockUtils.getFontRenderContext());
		final Shape sh = t.getOutline(null);
		final double current[] = new double[6];
		final PathIterator it = sh.getPathIterator(null);
		int sum = 0;
		final StringBuilder result = new StringBuilder();
		while (it.isDone() == false) {
			final double oldX = current[0];
			final double oldY = current[1];
			final int nb = it.currentSegment(current);
			sum += nb;
			result.append(getType(nb, oldX, oldY, current[0], current[1]));
			appendValue(result, current);
			it.next();
		}
		return result.toString();
	}

	private void appendValue(StringBuilder result, double[] current) {
		for (double v : current) {
			final int i = (int) (v * 100);
			result.append(i);
			result.append(":");
		}

	}

	public void printChar(final PrintWriter pw, char c) throws IOException, TransformerException {
		pw.println("<p>");
		final int ascii = (int) c;
		pw.println(ascii + " - " + Integer.toHexString(ascii) + " - ");
		pw.println("&#" + ascii + ";");
		final String svg = getSvgImage(c);
		pw.println(svg);
	}

	private String getSvgImage(char c) throws IOException, TransformerException {
		final SvgGraphics svg = new SvgGraphics(1.0);
		svg.setStrokeColor("black");
		svg.svgImage(getBufferedImage(c), 0, 0);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		svg.createXml(os);
		os.close();
		return new String(os.toByteArray());
	}

	public BufferedImage getBufferedImage(final char c) throws IOException {
		assert c != '\t';
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1, null, null, null, 0, 0, null,
				false);
		final double dim = 20;
		imageBuilder.setUDrawable(new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
				ug.draw(new URectangle(dim - 1, dim - 1));
				if (ug instanceof UGraphic2) {
					ug = (UGraphic2) ug.apply(new UTranslate(dim / 3, 2 * dim / 3));
					final UText text = new UText("" + c, FontConfiguration.blackBlueTrue(font));
					ug.draw(text);
				}
			}
		});
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.PNG), os);
		os.close();
		return ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
	}

	// public BufferedImage getBufferedImageOld(char c) throws IOException {
	// final double dim = 20;
	// UGraphic2 ug = new FileFormatOption(FileFormat.PNG).createUGraphic(new Dimension2DDouble(dim, dim));
	// ug = (UGraphic2) ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
	// ug.draw(new URectangle(dim - 1, dim - 1));
	// ug = (UGraphic2) ug.apply(new UTranslate(dim / 3, 2 * dim / 3));
	// final UText text = new UText("" + c, new FontConfiguration(font, HtmlColorUtils.BLACK));
	// ug.draw(text);
	// final ByteArrayOutputStream os = new ByteArrayOutputStream();
	// ug.writeImageTOBEMOVED(os, null, 96);
	// os.close();
	// return ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
	// }

	public static void main(String[] args) throws IOException, TransformerException {

		final String name = args[0];
		final int size = Integer.parseInt(args[1]);
		final int v1 = Integer.parseInt(args[2]);
		final int v2 = Integer.parseInt(args[3]);
		final File f = new File("fontchecker-" + name + "-" + v1 + "-" + v2 + ".html");

		final FontChecker fc = new FontChecker(new UFont(name, Font.PLAIN, size));
		final PrintWriter pw = new PrintWriter(f);
		pw.println("<html>");
		pw.println("<h1>PROBLEM</h1>");
		for (int i = v1; i <= v2; i++) {
			final char c = (char) i;
			final boolean ok = fc.isCharOk(c);
			if (ok == false) {
				fc.printChar(pw, c);
				pw.println("</p>");
			}
		}
		pw.println("<h1>OK</h1>");
		final String allFontNames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = v1; i <= v2; i++) {
			final char c = (char) i;
			final boolean ok = fc.isCharOk(c);
			if (ok) {
				fc.printChar(pw, c);
				final String desc = fc.getCharDescVerbose(c);
				for (String n : allFontNames) {
					final FontChecker other = new FontChecker(new UFont(n, Font.PLAIN, size));
					final String descOther = other.getCharDescVerbose(c);
					if (desc.equals(descOther)) {
						pw.println("&nbsp;");
						pw.println(n);
					}
				}
				pw.println("</p>");

			}
		}
		pw.println("</html>");
		pw.close();

	}
}
