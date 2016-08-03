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

package smetana.core;

import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarStar;

public class size_t_array_of_something implements size_t {

	final private int nb;
	final private Class cl;

	public size_t_array_of_something(Class cl, int nb) {
		this.nb = nb;
		this.cl = cl;
	}

	public boolean isZero() {
		return nb == 0;
	}

	public __ptr__ malloc() {
		return new StarArrayOfPtr(__array_of_ptr__.malloc_allocated(cl, nb));
	}

	public size_t negate() {
		throw new UnsupportedOperationException();
	}

	public size_t plus(int length) {
		throw new UnsupportedOperationException();
	}

	public boolean isStrictPositive() {
		throw new UnsupportedOperationException();
	}

	public boolean isStrictNegative() {
		throw new UnsupportedOperationException();
	}

	public __ptr__ realloc(Object old) {
		if (old instanceof StarStar) {
			((StarStar) old).realloc(nb);
			return (__ptr__) old;
		}
		((StarArrayOfPtr) old).realloc(nb);
		return (__ptr__) old;
	}

	public int getNb() {
		return nb;
	}

}
