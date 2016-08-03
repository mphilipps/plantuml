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
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.NewpagedDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandAllowMixing;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClass;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateElementFull2;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateElementFull2.Mode;
import net.sourceforge.plantuml.classdiagram.command.CommandDiamondAssociation;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowSpecificClass;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowSpecificStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLayoutNewLine;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkLollipop;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespaceSeparator;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandNamespace;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPackageEmpty;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.note.FactoryTipOnEntityCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.descdiagram.command.CommandNewpage;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateEntityObject;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateEntityObjectMultilines;

public class ClassDiagramFactory extends UmlDiagramFactory {

	@Override
	public ClassDiagram createEmptyDiagram() {
		return new ClassDiagram();

	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());
		addCommonCommands(cmds);

		cmds.add(new CommandRankDir());
		cmds.add(new CommandNewpage(this));
		cmds.add(new CommandHideShowSpecificStereotype());
		cmds.add(new CommandPage());
		cmds.add(new CommandAddMethod());

		cmds.add(new CommandCreateClass());
		cmds.add(new CommandCreateEntityObject());

		cmds.add(new CommandAllowMixing());
		cmds.add(new CommandLayoutNewLine());

		cmds.add(new CommandCreateElementFull2(Mode.NORMAL_KEYWORD));
		cmds.add(new CommandCreateElementFull2(Mode.WITH_MIX_PREFIX));
		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();
		cmds.add(factoryNoteCommand.createSingleLine());

		cmds.add(new CommandPackage());
		cmds.add(new CommandEndPackage());
		cmds.add(new CommandPackageEmpty());

		cmds.add(new CommandNamespace());
		cmds.add(new CommandStereotype());

		cmds.add(new CommandLinkClass(UmlDiagramType.CLASS));
		cmds.add(new CommandLinkLollipop(UmlDiagramType.CLASS));

		cmds.add(new CommandImport());

		final FactoryTipOnEntityCommand factoryTipOnEntityCommand = new FactoryTipOnEntityCommand(new RegexLeaf(
				"ENTITY", "(" + CommandCreateClass.CODE_NO_DOTDOT + "|[%g][^%g]+[%g])::([^%s]+)"));
		cmds.add(factoryTipOnEntityCommand.createMultiLine(true));
		cmds.add(factoryTipOnEntityCommand.createMultiLine(false));

		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexLeaf(
				"ENTITY", "(" + CommandCreateClass.CODE + "|[%g][^%g]+[%g])"));
		cmds.add(factoryNoteOnEntityCommand.createSingleLine());
		cmds.add(new CommandUrl());

		cmds.add(factoryNoteOnEntityCommand.createMultiLine(true));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(false));
		cmds.add(factoryNoteCommand.createMultiLine(false));

		cmds.add(new CommandCreateClassMultilines());
		cmds.add(new CommandCreateEntityObjectMultilines());

		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));

		cmds.add(new CommandDiamondAssociation());

		cmds.add(new CommandHideShowSpecificClass());

		cmds.add(new CommandNamespaceSeparator());

		return cmds;
	}

	@Override
	public String checkFinalError(AbstractPSystem sys) {
		if (sys instanceof NewpagedDiagram) {
			for (Diagram p : ((NewpagedDiagram) sys).getDiagrams()) {
				checkFinal((ClassDiagram) p);
			}
		} else {
			final ClassDiagram system = (ClassDiagram) sys;
			checkFinal(system);
		}
		return super.checkFinalError(sys);
	}

	private void checkFinal(final ClassDiagram system) {
		for (Link link : system.getLinks()) {
			final int len = link.getLength();
			if (len == 1) {
				for (Link link2 : system.getLinks()) {
					if (link2.sameConnections(link) && link2.getLength() != 1) {
						link2.setLength(1);
					}
				}
			}
		}
		system.applySingleStrategy();
	}

}
