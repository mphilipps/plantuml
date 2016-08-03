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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ParentItem implements Item {

	private final String code;
	private final Item parent;

	private final List<Item> children = new ArrayList<Item>();

	public ParentItem(String code, Item parent) {
		this.code = code;
		this.parent = parent;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(code + " {");
		for (final Iterator<Item> it = children.iterator(); it.hasNext();) {
			final Item child = it.next();
			sb.append(child.getCode());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public Instant getBegin() {
		Instant result = null;
		for (Item it : children) {
			if (result == null || result.compareTo(it.getBegin()) > 0) {
				result = it.getBegin();
			}
		}
		return result;
	}

	public Instant getCompleted() {
		Instant result = null;
		for (Item it : children) {
			if (result == null || result.compareTo(it.getCompleted()) < 0) {
				result = it.getCompleted();
			}
		}
		return result;
	}

	public Duration getDuration() {
		throw new UnsupportedOperationException();
	}

	public Load getLoad() {
		throw new UnsupportedOperationException();
	}

	public NumericNumber getWork() {
		throw new UnsupportedOperationException();
	}

	public boolean isLeaf() {
		return false;
	}

	public Item getParent() {
		return parent;
	}

	public List<Item> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public String getCode() {
		return code;
	}

	public void addChild(Item child) {
		this.children.add(child);
	}

	public boolean isValid() {
		if (children.size() == 0) {
			return false;
		}
		for (Item it : children) {
			if (it.isValid() == false) {
				return false;
			}
		}
		return true;
	}

}
