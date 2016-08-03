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

import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.core.Diagram;

public abstract class SingleLineCommand2<S extends Diagram> implements Command<S> {

	private final RegexConcat pattern;

	public SingleLineCommand2(RegexConcat pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.getPattern().startsWith("^") == false || pattern.getPattern().endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern.getPattern());
		}

		this.pattern = pattern;
	}

	public String[] getDescription() {
		return new String[] { pattern.getPattern() };
	}

	final public CommandControl isValid(BlocLines lines) {
		if (lines.size() != 1) {
			return CommandControl.NOT_OK;
		}
		lines = lines.removeInnerComments();
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		final String line = StringUtils.trin(lines.getFirst499());
		final boolean result = pattern.match(line);
		if (result) {
			actionIfCommandValid();
		}
		return result ? CommandControl.OK : CommandControl.NOT_OK;
	}

	protected boolean isCommandForbidden() {
		return false;
	}

	protected void actionIfCommandValid() {
	}

	public final CommandExecutionResult execute(S system, BlocLines lines) {
		if (lines.size() != 1) {
			throw new IllegalArgumentException();
		}
		lines = lines.removeInnerComments();
		final String line = StringUtils.trin(lines.getFirst499());
		if (isForbidden(line)) {
			return CommandExecutionResult.error("Forbidden line " + line);
		}

		final RegexResult arg = pattern.matcher(line);
		if (arg == null) {
			return CommandExecutionResult.error("Cannot parse line " + line);
		}
		if (system instanceof PSystemError) {
			return CommandExecutionResult.error("PSystemError cannot be cast");
		}
		// System.err.println("lines="+lines);
		// System.err.println("pattern="+pattern.getPattern());
		return executeArg(system, arg);
	}

	protected boolean isForbidden(CharSequence line) {
		return false;
	}

	protected abstract CommandExecutionResult executeArg(S system, RegexResult arg);

}
