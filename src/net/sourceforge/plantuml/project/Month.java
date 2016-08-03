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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum Month {

	JAN(31), FEB(28), MAR(31), APR(30), MAY(31), JUN(30), JUL(31), AUG(31), SEP(30), OCT(31), NOV(30), DEC(31);

	final private int nbDays;

	private Month(int nbDays) {
		this.nbDays = nbDays;
	}

	public final int getNbDays(int year) {
		if (this == FEB && year % 4 == 0) {
			return 29;
		}
		return nbDays;
	}

	public final int getNum() {
		return ordinal() + 1;
	}

	public final int getNumNormal() {
		return ordinal();
	}

	public Month next() {
		if (this == DEC) {
			return null;
		}
		final List<Month> all = new ArrayList<Month>(EnumSet.allOf(Month.class));
		return all.get(getNum());
	}

	public Month prev() {
		if (this == JAN) {
			return null;
		}
		final List<Month> all = new ArrayList<Month>(EnumSet.allOf(Month.class));
		return all.get(getNum() - 2);
	}

	public static Month fromNum(int num) {
		if (num < 1 || num > 12) {
			throw new IllegalArgumentException();
		}
		final List<Month> all = new ArrayList<Month>(EnumSet.allOf(Month.class));
		return all.get(num - 1);
	}
}
