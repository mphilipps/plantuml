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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class CommandHideShowSpecificClass extends SingleLineCommand2<CucaDiagram> {

	public CommandHideShowSpecificClass() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("COMMAND", "(hide|show)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("CODE", "(" + CommandCreateClass.CODE + ")"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(CucaDiagram diagram, RegexResult arg) {

		final String codeString = arg.get("CODE", 0);
		if (codeString.equals("class")) {
			diagram.hideOrShow(LeafType.CLASS, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		} else if (codeString.equals("interface")) {
			diagram.hideOrShow(LeafType.INTERFACE, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		} else {
			final Code code = Code.of(codeString);
			final ILeaf leaf = diagram.getEntityFactory().getLeafs().get(code);
			if (leaf == null) {
				return CommandExecutionResult.error("Class does not exist : " + code.getFullName());
			}
			diagram.hideOrShow(leaf, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		}
		return CommandExecutionResult.ok();
	}
}
