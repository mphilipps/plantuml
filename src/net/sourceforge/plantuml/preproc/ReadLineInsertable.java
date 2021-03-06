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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;
import net.sourceforge.plantuml.LineLocation;

class ReadLineInsertable implements ReadLine {

	private final ReadLine source;
	private final List<CharSequence2> inserted = new LinkedList<CharSequence2>();

	public ReadLineInsertable(ReadLine source) {
		this.source = source;
	}

	public void close() throws IOException {
		source.close();
	}

	public CharSequence2 readLine() throws IOException {
		if (inserted.size() > 0) {
			final Iterator<CharSequence2> it = inserted.iterator();
			final CharSequence2 result = it.next();
			it.remove();
			return result;
		}
		return source.readLine();
	}

	public void insert(List<? extends CharSequence> data, LineLocation location) {
		for (CharSequence s : data) {
			inserted.add(new CharSequence2Impl(s, location));
		}
	}

}
