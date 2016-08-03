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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class GroupingStart extends Grouping {

	private final List<GroupingLeaf> children = new ArrayList<GroupingLeaf>();
	private final HtmlColor backColorGeneral;

	final private GroupingStart parent;

	public GroupingStart(String title, String comment, HtmlColor backColorGeneral, HtmlColor backColorElement,
			GroupingStart parent) {
		super(title, comment, GroupingType.START, backColorElement);
		this.backColorGeneral = backColorGeneral;
		this.parent = parent;
	}

	List<GroupingLeaf> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public void addChildren(GroupingLeaf g) {
		children.add(g);
	}

	public int getLevel() {
		if (parent == null) {
			return 0;
		}
		return parent.getLevel() + 1;
	}

	@Override
	public HtmlColor getBackColorGeneral() {
		return backColorGeneral;
	}

	public boolean dealWith(Participant someone) {
		return false;
	}

	public Url getUrl() {
		return null;
	}
	
	public boolean hasUrl() {
		return false;
	}


	@Override
	public boolean isParallel() {
		return getTitle().equals("par2");
	}

}
