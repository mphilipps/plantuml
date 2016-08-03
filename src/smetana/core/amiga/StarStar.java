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

package smetana.core.amiga;

import smetana.core.UnsupportedC;
import smetana.core.__array_of_ptr__;
import smetana.core.__ptr__;

public class StarStar extends UnsupportedC implements Area {

	private Area area;

	StarStar(Area area) {
		this.area = area;
	}

	public static StarStar array_of_array_of_something_empty(final Class cl, int nb) {
		// if (allocated) {
		// return new StarStar(__array__.malloc(cl, nb));
		// }
		return new StarStar(__array_of_ptr__.malloc_empty(nb));
	}

	public String toString() {
		return "->" + area;
	}

	Area getArea() {
		return area;
	}

	public void swap(int i, int j) {
		((__array_of_ptr__) area).swap(i, j);

	}

	public void memcopyFrom(Area source) {
		StarStar other = (StarStar) source;
		this.area = other.area;
	}

	// public __ptr__ getBracket(int idx) {
	// return ((AreaArray) area).getBracket(idx);
	// }
	//
	public void realloc(int nb) {
		((__array_of_ptr__) area).realloc(nb);
	}

	//
	// // __c__
	// public void setBracket(int idx, Object data) {
	// ((AreaArray) area).setBracket(idx, data);
	// }
	//
	// public __ptr__ plus(int pointerMove) {
	// return ((AreaArray) area).plus(pointerMove);
	// }

	public __ptr__ plus(int pointerMove) {
		return new StarStar(((__array_of_ptr__) area).move(pointerMove));
	}

	public __ptr__ getPtr(String fieldName) {
		return ((__array_of_ptr__) area).asPtr().getPtr(fieldName);
	}

	public __ptr__ getPtr() {
		return (__ptr__) ((__array_of_ptr__) area).getInternal(0);
	}

	public void setPtr(__ptr__ value) {
		((__array_of_ptr__) area).setInternalByIndex(0, (Area) value);
	}

	public void setDouble(String fieldName, double data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int comparePointer(__ptr__ other) {
		return ((__array_of_ptr__) area).comparePointerInternal(((__array_of_ptr__) ((StarStar) other).area));
	}

}
