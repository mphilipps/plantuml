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

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionList implements Instruction, InstructionCollection {

	private final List<Instruction> all = new ArrayList<Instruction>();
	private final Swimlane defaultSwimlane;

	public boolean isOnlySingleStop() {
		if (all.size() == 1) {
			final Instruction last = getLast();
			return last instanceof InstructionStop;
		}
		return false;
	}

	public InstructionList() {
		this(null);
	}

	public boolean isEmpty() {
		return all.isEmpty();
	}

	public InstructionList(Swimlane defaultSwimlane) {
		this.defaultSwimlane = defaultSwimlane;
	}

	public void add(Instruction ins) {
		all.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		if (all.size() == 0) {
			return new FtileEmpty(factory.shadowing(), defaultSwimlane);
		}
		Ftile result = null;
		for (Instruction ins : all) {
			Ftile cur = ins.createFtile(factory);
			if (ins.getInLinkRendering() != null) {
				cur = factory.decorateIn(cur, ins.getInLinkRendering());
			}
			if (result == null) {
				result = cur;
			} else {
				result = factory.assembly(result, cur);
			}
		}
		if (outlinkRendering != null) {
			result = factory.decorateOut(result, outlinkRendering);
		}
		// if (killed) {
		// result = new FtileKilled(result);
		// }
		return result;
	}

	final public boolean kill() {
		if (all.size() == 0) {
			return false;
		}
		return getLast().kill();
	}

	public LinkRendering getInLinkRendering() {
		return all.iterator().next().getInLinkRendering();
	}

	public Instruction getLast() {
		if (all.size() == 0) {
			return null;
		}
		return all.get(all.size() - 1);
	}

	public boolean addNote(Display note, NotePosition position) {
		if (getLast() == null) {
			return false;
		}
		return getLast().addNote(note, position);
	}

	public Set<Swimlane> getSwimlanes() {
		return getSwimlanes2(all);
	}

	public Swimlane getSwimlaneIn() {
		if (getSwimlanes().size() == 0) {
			return null;
		}
		return all.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		if (getSwimlanes().size() == 0) {
			return null;
		}
		return getLast().getSwimlaneOut();
	}

	public static Set<Swimlane> getSwimlanes2(List<? extends Instruction> list) {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		for (Instruction ins : list) {
			result.addAll(ins.getSwimlanes());
		}
		return Collections.unmodifiableSet(result);
	}

	private LinkRendering outlinkRendering;

	public void setOutRendering(LinkRendering outlinkRendering) {
		this.outlinkRendering = outlinkRendering;
	}

}
