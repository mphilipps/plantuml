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
package net.sourceforge.plantuml.sequencediagram.command;

import java.text.DecimalFormat;
import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandAutonumber extends SingleLineCommand<SequenceDiagram> {

	public CommandAutonumber() {
		super("(?i)^autonumber[%s]*(\\d+)?(?:[%s]+(\\d+))?(?:[%s]+[%g]([^%g]+)[%g])?[%s]*$");
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram sequenceDiagram, List<String> arg) {
		int start = 1;
		if (arg.get(0) != null) {
			start = Integer.parseInt(arg.get(0));
		}
		int inc = 1;
		if (arg.get(1) != null) {
			inc = Integer.parseInt(arg.get(1));
		}

		final String df = arg.get(2) == null ? "<b>0</b>" : arg.get(2);
		final DecimalFormat decimalFormat;
		try {
			decimalFormat = new DecimalFormat(df);
		} catch (IllegalArgumentException e) {
			return CommandExecutionResult.error("Error in pattern : " + df);
		}

		sequenceDiagram.autonumberGo(start, inc, decimalFormat);
		return CommandExecutionResult.ok();
	}
}
