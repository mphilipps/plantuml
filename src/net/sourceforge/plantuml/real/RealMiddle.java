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
package net.sourceforge.plantuml.real;

class RealMiddle extends AbstractReal implements Real {

	private final RealMoveable p1;
	private final RealMoveable p2;
	private final double delta;

	private RealMiddle(RealMoveable p1, RealMoveable p2, double delta) {
		super(p1.getLine());
		this.p1 = p1;
		this.p2 = p2;
		this.delta = delta;
	}

	RealMiddle(RealMoveable p1, RealMoveable p2) {
		this(p1, p2, 0);
	}

	@Override
	double getCurrentValueInternal() {
		return (p1.getCurrentValue() + p2.getCurrentValue()) / 2 + delta;
	}

	public Real addFixed(double diff) {
		return new RealMiddle(p1, p2, delta + diff);
	}

	public Real addAtLeast(double delta) {
		throw new UnsupportedOperationException();
	}

	public void ensureBiggerThan(Real other) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return "[Middle " + p1.getName() + " and " + p2.getName() + "]";
	}

	public void printCreationStackTrace() {
		throw new UnsupportedOperationException();
	}

}
