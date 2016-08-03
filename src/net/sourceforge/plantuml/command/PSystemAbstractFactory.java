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
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;

public abstract class PSystemAbstractFactory implements PSystemFactory {

	private final DiagramType type;

	protected PSystemAbstractFactory(DiagramType type) {
		this.type = type;
	}

	final protected AbstractPSystem buildEmptyError(UmlSource source, LineLocation lineLocation) {
		final PSystemError result = new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR,
				"Empty description", 1, lineLocation), null);
		result.setSource(source);
		return result;
	}
	
	final protected PSystemError buildEmptyError(UmlSource source, String err, LineLocation lineLocation) {
		final PSystemError result = new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, err, 1, lineLocation), null);
		result.setSource(source);
		return result;
	}

	final public DiagramType getDiagramType() {
		return type;
	}

}
