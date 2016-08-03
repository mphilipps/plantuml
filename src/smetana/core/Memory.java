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
import smetana.core.amiga.StarStruct;

public class Memory {

	public static __ptr__ malloc(Class cl) {
		JUtils.LOG("MEMORY::malloc " + cl);
		return StarStruct.malloc(cl);
	}

	public static __ptr__ malloc(size_t size) {
		return (__ptr__) size.malloc();
	}

	public static __ptr__ realloc(__ptr__ old, size_t size) {
		if (old instanceof StarArrayOfPtr) {
			((StarArrayOfPtr) old).realloc(((size_t_array_of_something) size).getNb());
			return old;
		}
		if (old instanceof StarStar) {
			((StarStar) old).realloc(((size_t_array_of_array_of_something_empty) size).getNb());
			return old;
		}
		throw new UnsupportedOperationException();
	}

	public static void free(Object arg) {
	}

	public static int identityHashCode(CString data) {
		if (data==null) {
			return 0;
		}
		//int result = 2 * System.identityHashCode(data);
		int result = data.getUid();
		Z._().all.put(result, data);
		// System.err.println("Memory::identityHashCode data=" + data);
		// System.err.println("Memory::identityHashCode result=" + result + " " + Z._().all.size());
		return result;
	}

	public static Object fromIdentityHashCode(int hash) {
		// System.err.println("Memory::fromIdentityHashCode hash=" + hash);
		if (hash % 2 != 0) {
			throw new IllegalArgumentException();
		}
		Object result = Z._().all.get(hash);
		// System.err.println("Memory::fromIdentityHashCode result=" + result);
		if (result == null) {
			throw new UnsupportedOperationException();
		}
		return result;
	}

}
