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
package net.sourceforge.plantuml.hector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SkeletonConfigurationSet implements Iterable<SkeletonConfiguration> {

	private final List<SkeletonConfiguration> all;
	private final SkeletonConfigurationComparator comparator;
	private final int limitSize;

	public SkeletonConfigurationSet(int limitSize, SkeletonConfigurationEvaluator evaluator) {
		this.comparator = new SkeletonConfigurationComparator(evaluator);
		this.all = new ArrayList<SkeletonConfiguration>();
		this.limitSize = limitSize;
	}

	public void add(SkeletonConfiguration skeletonConfiguration) {
		this.all.add(skeletonConfiguration);
		sortAndTruncate();
	}

	public void addAll(Collection<SkeletonConfiguration> others) {
		all.addAll(others);
		sortAndTruncate();
	}

	private void sortAndTruncate() {
		Collections.sort(all, comparator);
		while (all.size() > limitSize) {
			all.remove(all.size() - 1);
		}
	}

	@Override
	public String toString() {
		return all.toString();
	}

	public int size() {
		return all.size();
	}

	public Iterator<SkeletonConfiguration> iterator() {
		return new ArrayList<SkeletonConfiguration>(all).iterator();
	}

	public SkeletonConfiguration first() {
		return all.get(0);
	}

}
