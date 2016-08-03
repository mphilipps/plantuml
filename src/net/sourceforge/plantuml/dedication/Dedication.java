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
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.webp.VP8Decoder;

public class Dedication {

	private final String signature;

	public Dedication(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	public byte[] getKey(String keepLetter) {
		try {
			return keepLetter.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private InputStream getInputStream(String keepLetter) {
		final byte[] key = getKey(keepLetter);
		final InputStream tmp = PSystemDedication.class.getResourceAsStream(getSignature() + ".png");
		return new DecoderInputStream(tmp, key);
	}

	public BufferedImage getBufferedImage(String keepLetter) {
		try {
			final InputStream is = getInputStream(keepLetter);
			final ImageInputStream iis = ImageIO.createImageInputStream(is);
			final VP8Decoder vp8Decoder = new VP8Decoder();
			vp8Decoder.decodeFrame(iis, false);
			iis.close();
			return vp8Decoder.getFrame().getBufferedImage();
		} catch (Exception e) {
			return null;
		}
	}

}
