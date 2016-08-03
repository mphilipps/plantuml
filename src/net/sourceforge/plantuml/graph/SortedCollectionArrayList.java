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
package net.sourceforge.plantuml.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SortedCollectionArrayList<S extends Comparable<S>> implements SortedCollection<S> {

	private final List<S> allAsList = new ArrayList<S>();
	private final Set<S> allAsSet = new HashSet<S>();

	public Iterator<S> iterator() {
		return allAsList.iterator();
	}

	public void add(S newEntry) {
		final int r = Collections.binarySearch(allAsList, newEntry);
		if (r >= 0) {
			allAsList.add(r, newEntry);
		} else {
			allAsList.add(-1 - r, newEntry);
		}
		allAsSet.add(newEntry);
		assert isSorted();
	}

	public int size() {
		assert allAsSet.size() == allAsList.size();
		return allAsList.size();
	}

	List<S> toList() {
		return new ArrayList<S>(allAsList);
	}

	boolean isSorted() {
		S before = null;
		for (S ent : allAsList) {
			if (before != null && ent.compareTo(before) < 0) {
				return false;
			}
			before = ent;
		}
		return true;
	}

	public boolean contains(S entry) {
		return allAsSet.contains(entry);
	}

}
