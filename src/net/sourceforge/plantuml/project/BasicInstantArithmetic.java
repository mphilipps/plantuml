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
package net.sourceforge.plantuml.project;

class BasicInstantArithmetic implements InstantArithmetic {

	private final DayClose dayClose;

	BasicInstantArithmetic(DayClose dayClose) {
		if (dayClose == null) {
			throw new IllegalArgumentException();
		}
		this.dayClose = dayClose;
	}

	public Instant add(Instant i1, Duration duration) {
		Instant result = i1;
		final long min = duration.getMinutes();
		if (min < 0) {
			throw new IllegalArgumentException();
		}
		for (long i = 0; i < min; i += 24 * 60 * 60) {
			result = result.next(dayClose);
		}
		return result;
	}

	public Instant sub(Instant i1, Duration duration) {
		Instant result = i1;
		final long min = duration.getMinutes();
		if (min < 0) {
			throw new IllegalArgumentException();
		}
		for (long i = 0; i < min; i += 24 * 60 * 60) {
			result = result.prev(dayClose);
		}
		return result;
	}

	public Duration diff(Instant i1, Instant i2) {
		if (i2.compareTo(i1) < 0) {
			throw new IllegalArgumentException();
		}
		long minutes = 0;
		while (i2.compareTo(i1) > 0) {
			minutes += 24 * 60 * 60;
			i1 = i1.next(null);
		}
		return new Duration(minutes);
	}
}
