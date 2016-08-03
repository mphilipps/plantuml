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
package net.sourceforge.plantuml.syntax;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.preproc.Defines;

public class SyntaxChecker {

	public static SyntaxResult checkSyntax(List<String> source) {
		final StringBuilder sb = new StringBuilder();
		for (String s : source) {
			sb.append(s);
			sb.append("\n");
		}
		return checkSyntax(sb.toString());
	}

	public static SyntaxResult checkSyntax(String source) {
		OptionFlags.getInstance().setQuiet(true);
		final SyntaxResult result = new SyntaxResult();

		if (source.startsWith("@startuml\n") == false) {
			result.setError(true);
			result.setErrorLinePosition(0);
			result.addErrorText("No @startuml found");
			result.setSuggest(Arrays.asList("Did you mean:", "@startuml"));
			return result;
		}
		if (source.endsWith("@enduml\n") == false && source.endsWith("@enduml") == false) {
			result.setError(true);
			result.setErrorLinePosition(lastLineNumber(source));
			result.addErrorText("No @enduml found");
			result.setSuggest(Arrays.asList("Did you mean:", "@enduml"));
			return result;
		}
		final SourceStringReader sourceStringReader = new SourceStringReader(new Defines(), source,
				Collections.<String> emptyList());

		final List<BlockUml> blocks = sourceStringReader.getBlocks();
		if (blocks.size() == 0) {
			result.setError(true);
			result.setErrorLinePosition(lastLineNumber(source));
			result.addErrorText("No @enduml found");
			result.setSuggest(Arrays.asList("Did you mean:", "@enduml"));
			return result;
		}
		final Diagram system = blocks.get(0).getDiagram();
		result.setCmapData(system.hasUrl());
		if (system instanceof UmlDiagram) {
			result.setUmlDiagramType(((UmlDiagram) system).getUmlDiagramType());
			result.setDescription(system.getDescription().getDescription());
		} else if (system instanceof PSystemError) {
			result.setError(true);
			final PSystemError sys = (PSystemError) system;
			result.setErrorLinePosition(sys.getHigherErrorPosition());
			result.setLineLocation(sys.getLineLocation());
			for (ErrorUml er : sys.getErrorsUml()) {
				result.addErrorText(er.getError());
			}
			result.setSuggest(sys.getSuggest());
		} else {
			result.setDescription(system.getDescription().getDescription());
		}
		return result;
	}

	public static SyntaxResult checkSyntaxFair(String source) {
		final SyntaxResult result = new SyntaxResult();
		final SourceStringReader sourceStringReader = new SourceStringReader(new Defines(), source,
				Collections.<String> emptyList());

		final List<BlockUml> blocks = sourceStringReader.getBlocks();
		if (blocks.size() == 0) {
			result.setError(true);
			result.setErrorLinePosition(lastLineNumber(source));
			result.addErrorText("No @enduml found");
			result.setSuggest(Arrays.asList("Did you mean:", "@enduml"));
			return result;
		}

		final Diagram system = blocks.get(0).getDiagram();
		result.setCmapData(system.hasUrl());
		if (system instanceof UmlDiagram) {
			result.setUmlDiagramType(((UmlDiagram) system).getUmlDiagramType());
			result.setDescription(system.getDescription().getDescription());
		} else if (system instanceof PSystemError) {
			result.setError(true);
			final PSystemError sys = (PSystemError) system;
			result.setErrorLinePosition(sys.getHigherErrorPosition());
			result.setLineLocation(sys.getLineLocation());
			for (ErrorUml er : sys.getErrorsUml()) {
				result.addErrorText(er.getError());
			}
			result.setSystemError(sys);
			result.setSuggest(sys.getSuggest());
		} else {
			result.setDescription(system.getDescription().getDescription());
		}
		return result;
	}

	private static int lastLineNumber(String source) {
		int result = 0;
		for (int i = 0; i < source.length(); i++) {
			if (source.charAt(i) == '\n') {
				result++;
			}
		}
		return result;
	}
}
