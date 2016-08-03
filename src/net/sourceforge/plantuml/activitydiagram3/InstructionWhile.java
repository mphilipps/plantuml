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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNoteOpale;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionWhile implements Instruction, InstructionCollection {

	private final InstructionList repeatList = new InstructionList();
	private final Instruction parent;
	private final LinkRendering nextLinkRenderer;
	private final HtmlColor color;
	private boolean killed = false;

	private final Display test;
	private Display yes;
	private Display out = Display.NULL;
	private boolean testCalled = false;
	private LinkRendering endInlinkRendering;
	private LinkRendering afterEndwhile;
	private final Swimlane swimlane;
	private final ISkinParam skinParam;

	public void overwriteYes(Display yes) {
		this.yes = yes;
	}

	public InstructionWhile(Swimlane swimlane, Instruction parent, Display test, LinkRendering nextLinkRenderer,
			Display yes, HtmlColor color, ISkinParam skinParam) {
		if (test == null) {
			throw new IllegalArgumentException();
		}
		if (yes == null) {
			throw new IllegalArgumentException();
		}
		this.parent = parent;
		this.test = test;
		this.nextLinkRenderer = nextLinkRenderer;
		this.yes = yes;
		this.swimlane = swimlane;
		this.color = color;
		this.skinParam = skinParam;
	}

	public void add(Instruction ins) {
		repeatList.add(ins);
	}

	private Display note;
	private NotePosition position;

	public Ftile createFtile(FtileFactory factory) {
		Ftile tmp = factory.decorateOut(repeatList.createFtile(factory), endInlinkRendering);
		tmp = factory.createWhile(swimlane, tmp, test, yes, out, afterEndwhile, color);
		if (note != null) {
			tmp = new FtileWithNoteOpale(tmp, note, position, skinParam, false);
		}
		if (killed) {
			return new FtileKilled(tmp);
		}
		return tmp;
	}

	public Instruction getParent() {
		return parent;
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

	public void endwhile(LinkRendering nextLinkRenderer, Display out) {
		this.endInlinkRendering = nextLinkRenderer;
		this.out = out;
		if (out == null) {
			throw new IllegalArgumentException();
		}
		this.testCalled = true;
	}

	public void afterEndwhile(LinkRendering linkRenderer) {
		this.afterEndwhile = linkRenderer;
	}

	public boolean addNote(Display note, NotePosition position) {
		if (repeatList.isEmpty()) {
			this.note = note;
			this.position = position;
			return true;
		} else {
			return repeatList.addNote(note, position);
		}
	}

	public Set<Swimlane> getSwimlanes() {
		return repeatList.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	public Instruction getLast() {
		return repeatList.getLast();
	}


}
