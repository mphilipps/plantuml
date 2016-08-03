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

package smetana.core.amiga;

public class AreaInt implements Area {

	private int data = 0;

	private final int UID = StarStruct.CPT++;

	private String getUID36() {
		return Integer.toString(UID, 36);
	}

	@Override
	public String toString() {
		return "AreaArray " + getUID36() + " " + data;
	}

	public void memcopyFrom(Area source) {
		AreaInt other = (AreaInt) source;
		this.data = other.data;
	}

	public void setInternal(int data) {
		this.data = data;
		if (trace()) {
			System.err.println("set I AM " + this);
		}
	}

	private boolean trace() {
		return false;
		//return getUID36().equals("2z7");
	}

	public int getInternal() {
		if (trace()) {
			// System.err.println("get I AM " + this);
		}
		return data;
	}

	// public void incInternal(int increment) {
	// data += increment;
	//
	// }
}
