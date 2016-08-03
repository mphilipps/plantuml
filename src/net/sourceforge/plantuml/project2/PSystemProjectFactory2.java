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
package net.sourceforge.plantuml.project2;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandComment;
import net.sourceforge.plantuml.command.CommandMultilinesComment;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.project2.command.CommandAffectation;
import net.sourceforge.plantuml.project2.command.CommandCloseWeekDay;

public class PSystemProjectFactory2 extends UmlDiagramFactory {

	public PSystemProjectFactory2() {
		super(DiagramType.PROJECT);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandNope());
		cmds.add(new CommandComment());
		cmds.add(new CommandMultilinesComment());
		cmds.add(new CommandAffectation());
		cmds.add(new CommandCloseWeekDay());
		return cmds;
	}

	@Override
	public PSystemProject2 createEmptyDiagram() {
		return new PSystemProject2();
	}

}
