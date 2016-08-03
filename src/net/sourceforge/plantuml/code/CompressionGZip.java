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
package net.sourceforge.plantuml.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionGZip implements Compression {

	class MyGZIPOutputStream extends GZIPOutputStream {

		public MyGZIPOutputStream(OutputStream baos) throws IOException {
			super(baos);
			def.setLevel(9);
		}

	}

	public byte[] compress(byte[] in) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			final GZIPOutputStream gz = new MyGZIPOutputStream(baos);
			gz.write(in);
			gz.close();
			baos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e.toString());
		}
	}

	public byte[] decompress(byte[] in) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final ByteArrayInputStream bais = new ByteArrayInputStream(in);
		final GZIPInputStream gz = new GZIPInputStream(bais);
		int read;
		while ((read = gz.read()) != -1) {
			baos.write(read);
		}
		gz.close();
		bais.close();
		baos.close();
		return baos.toByteArray();
	}

}
