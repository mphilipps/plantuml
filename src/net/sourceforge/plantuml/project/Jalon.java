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

import java.util.List;

public class Jalon implements Item {

	private Instant begin;
	private final String code;
	private final Item parent;

	public Jalon(String code, Item parent) {
		this.code = code;
		this.parent = parent;
	}

	public Instant getBegin() {
		return begin;
	}

	public Instant getCompleted() {
		return begin;
	}

	public Duration getDuration() {
		return new Duration(0);
	}

	public Load getLoad() {
		return new Load(0);
	}

	public NumericNumber getWork() {
		return new NumericNumber(1);
	}

	public boolean isLeaf() {
		return true;
	}

	public Item getParent() {
		return parent;
	}

	public List<Item> getChildren() {
		return null;
	}

	public String getCode() {
		return code;
	}

	public boolean isValid() {
		return begin != null;
	}

	public void setInstant(Numeric value) {
		this.begin = (Instant) value;
	}

}
