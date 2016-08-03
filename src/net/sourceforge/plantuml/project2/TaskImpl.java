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

public class TaskImpl implements Task {

	private final String code;
	private TimeElement start;
	private TimeElement end;
	private int duration;
	private final TimeLine timeLine;

	public TaskImpl(TimeLine timeLine, String code) {
		this.code = code;
		this.timeLine = timeLine;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return code;
	}

	public long getLoad() {
		throw new UnsupportedOperationException();
	}

	public TimeElement getStart() {
		return start;
	}

	public TimeElement getEnd() {
		TimeElement result = start;
		for (int i = 1; i < duration; i++) {
			result = timeLine.next(result);
		}
		return result;
	}

	public TimeElement getCompleted() {
		return timeLine.next(getEnd());
	}

	public void setStart(TimeElement exp) {
		this.start = exp;
	}

	public void setDuration(int value) {
		this.duration = value;
	}

	public void setLoad(int value) {
	}

}
