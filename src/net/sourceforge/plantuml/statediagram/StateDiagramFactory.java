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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.statediagram.command.CommandAddField;
import net.sourceforge.plantuml.statediagram.command.CommandConcurrentState;
import net.sourceforge.plantuml.statediagram.command.CommandCreatePackageState;
import net.sourceforge.plantuml.statediagram.command.CommandCreateState;
import net.sourceforge.plantuml.statediagram.command.CommandEndState;
import net.sourceforge.plantuml.statediagram.command.CommandHideEmptyDescription;
import net.sourceforge.plantuml.statediagram.command.CommandLinkState;

public class StateDiagramFactory extends UmlDiagramFactory {

	@Override
	public StateDiagram createEmptyDiagram() {
		return new StateDiagram();
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());
		cmds.add(new CommandRankDir());
		cmds.add(new CommandCreateState());
		// addCommand(new CommandLinkState());
		cmds.add(new CommandLinkState());
		cmds.add(new CommandCreatePackageState());
		cmds.add(new CommandEndState());
		cmds.add(new CommandAddField());
		cmds.add(new CommandConcurrentState());

		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexOr(
				"ENTITY", new RegexLeaf("[\\p{L}0-9_.]+"), //
				new RegexLeaf("[%g][^%g]+[%g]") //
				));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(true));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(false));

		cmds.add(new CommandHideEmptyDescription());

		cmds.add(factoryNoteOnEntityCommand.createSingleLine());
		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));
		cmds.add(new CommandUrl());

		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();
		cmds.add(factoryNoteCommand.createSingleLine());
		cmds.add(factoryNoteCommand.createMultiLine(false));

		addCommonCommands(cmds);

		return cmds;
	}

	@Override
	public String checkFinalError(AbstractPSystem sys) {
		final StateDiagram system = (StateDiagram) sys;

		for (Link link : system.getLinks()) {
			final IGroup parent1 = getGroupParentIfItIsConcurrentState(link.getEntity1());
			final IGroup parent2 = getGroupParentIfItIsConcurrentState(link.getEntity2());
			if (isCompatible(parent1, parent2) == false) {
				return "State within concurrent state cannot be linked out of this concurrent state (between "
						+ link.getEntity1().getCode().getFullName() + " and "
						+ link.getEntity2().getCode().getFullName() + ")";
			}
		}
		return super.checkFinalError(system);
	}

	private boolean isCompatible(IGroup parent1, IGroup parent2) {
		if (parent1 == null && parent2 == null) {
			return true;
		}
		if (parent1 != null ^ parent2 != null) {
			return false;
		}
		assert parent1 != null && parent2 != null;
		return parent1 == parent2;
	}

	private IGroup getGroupParentIfItIsConcurrentState(IEntity ent) {
		IGroup parent = ent.getParentContainer();
		while (parent != null) {
			if (parent.getGroupType() == GroupType.CONCURRENT_STATE) {
				return parent;
			}
			parent = parent.getParentContainer();
		}
		return null;

	}

}
