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

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.code.Base64Coder;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.ImgValign;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;

public class AtomImg implements Atom {

	private static final String DATA_IMAGE_PNG_BASE64 = "data:image/png;base64,";
	private final BufferedImage image;
	private final double scale;

	private AtomImg(BufferedImage image, double scale) {
		this.image = image;
		this.scale = scale;
	}

	public static Atom create(String src, final ImgValign valign, final int vspace, final double scale) {
		final UFont font = new UFont("Monospaced", Font.PLAIN, 14);
		final FontConfiguration fc = FontConfiguration.blackBlueTrue(font);

		if (src.startsWith(DATA_IMAGE_PNG_BASE64)) {
			final String data = src.substring(DATA_IMAGE_PNG_BASE64.length(), src.length());
			try {
				final byte bytes[] = Base64Coder.decode(data);
				return build(src, fc, bytes, scale);
			} catch (Exception e) {
				return AtomText.create("ERROR " + e.toString(), fc);
			}

		}
		try {
			final File f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				// Check if valid URL
				if (src.startsWith("http:") || src.startsWith("https:")) {
					final byte image[] = getFile(src);
					return build(src, fc, image, scale);
				}
				return AtomText.create("(File not found: " + f + ")", fc);
			}
			if (f.getName().endsWith(".svg")) {
				// return new AtomImg(new TileImageSvg(f));
				throw new UnsupportedOperationException();
			}
			final BufferedImage read = ImageIO.read(f);
			if (read == null) {
				return AtomText.create("(Cannot decode: " + f + ")", fc);
			}
			return new AtomImg(ImageIO.read(f), scale);
		} catch (IOException e) {
			return AtomText.create("ERROR " + e.toString(), fc);
		}
	}

	private static Atom build(String source, final FontConfiguration fc, final byte[] data, double scale)
			throws IOException {
		final BufferedImage read = ImageIO.read(new ByteArrayInputStream(data));
		if (read == null) {
			return AtomText.create("(Cannot decode: " + source + ")", fc);
		}
		return new AtomImg(read, scale);
	}

	// Added by Alain Corbiere
	static byte[] getFile(String host) throws IOException {
		final ByteArrayOutputStream image = new ByteArrayOutputStream();
		InputStream input = null;
		try {
			final URL url = new URL(host);
			final URLConnection connection = url.openConnection();
			input = connection.getInputStream();
			final byte[] buffer = new byte[1024];
			int read;
			while ((read = input.read(buffer)) > 0) {
				image.write(buffer, 0, read);
			}
			image.close();
			return image.toByteArray();
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	// End

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(image.getWidth() * scale, image.getHeight() * scale);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		// final double h = calculateDimension(ug.getStringBounder()).getHeight();
		ug.draw(new UImage(image, scale));
		// tileImage.drawU(ug.apply(new UTranslate(0, -h)));
	}

}
