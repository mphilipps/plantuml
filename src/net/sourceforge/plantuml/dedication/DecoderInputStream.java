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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class DecoderInputStream extends FilterInputStream {

	private final byte key[];
	private int idx;
	private final Random rnd;

	public DecoderInputStream(InputStream source, byte key[]) {
		super(source);
		this.key = key;
		this.rnd = new Random(getSeed());
	}

	private long getSeed() {
		long result = 17;
		for (byte b : key) {
			result = result * 37 + b;
		}
		return result;
	}

	private byte getNextByte() {
		for (int i = 0; i < nextKey(); i++) {
			rnd.nextInt();
		}
		return (byte) rnd.nextInt();
	}

	private int nextKey() {
		final int result = key[idx];
		idx++;
		if (idx >= key.length) {
			idx = 0;
		}
		if (result < 0) {
			return result + 256;
		}
		return result;
	}

	@Override
	public int read() throws IOException {
		int b = super.read();
		if (b == -1) {
			return -1;
		}
		b = b ^ getNextByte();
		return b;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int nb = super.read(b, off, len);
		if (nb == -1) {
			return nb;
		}
		for (int i = 0; i < nb; i++) {
			b[i + off] = (byte) (b[i + off] ^ getNextByte());
		}
		return nb;
	}

}
