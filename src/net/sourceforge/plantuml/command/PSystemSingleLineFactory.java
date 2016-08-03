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

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

public abstract class PSystemSingleLineFactory extends PSystemAbstractFactory {

	protected abstract AbstractPSystem executeLine(String line);

	protected PSystemSingleLineFactory() {
		super(DiagramType.UML);
	}

	final public Diagram createSystem(UmlSource source) {

		final IteratorCounter2 it = source.iterator2();
		if (source.isEmpty()) {
			return buildEmptyError(source, it.peek().getLocation());
		}

		final CharSequence2 startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine) == false) {
			throw new UnsupportedOperationException();
		}

		if (it.hasNext() == false) {
			return buildEmptyError(source, startLine.getLocation());
		}
		final CharSequence2 s = it.next();
		if (StartUtils.isArobaseEndDiagram(s)) {
			return buildEmptyError(source, s.getLocation());
		}
		final AbstractPSystem sys = executeLine(s.toString2());
		if (sys == null) {
			return new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?",
					it.currentNum() - 1, s.getLocation()), null);
		}
		sys.setSource(source);
		return sys;

	}

}
