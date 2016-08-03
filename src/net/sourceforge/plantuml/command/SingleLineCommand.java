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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.core.Diagram;

public abstract class SingleLineCommand<S extends Diagram> implements Command<S> {

	private final Pattern pattern;

	public SingleLineCommand(String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.startsWith("(?i)^") == false || pattern.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern);
		}

		this.pattern = MyPattern.cmpile(pattern);
	}

	public String[] getDescription() {
		return new String[] { pattern.pattern() };
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
		final Matcher m = pattern.matcher(line);
		final boolean result = m.find();
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
		final List<String> arg = getSplit(line);
		if (arg == null) {
			return CommandExecutionResult.error("Cannot parse line " + line);
		}
		return executeArg(system, arg);
	}

	protected boolean isForbidden(String line) {
		return false;
	}

	protected abstract CommandExecutionResult executeArg(S system, List<String> arg);

	final public List<String> getSplit(String line) {
		return StringUtils.getSplit(pattern, line);
	}

}
