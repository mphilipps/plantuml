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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNoteOpale;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionIf implements Instruction, InstructionCollection {

	private final List<Branch> thens = new ArrayList<Branch>();
	private Branch elseBranch;
	private final ISkinParam skinParam;

	private final Instruction parent;

	private Branch current;
	private final LinkRendering topInlinkRendering;
	private LinkRendering afterEndwhile;

	private final Swimlane swimlane;

	public InstructionIf(Swimlane swimlane, Instruction parent, Display labelTest, Display whenThen,
			LinkRendering inlinkRendering, HtmlColor color, ISkinParam skinParam) {
		this.parent = parent;
		this.skinParam = skinParam;
		this.topInlinkRendering = inlinkRendering;
		this.swimlane = swimlane;
		this.thens.add(new Branch(swimlane, whenThen, labelTest, color));
		this.current = this.thens.get(0);
	}

	public void add(Instruction ins) {
		current.add(ins);
	}

	private Display note;
	private NotePosition position;

	public Ftile createFtile(FtileFactory factory) {
		for (Branch branch : thens) {
			branch.updateFtile(factory);
		}
		if (elseBranch == null) {
			this.elseBranch = new Branch(swimlane, Display.NULL, Display.NULL, null);
		}
		elseBranch.updateFtile(factory);
		Ftile result = factory.createIf(swimlane, thens, elseBranch, afterEndwhile, topInlinkRendering);
		if (note != null) {
			result = new FtileWithNoteOpale(result, note, position, skinParam, false);
		}
		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public boolean swithToElse2(Display whenElse, LinkRendering nextLinkRenderer) {
		if (elseBranch != null) {
			return false;
		}
		this.current.setInlinkRendering(nextLinkRenderer);
		this.elseBranch = new Branch(swimlane, whenElse, Display.NULL, null);
		this.current = elseBranch;
		return true;
	}

	public boolean elseIf(Display test, Display whenThen, LinkRendering nextLinkRenderer, HtmlColor color) {
		if (elseBranch != null) {
			return false;
		}
		this.current.setInlinkRendering(nextLinkRenderer);
		this.current = new Branch(swimlane, whenThen, test, color);
		this.thens.add(current);
		return true;

	}

	public void endif(LinkRendering nextLinkRenderer) {
		if (elseBranch == null) {
			this.elseBranch = new Branch(swimlane, Display.NULL, Display.NULL, null);
		}
		this.current.setInlinkRendering(nextLinkRenderer);
	}

	final public boolean kill() {
		return current.kill();
	}

	public LinkRendering getInLinkRendering() {
		return topInlinkRendering;
	}

	public boolean addNote(Display note, NotePosition position) {
		if (current.isEmpty()) {
			this.note = note;
			this.position = position;
			return true;
		} else {
			return current.addNote(note, position);
		}
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (swimlane != null) {
			result.add(swimlane);
		}
		for (Branch branch : thens) {
			result.addAll(branch.getSwimlanes());
		}
		result.addAll(elseBranch.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public Instruction getLast() {
		if (elseBranch == null) {
			return thens.get(thens.size() - 1).getLast();
		}
		return elseBranch.getLast();
	}

	public void afterEndwhile(LinkRendering linkRenderer) {
		this.afterEndwhile = linkRenderer;
	}

}
