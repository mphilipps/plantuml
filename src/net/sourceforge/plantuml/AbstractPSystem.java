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
package net.sourceforge.plantuml;

import java.util.List;
import java.util.Properties;

import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ProtectedCommand;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.Version;

public abstract class AbstractPSystem implements Diagram {

	private UmlSource source;

	private String getVersion() {
		final StringBuilder toAppend = new StringBuilder();
		toAppend.append("PlantUML version ");
		toAppend.append(Version.versionString());
		toAppend.append("(" + Version.compileTimeString() + ")\n");
		toAppend.append("(" + License.getCurrent() + " source distribution)\n");
		final Properties p = System.getProperties();
		toAppend.append(p.getProperty("java.runtime.name"));
		toAppend.append('\n');
		toAppend.append(p.getProperty("java.vm.name"));
		toAppend.append('\n');
		toAppend.append(p.getProperty("java.runtime.version"));
		toAppend.append('\n');
		toAppend.append(p.getProperty("os.name"));

		return toAppend.toString();
	}

	final public String getMetadata() {
		if (source == null) {
			return getVersion();
		}
		return source.getPlainString() + "\n" + getVersion();
	}

	final public UmlSource getSource() {
		return source;
	}

	final public void setSource(UmlSource source) {
		this.source = source;
	}

	public int getNbImages() {
		return 1;
	}

	public DisplayPositionned getTitle() {
		if (source == null) {
			return new DisplayPositionned(Display.empty(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);
		}
		return new DisplayPositionned(source.getTitle(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);
	}

	public String getWarningOrError() {
		return null;
	}

	public void makeDiagramReady() {
	}

	public boolean isOk() {
		return true;
	}

	public CommandExecutionResult executeCommand(Command cmd, BlocLines lines) {
		cmd = new ProtectedCommand(cmd);
		return cmd.execute(this, lines);
	}

	public boolean hasUrl() {
		return false;
	}

}
