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

import smetana.core.amiga.Area;
import smetana.core.amiga.StarArrayOfPtr;
import smetana.core.amiga.StarArrayOfStruct;
import smetana.core.amiga.StarStruct;

public class __struct__<C extends __ptr__> implements __c__fields, Area {

	final private StarStruct data;

	public __struct__(Class<C> cl) {
		this.data = (StarStruct) Memory.malloc(cl);
	}

	@Override
	public String toString() {
		return super.toString() + " " + data.getUID36();
	}

	public __struct__(StarStruct data) {
		this.data = data;
	}

	public __struct__<C> copy() {
		final __struct__<C> result = new __struct__(data.getRealClass());
		if (result.data.getRealClass() != this.data.getRealClass()) {
			throw new IllegalStateException();
		}
		result.____(this);
		return result;
	}

	public static <C extends __ptr__> __struct__<C> from(Class<C> cl) {
		return new __struct__<C>(cl);
	}

	public void ____(__struct__<C> other) {
		data.copyDataFrom(other);
	}

	public void ____(__ptr__ other) {
		if (other instanceof StarArrayOfPtr) {
			____(((StarArrayOfPtr) other).getStruct());
			return;
		}
		if (other instanceof StarStruct) {
			memcopyFrom((StarStruct) other);
			return;
		}
		if (other instanceof StarArrayOfStruct) {
			StarArrayOfStruct array = (StarArrayOfStruct) other;
			memcopyFrom(array.getStruct());
			return;
		}
		throw new UnsupportedOperationException();
	}

	public void memcopyFrom(Area source) {
		data.memcopyFrom(source);
	}

	public StarStruct amp() {
		return data;
	}

	public StarStruct getInternalData() {
		return data;
	}

	public __ptr__ getPtr(String fieldName) {
		return data.getPtr(fieldName);
	}

	public __struct__ getStruct(String fieldName) {
		return data.getStruct(fieldName);
	}

	public boolean getBoolean(String fieldName) {
		return data.getBoolean(fieldName);
	}

	public double getDouble(String fieldName) {
		return data.getDouble(fieldName);
	}
	
	public void setCString(String fieldName, CString value) {
		data.setCString(fieldName, value);
	}


	public CString getCString(String fieldName) {
		return data.getCString(fieldName);
	}

	public int getInt(String fieldName) {
		return data.getInt(fieldName);
	}

	public __ptr__ setPtr(String fieldName, __ptr__ value) {
		return data.setPtr(fieldName, value);
	}

	public void setStruct(String fieldName, __struct__ value) {
		data.setStruct(fieldName, value);
	}

	public void setDouble(String fieldName, double value) {
		data.setDouble(fieldName, value);
	}

	public void setInt(String fieldName, int value) {
		data.setInt(fieldName, value);
	}

	public void setBoolean(String fieldName, boolean value) {
		data.setBoolean(fieldName, value);
	}

	public Object call(String name, Object... args) {
		return data.call(name, args);
	}

//	public __array__ getArray(String fieldName) {
//		return data.getArray(fieldName);
//	}

	public __array_of_integer__ getArrayOfInteger(String fieldName) {
		throw new UnsupportedOperationException();
	}
	
	public __array_of_struct__ getArrayOfStruct(String fieldName) {
		return data.getArrayOfStruct(fieldName);
	}

	public __array_of_ptr__ getArrayOfPtr(String fieldName) {
		return data.getArrayOfPtr(fieldName);
	}

	public __array_of_cstring__ getArrayOfCString(String fieldName) {
		throw new UnsupportedOperationException();
	}


}
