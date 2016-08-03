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
package net.sourceforge.plantuml.directdot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.CounterOutputStream;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.Graphviz;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.cucadiagram.dot.ProcessState;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UGraphicUtils;

public class PSystemDot extends AbstractPSystem {

	private final String data;

	public PSystemDot(String data) {
		this.data = data;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Dot)", getClass());
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final Graphviz graphviz = GraphvizUtils
				.create(null, data, StringUtils.goLowerCase(fileFormat.getFileFormat().name()));
		if (graphviz.illegalDotExe()) {
			final TextBlock result = GraphicStrings.createDefault(Arrays.asList("There is an issue with your Dot/Graphviz installation"), false);
			UGraphicUtils.writeImage(os, null, fileFormat, new ColorMapperIdentity(), HtmlColorUtils.WHITE, result);
			return new ImageDataSimple();
		}
		final CounterOutputStream counter = new CounterOutputStream(os);
		final ProcessState state = graphviz.createFile3(counter);
		if (state.differs(ProcessState.TERMINATED_OK())) {
			throw new IllegalStateException("Timeout1 " + state);
		}
		if (counter.getLength() == 0) {
			final TextBlock result = GraphicStrings.createDefault(Arrays.asList("Graphivz has crashed"), false);
			UGraphicUtils.writeImage(os, null, fileFormat, new ColorMapperIdentity(), HtmlColorUtils.WHITE, result);
		}

		return new ImageDataSimple();
	}
}
