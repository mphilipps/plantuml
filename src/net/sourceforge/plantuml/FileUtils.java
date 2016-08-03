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
package net.sourceforge.plantuml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

// Used by the Eclipse Plugin, so do not change package location.
public class FileUtils {

	private static AtomicInteger counter;

	public static void resetCounter() {
		counter = new AtomicInteger(0);
	}

	static public File createTempFile(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false) {
			throw new IllegalArgumentException();
		}
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		final File f;
		if (counter == null) {
			f = File.createTempFile(prefix, suffix);
		} else {
			final String name = prefix + counter.addAndGet(1) + suffix;
			f = new File(name);
		}
		Log.info("Creating temporary file: " + f);
		if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
			f.deleteOnExit();
		}
		return f;
	}

	private static void copyInternal(final InputStream fis, final OutputStream fos) throws IOException {
		final byte[] buf = new byte[10240];
		int len;
		while ((len = fis.read(buf)) > 0) {
			fos.write(buf, 0, len);
		}
		fos.close();
		fis.close();
	}

	static public void copyToFile(File src, File dest) throws IOException {
		if (dest.isDirectory()) {
			dest = new File(dest, src.getName());
		}
		final InputStream fis = new BufferedInputStream(new FileInputStream(src));
		final OutputStream fos = new BufferedOutputStream(new FileOutputStream(dest));
		copyInternal(fis, fos);
	}

	static public void copyToStream(File src, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(new FileInputStream(src));
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToStream(InputStream is, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(is);
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToFile(byte[] src, File dest) throws IOException {
		final OutputStream fos = new BufferedOutputStream(new FileOutputStream(dest));
		fos.write(src);
		fos.close();
	}

}
