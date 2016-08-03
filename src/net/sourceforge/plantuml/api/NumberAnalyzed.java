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

public class NumberAnalyzed implements INumberAnalyzed {

	private int nb;
	private int sum;
	private int min;
	private int max;

	public NumberAnalyzed() {

	}

	private NumberAnalyzed(int nb, int sum, int min, int max) {
		this.nb = nb;
		this.sum = sum;
		this.min = min;
		this.max = max;
	}

	public synchronized INumberAnalyzed getCopyImmutable() {
		final NumberAnalyzed copy = new NumberAnalyzed(nb, sum, min, max);
		return copy;
	}

	public synchronized void addValue(int v) {
		nb++;
		if (nb == 1) {
			sum = v;
			min = v;
			max = v;
			return;
		}
		sum += v;
		if (v > max) {
			max = v;
		}
		if (v < min) {
			min = v;
		}
	}

	synchronized public final int getNb() {
		return nb;
	}

	synchronized public final int getSum() {
		return sum;
	}

	synchronized public final int getMin() {
		return min;
	}

	synchronized public final int getMax() {
		return max;
	}

	synchronized public final int getMean() {
		if (nb == 0) {
			return 0;
		}
		return sum / nb;
	}

}
