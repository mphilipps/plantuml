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
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;

public class CommandPragma extends SingleLineCommand<UmlDiagram> {

	public CommandPragma() {
		super("(?i)^!pragma[%s]+([A-Za-z_][A-Za-z_0-9]*)(?:[%s]+(.*))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram system, List<String> arg) {
		final String name = StringUtils.goLowerCase(arg.get(0));
		final String value = arg.get(1);
		system.getPragma().define(name, value);
		if (name.equalsIgnoreCase("graphviz_dot") && value.equalsIgnoreCase("jdot")) {
			system.setUseJDot(true);
		} else if (name.equalsIgnoreCase("graphviz_dot")) {
			system.setDotExecutable(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value));
		}
		return CommandExecutionResult.ok();
	}

}
