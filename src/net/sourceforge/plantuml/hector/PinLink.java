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

public class PinLink {

	private final Pin pin1;
	private final Pin pin2;
	private final Object userData;
	private final int length;

	public PinLink(Pin pin1, Pin pin2, int length, Object userData) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		this.pin1 = pin1;
		this.pin2 = pin2;
		this.userData = userData;
		this.length = length;
	}

	public boolean contains(Pin pin) {
		return pin == pin1 || pin == pin2;
	}

	public boolean doesTouch(PinLink other) {
		return other.contains(pin1) || other.contains(pin2);
	}

	public Pin getPin1() {
		return pin1;
	}

	public Pin getPin2() {
		return pin2;
	}

	public int getLengthDot() {
		return length;
	}

	public int getLengthStandard() {
		return length - 1;
	}
}
