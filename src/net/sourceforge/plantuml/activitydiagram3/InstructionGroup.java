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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionGroup implements Instruction, InstructionCollection {

	private final InstructionList list;
	private final Instruction parent;
	private final HtmlColor backColor;
	private final HtmlColor borderColor;
	private final HtmlColor titleColor;

	private final Display test;
	private Display headerNote = Display.NULL;

	public InstructionGroup(Instruction parent, Display test, HtmlColor backColor, HtmlColor titleColor,
			Swimlane swimlane, HtmlColor borderColor) {
		this.list = new InstructionList(swimlane);
		this.parent = parent;
		this.test = test;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.titleColor = titleColor;
	}

	public void add(Instruction ins) {
		list.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		return factory.createGroup(list.createFtile(factory), test, backColor, titleColor, headerNote, borderColor);
	}

	public Instruction getParent() {
		return parent;
	}

	final public boolean kill() {
		return list.kill();
	}

	public LinkRendering getInLinkRendering() {
		return null;
	}

	public boolean addNote(Display note, NotePosition position) {
		if (list.isEmpty()) {
			this.headerNote = note;
			return true;
		}
		return list.addNote(note, position);
	}

	public Set<Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return list.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return list.getSwimlaneOut();
	}

	public Instruction getLast() {
		return list.getLast();
	}

}
