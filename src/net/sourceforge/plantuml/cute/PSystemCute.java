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
package net.sourceforge.plantuml.cute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class PSystemCute extends AbstractPSystem {

	// private final List<Positionned> shapes = new ArrayList<Positionned>();
	// private final Map<String, Group> groups = new HashMap<String, Group>();
	private final Group root = Group.createRoot();
	private Group currentGroup = root;

	public PSystemCute() {
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Cute)", getClass());
	}

	public void doCommandLine(String line) {
		line = StringUtils.trin(line);
		if (line.length()==0 || line.startsWith("'")) {
			return;
		}
		if (line.startsWith("group ")) {
			final StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			final String groupName = st.nextToken();
			currentGroup = currentGroup.createChild(groupName);
		} else if (line.startsWith("}")) {
			currentGroup = currentGroup.getParent();
		} else {
			final Positionned shape = new CuteShapeFactory(currentGroup.getChildren()).createCuteShapePositionned(line);
			// if (currentGroup == null) {
			// shapes.add(shape);
			// } else {
			currentGroup.add(shape);
			// }
		}
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final ImageBuilder builder = new ImageBuilder(new ColorMapperIdentity(), 1.0, null, null, null, 10, 10, null, false);
		builder.setUDrawable(root);
		return builder.writeImageTOBEMOVED(fileFormat, os);
	}
}
