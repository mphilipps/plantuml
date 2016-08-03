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
package net.sourceforge.plantuml.statediagram;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class StateDiagram extends AbstractEntityDiagram {

	private static final String CONCURRENT_PREFIX = "CONC";

	public boolean checkConcurrentStateOk(Code code) {
		if (leafExist(code) == false) {
			return true;
		}
		final IEntity existing = this.getLeafsget(code);
		if (getCurrentGroup().getGroupType() == GroupType.CONCURRENT_STATE
				&& getCurrentGroup() != existing.getParentContainer()) {
			return false;
		}
		if (existing.getParentContainer().getGroupType() == GroupType.CONCURRENT_STATE
				&& getCurrentGroup() != existing.getParentContainer()) {
			return false;
		}
		return true;
	}

	@Override
	public IEntity getOrCreateLeaf(Code code, LeafType type, USymbol symbol) {
		if (checkConcurrentStateOk(code) == false) {
			throw new IllegalStateException("Concurrent State " + code);
		}
		if (type == null) {
			if (code.getFullName().startsWith("[*]")) {
				throw new IllegalArgumentException();
			}
			if (isGroup(code)) {
				return getGroup(code);
			}
			return getOrCreateLeafDefault(code, LeafType.STATE, null);
		}
		return getOrCreateLeafDefault(code, type, symbol);
	}

	public IEntity getStart() {
		final IGroup g = getCurrentGroup();
		if (EntityUtils.groupRoot(g)) {
			return getOrCreateLeaf(Code.of("*start"), LeafType.CIRCLE_START, null);
		}
		return getOrCreateLeaf(Code.of("*start*" + g.getCode().getFullName()), LeafType.CIRCLE_START, null);
	}

	public IEntity getEnd() {
		final IGroup p = getCurrentGroup();
		if (EntityUtils.groupRoot(p)) {
			return getOrCreateLeaf(Code.of("*end"), LeafType.CIRCLE_END, null);
		}
		return getOrCreateLeaf(Code.of("*end*" + p.getCode().getFullName()), LeafType.CIRCLE_END, null);
	}

	public IEntity getHistorical() {
		final IGroup g = getCurrentGroup();
		if (EntityUtils.groupRoot(g)) {
			return getOrCreateLeaf(Code.of("*historical"), LeafType.PSEUDO_STATE, null);
		}
		return getOrCreateLeaf(Code.of("*historical*" + g.getCode().getFullName()), LeafType.PSEUDO_STATE, null);
	}

	public IEntity getHistorical(Code codeGroup) {
		final IEntity g = getOrCreateGroup(codeGroup, Display.getWithNewlines(codeGroup), GroupType.STATE,
				getRootGroup());
		final IEntity result = getOrCreateLeaf(Code.of("*historical*" + g.getCode().getFullName()),
				LeafType.PSEUDO_STATE, null);
		endGroup();
		return result;
	}

	public boolean concurrentState(char direction) {
		final IGroup cur = getCurrentGroup();
		// printlink("BEFORE");
		if (EntityUtils.groupRoot(cur) == false && cur.getGroupType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		getCurrentGroup().setConcurrentSeparator(direction);
		final IGroup conc1 = getOrCreateGroup(UniqueSequence.getCode(CONCURRENT_PREFIX), Display.create(""),
				GroupType.CONCURRENT_STATE, getCurrentGroup());
		if (EntityUtils.groupRoot(cur) == false && cur.getGroupType() == GroupType.STATE) {
			cur.moveEntitiesTo(conc1);
			super.endGroup();
			getOrCreateGroup(UniqueSequence.getCode(CONCURRENT_PREFIX), Display.create(""), GroupType.CONCURRENT_STATE,
					getCurrentGroup());
		}
		// printlink("AFTER");
		return true;
	}

	// private void printlink(String comment) {
	// Log.println("COMMENT="+comment);
	// for (Link l : getLinks()) {
	// Log.println(l);
	// }
	// }

	@Override
	public void endGroup() {
		final IGroup cur = getCurrentGroup();
		if (EntityUtils.groupRoot(cur) == false && cur.getGroupType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		super.endGroup();
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.STATE;
	}

	private boolean hideEmptyDescription = false;

	public final void setHideEmptyDescription(boolean hideEmptyDescription) {
		this.hideEmptyDescription = hideEmptyDescription;
	}

	public final boolean isHideEmptyDescriptionForState() {
		return hideEmptyDescription;
	}


}
