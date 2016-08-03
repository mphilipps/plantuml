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

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandImport extends SingleLineCommand<ClassDiagram> {

	public CommandImport() {
		super("(?i)^import[%s]+[%g]?([^%g]+)[%g]?$");
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram classDiagram, List<String> arg) {
		final String arg0 = arg.get(0);
		try {
			final File f = FileSystem.getInstance().getFile(arg0);

			if (f.isFile()) {
				includeSimpleFile(classDiagram, f);
			} else if (f.isDirectory()) {
				includeDirectory(classDiagram, f);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return CommandExecutionResult.error("IO error " + e);
		}
		return CommandExecutionResult.ok();
	}

	private void includeDirectory(ClassDiagram classDiagram, File dir) throws IOException {
		for (File f : dir.listFiles()) {
			includeSimpleFile(classDiagram, f);
		}

	}

	private void includeSimpleFile(ClassDiagram classDiagram, File f) throws IOException {
		if (StringUtils.goLowerCase(f.getName()).endsWith(".java")) {
			includeFileJava(classDiagram, f);
		}
		// if (f.getName().goLowerCase().endsWith(".sql")) {
		// includeFileSql(f);
		// }
	}

	private void includeFileJava(ClassDiagram classDiagram, final File f) throws IOException {
		final JavaFile javaFile = new JavaFile(f);
		for (JavaClass cl : javaFile.getJavaClasses()) {
			final Code name = Code.of(cl.getName());
			final IEntity ent1 = classDiagram.getOrCreateLeaf(name, cl.getType(), null);

			for (String p : cl.getParents()) {
				final IEntity ent2 = classDiagram.getOrCreateLeaf(Code.of(p), cl.getParentType(), null);
				final Link link = new Link(ent2, ent1, new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS), Display.NULL, 2);
				classDiagram.addLink(link);
			}
		}
	}

	// private void includeFileSql(final File f) throws IOException {
	// new SqlImporter(getSystem(), f).process();
	// }

}
