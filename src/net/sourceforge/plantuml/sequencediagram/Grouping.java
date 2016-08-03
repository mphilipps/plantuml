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

import net.sourceforge.plantuml.graphic.HtmlColor;

public abstract class Grouping implements Event {

	private final String title;
	private final GroupingType type;
	private final String comment;
	private final HtmlColor backColorElement;

	public Grouping(String title, String comment, GroupingType type,
			HtmlColor backColorElement) {
		this.title = title;
		this.comment = comment;
		this.type = type;
		this.backColorElement = backColorElement;
	}

	@Override
	public final String toString() {
		return super.toString() + " " + type + " " + title;
	}

	final public String getTitle() {
		return title;
	}

	final public GroupingType getType() {
		return type;
	}

	public abstract int getLevel();

	public abstract HtmlColor getBackColorGeneral();
	
	final public String getComment() {
		return comment;
	}

	public final HtmlColor getBackColorElement() {
		return backColorElement;
	}
	
	public abstract boolean isParallel(); 

}
