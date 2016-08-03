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
package net.sourceforge.plantuml.fun;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

public class IconLoader {

	private static final int NUMBER_OF_ICONS = 20;
	
	private final static Map<String, BufferedImage> all = new ConcurrentHashMap<String, BufferedImage>();

	public static BufferedImage getRandom() {
		// return addTransparent(getIcon("sprite013.png"));
		return addTransparent(getIcon(getSomeQuote()));
	}

	private static String getSomeQuote() {
		final int v = (int) (System.currentTimeMillis() / 1000L);
		final int n = v % NUMBER_OF_ICONS;
		return "sprite" + String.format("%03d", n) + ".png";
	}

	private static BufferedImage getIcon(String name) {
		BufferedImage result = all.get(name);
		if (result == null) {
			result = getIconSlow(name);
			if (result != null) {
				all.put(name, result);
			}
		}
		return result;
	}

	private static BufferedImage getIconSlow(String name) {
		try {
			final InputStream is = IconLoader.class.getResourceAsStream(name);
			if (is == null) {
				return null;
			}
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage addTransparent(BufferedImage ico) {
		if (ico == null) {
			return null;
		}
		final BufferedImage transparentIcon = new BufferedImage(ico.getWidth(), ico.getHeight(),
				BufferedImage.TYPE_INT_ARGB_PRE);
		for (int i = 0; i < ico.getWidth(); i++) {
			for (int j = 0; j < ico.getHeight(); j++) {
				final int col = ico.getRGB(i, j);
				if (col != ico.getRGB(0, 0)) {
					transparentIcon.setRGB(i, j, col);
				}
			}
		}
		return transparentIcon;
	}

}
