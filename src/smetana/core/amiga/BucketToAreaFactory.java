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

import smetana.core.Bucket;
import smetana.core.CType;
import smetana.core.JUtils;
import smetana.core.__array_of_double__;
import smetana.core.__array_of_integer__;
import smetana.core.__array_of_ptr__;
import smetana.core.__array_of_struct__;

public class BucketToAreaFactory {

	public static Area createArea(Bucket bucket, StarStruct parent) {
		if (bucket.ctype.getArrayLength() != 0) {
			return createAreaArray(bucket, bucket.ctype.getArrayLength());
		}
		if (bucket.ctype.functionPointer()) {
			// return PointerToNull.nullPointerTo();
			// return new AreaFunctionPointer();
			return null;
		}
		if (bucket.ctype.isIntStar()) {
			return null;
		}
		if (bucket.ctype.isDoubleStar()) {
			return null;
		}
		if (bucket.ctype.isVoidStar()) {
			return null;
			// return PointerToNull.nullPointerTo();
			// return new AreaVoidStar();
		}
		if (bucket.ctype.containsStar()) {
			final String type = bucket.ctype.getType();
			if (type.matches("\\w+\\*")) {
				final Class theClass = CType.getClassFrom(type.substring(0, type.length() - 1));
				JUtils.LOG("theClass=" + theClass);
				// return PointerToNull.nullPointerTo();
				return null;
			}
			throw new UnsupportedOperationException(bucket.toString());
		}
		if (bucket.ctype.isEnum()) {
			return new AreaInt();
		}
		if (bucket.ctype.isPrimitive()) {
			if (bucket.ctype.isInteger()) {
				return new AreaInt();
			}
			if (bucket.ctype.isChar()) {
				return new AreaInt();
			}
			if (bucket.ctype.isShort()) {
				return new AreaInt();
			}
			if (bucket.ctype.isLong()) {
				return new AreaInt();
			}
			if (bucket.ctype.isBoolean()) {
				return new AreaInt();
			}
			if (bucket.ctype.isDoubleOrFloat()) {
				return new AreaDouble();
			}
			throw new UnsupportedOperationException();
		}
		if (bucket.inlineStruct()) {
			final Class theClass = bucket.ctype.getTypeClass();
			return new StarStruct(theClass, parent);
		}
		if (bucket.ctype.isArrayOfCString()) {
			return null;
		}
		if (bucket.ctype.isCString()) {
			// return new AreaCString();
			// return PointerToNull.nullPointerTo();
			return null;
		}
		final Class theClass = bucket.ctype.getTypeClass();
		if (theClass != null) {
			return null;
			// return PointerToNull.nullPointerTo();
		}
		JUtils.LOG("BucketToAreaFactory:: theClass = " + theClass);
		JUtils.LOG("BucketToAreaFactory:: bucket=" + bucket);
		JUtils.LOG("BucketToAreaFactory:: bucket.ctype=" + bucket.ctype);
		throw new UnsupportedOperationException();
	}

	private static Area createAreaArray(Bucket bucket, int arrayLength) {
		JUtils.LOG("BucketToAreaFactory:createAreaArray: bucket=" + bucket);
		JUtils.LOG("BucketToAreaFactory:createAreaArray: arrayLength=" + arrayLength);
		JUtils.LOG("BucketToAreaFactory:createAreaArray: type=" + bucket.ctype);
		if (bucket.ctype.getType().matches("char \\w+\\[\\d+\\]")) {
			// Array of char
			return __array_of_integer__.mallocInteger(arrayLength);
		}
		if (bucket.ctype.getType().matches("int \\w+\\[\\d+\\]")) {
			// Array of int
			return __array_of_integer__.mallocInteger(arrayLength);
		}
		if (bucket.ctype.getType().matches("double \\w+\\[\\d+\\]")) {
			// Array of double
			return __array_of_double__.mallocDouble(arrayLength);
		}
		if (bucket.ctype.getType().matches("\\w+ \\*\\w+\\[\\d+\\]")) {
			// Array of pointer
			final String element = bucket.ctype.getType().split(" ")[0];
			JUtils.LOG("element=" + element);
			final Class theClass = CType.getClassFrom(element);
			JUtils.LOG("theClass=" + theClass);
			return __array_of_ptr__.malloc_empty(arrayLength);
		}
		if (bucket.ctype.getType().matches("\\w+ \\w+\\[\\d+\\]")) {
			// Array of Struct
			final String element = bucket.ctype.getType().split(" ")[0];
			JUtils.LOG("element=" + element);
			final Class theClass = CType.getClassFrom(element);
			JUtils.LOG("theClass=" + theClass);
			return __array_of_struct__.malloc(theClass, arrayLength);
		}
		throw new UnsupportedOperationException();
	}
}
