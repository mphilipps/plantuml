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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.SymbolContext;

public class Stairs2 {

	private final List<StairsPosition> ys = new ArrayList<StairsPosition>();
	private final List<IntegerColored> values = new ArrayList<IntegerColored>();
	private final Map<Double, IntegerColored> cache = new HashMap<Double, IntegerColored>();

	@Override
	public String toString() {
		return ys.toString() + " " + values;
	}

	public void addStep(StairsPosition position, int value, SymbolContext color) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		// System.err.println("Stairs2::addStep " + position + " " + value + " color=" + color);
		assert ys.size() == values.size();
		if (ys.size() > 0) {
			final double lastY = ys.get(ys.size() - 1).getValue();
			if (position.getValue() < lastY) {
				// throw new IllegalArgumentException();
				return;
			}
			if (lastY == position.getValue()) {
				values.set(ys.size() - 1, new IntegerColored(value, color));
				cache.clear();
				return;
			}
		}
		ys.add(position);
		values.add(new IntegerColored(value, color));
		cache.clear();
	}

	public int getMaxValue() {
		int max = Integer.MIN_VALUE;
		for (IntegerColored vc : values) {
			final int v = vc.getValue();
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	public List<StairsPosition> getYs() {
		return Collections.unmodifiableList(ys);
	}

	public IntegerColored getValue(double y) {
		IntegerColored resultc = cache.get(y);
		if (resultc == null) {
			resultc = getValueSlow(new StairsPosition(y, false));
			cache.put(y, resultc);
		}
		return resultc;
	}

	private IntegerColored getValueSlow(StairsPosition y) {
		final int idx = Collections.binarySearch(ys, y);
		if (idx >= 0) {
			return values.get(idx);
		}
		final int insertPoint = -idx - 1;
		if (insertPoint == 0) {
			return new IntegerColored(0, null);
		}
		return values.get(insertPoint - 1);
	}

	public int getLastValue() {
		final int size = values.size();
		if (size == 0) {
			return 0;
		}
		return values.get(size - 1).getValue();
	}

}
