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
package net.sourceforge.plantuml.api;

public final class MagicArray {

	private final int data[];
	private final int size;
	private long lastUpdatedKey = -1;
	private int lastUpdatedValue;
	private long sum;
	private long maxSum;

	public MagicArray(int size) {
		this.data = new int[size];
		this.size = size;
	}

	synchronized public void incKey(long key) {
		incKey(key, 1);
	}

	synchronized public void incKey(long key, int delta) {
		if (key < lastUpdatedKey) {
			return;
		}
		if (key != lastUpdatedKey) {
			if (lastUpdatedKey != -1) {
				setValue(lastUpdatedKey, lastUpdatedValue);
				for (long i = lastUpdatedKey + 1; i < key; i++) {
					setValue(i, 0);
				}
			}
			lastUpdatedValue = 0;
			lastUpdatedKey = key;
		}
		lastUpdatedValue += delta;
	}

	private void setValue(long key, int value) {
		final int i = (int) (key % size);
		sum += value - data[i];
		if (sum > maxSum) {
			maxSum = sum;
		}
		data[i] = value;
	}

	synchronized public long getSum() {
		return sum;
	}

	synchronized public long getMaxSum() {
		return maxSum;
	}

	private long getSumSlow() {
		long tmp = 0;
		for (int d : data) {
			tmp += d;
		}
		return tmp;
	}

}
