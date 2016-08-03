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
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionRepeat implements Instruction {

	private final InstructionList repeatList = new InstructionList();
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;
	private final Swimlane swimlane;
	private final HtmlColor color;
	private boolean killed = false;

	private Display test = Display.NULL;
	private Display yes = Display.NULL;
	private Display out = Display.NULL;
	private boolean testCalled = false;
	private LinkRendering endRepeatLinkRendering;
	private LinkRendering backRepeatLinkRendering;

	public InstructionRepeat(Swimlane swimlane, Instruction parent, LinkRendering nextLinkRenderer, HtmlColor color) {
		this.parent = parent;
		this.swimlane = swimlane;
		this.nextLinkRenderer = nextLinkRenderer;
		this.color = color;
	}

	public void add(Instruction ins) {
		repeatList.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final Ftile result = factory.repeat(swimlane,
				factory.decorateOut(repeatList.createFtile(factory), endRepeatLinkRendering), test, yes, out, color,
				backRepeatLinkRendering);
		if (killed) {
			return new FtileKilled(result);
		}
		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public void setTest(Display test, Display yes, Display out, LinkRendering endRepeatLinkRendering,
			LinkRendering backRepeatLinkRendering) {
		this.test = test;
		this.yes = yes;
		this.out = out;
		if (test == null) {
			throw new IllegalArgumentException();
		}
		if (yes == null) {
			throw new IllegalArgumentException();
		}
		if (out == null) {
			throw new IllegalArgumentException();
		}
		this.endRepeatLinkRendering = endRepeatLinkRendering;
		this.backRepeatLinkRendering = backRepeatLinkRendering;
		this.testCalled = true;
	}

	final public boolean kill() {
		if (testCalled) {
			this.killed = true;
			return true;
		}
		return repeatList.kill();
	}

	public LinkRendering getInLinkRendering() {
		return nextLinkRenderer;
	}

	public boolean addNote(Display note, NotePosition position) {
		return repeatList.addNote(note, position);
	}

	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return repeatList.getSwimlaneOut();
	}

}
