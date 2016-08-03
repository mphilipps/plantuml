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
package net.sourceforge.plantuml.project2;

import java.util.HashMap;
import java.util.Map;

public class TimeConverterDay implements TimeConverter {

	private Day biggest;
	private Day smallest;
	private final double dayWith;
	private final Map<Day, Integer> map1 = new HashMap<Day, Integer>();
	private final Map<Integer, Day> map2 = new HashMap<Integer, Day>();
	private final TimeLine timeLine;

	public TimeConverterDay(TimeLine timeLine, Day start, double dayWith) {
		this.timeLine = timeLine;
		this.dayWith = dayWith;
		this.biggest = start;
		this.smallest = start;
		putDay(start, 0);
	}

//	private boolean isClosed(Day d) {
//		WeekDay wd = d.getWeekDay();
//		if (wd == WeekDay.SAT || wd == WeekDay.SUN) {
//			return true;
//		}
//		return false;
//	}

	private int getPosition(Day d) {
		Integer result = map1.get(d);
		if (result != null) {
			return result.intValue();
		}
		while (d.compareTo(biggest) > 0) {
			int n = getPosition(biggest);
			biggest = biggest.next();
			if (timeLine.isClosed(biggest) == false) {
				n++;
			}
			putDay(biggest, n);
		}
		while (d.compareTo(smallest) < 0) {
			int n = getPosition(smallest);
			smallest = smallest.previous();
			if (timeLine.isClosed(smallest) == false) {
				n--;
			}
			putDay(smallest, n);
		}
		result = map1.get(d);
		if (result != null) {
			return result.intValue();
		}
		throw new UnsupportedOperationException();
	}

	private void putDay(Day d, int n) {
		map1.put(d, n);
		map2.put(n, d);

	}

	public Day getCorrespondingElement(long position) {
		throw new UnsupportedOperationException();
	}

	public double getStartPosition(TimeElement timeElement) {
		return getPosition((Day) timeElement) * dayWith;
	}

	public double getEndPosition(TimeElement timeElement) {
		return (getPosition((Day) timeElement) + 1) * dayWith;
	}

}
