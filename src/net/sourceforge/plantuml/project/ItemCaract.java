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

enum ItemCaract {
	BEGIN(NumericType.INSTANT), //
	COMPLETED(NumericType.INSTANT), //
	DURATION(NumericType.DURATION), //
	LOAD(NumericType.LOAD), //
	WORK(NumericType.NUMBER);

	private final NumericType type;

	private ItemCaract(NumericType type) {
		this.type = type;
	}

	public NumericType getNumericType() {
		return type;
	}

	public Numeric getData(Item item) {
		if (this == BEGIN) {
			return item.getBegin();
		}
		if (this == COMPLETED) {
			return item.getCompleted();
		}
		if (this == DURATION) {
			return item.getDuration();
		}
		if (this == LOAD) {
			return item.getLoad();
		}
		if (this == WORK) {
			return item.getWork();
		}
		throw new UnsupportedOperationException();
	}
}
