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
package net.sourceforge.plantuml.project2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.StringUtils;

public class PSystemProject2 extends AbstractPSystem {

	private final Project2 project = new Project2();
	private final Color background = Color.WHITE;
	private final ColorMapper colorMapper = new ColorMapperIdentity();

	public int getNbImages() {
		return 1;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Project)", getClass());
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormatOption) throws IOException {
		final GanttDiagram2 diagram = new GanttDiagram2(project);
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			final BufferedImage im = createImage(diagram);
			PngIO.write(im, os, fileFormatOption.isWithMetadata() ? getMetadata() : null, 96);
		} else if (fileFormat == FileFormat.SVG) {
			final UGraphicSvg svg = new UGraphicSvg(colorMapper, StringUtils.getAsHtml(background), false, 1.0, fileFormatOption.getSvgLinkTarget());
			diagram.draw(svg, 0, 0);
			svg.createXml(os);
		} else if (fileFormat == FileFormat.EPS) {
			final UGraphicEps eps = new UGraphicEps(colorMapper, EpsStrategy.getDefault2());
			diagram.draw(eps, 0, 0);
			os.write(eps.getEPSCode().getBytes());
		} else if (fileFormat == FileFormat.EPS_TEXT) {
			final UGraphicEps eps = new UGraphicEps(colorMapper, EpsStrategy.WITH_MACRO_AND_TEXT);
			diagram.draw(eps, 0, 0);
			os.write(eps.getEPSCode().getBytes());
		} else {
			throw new UnsupportedOperationException();
		}
		return new ImageDataSimple();
	}

	private BufferedImage createImage(GanttDiagram2 diagram) {
		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, background);
		Graphics2D g2d = builder.getGraphics2D();
		UGraphicG2d ug = new UGraphicG2d(colorMapper, g2d, 1.0);

		final double height = diagram.getHeight(ug.getStringBounder());
		final double width = diagram.getWidth(ug.getStringBounder());

		g2d.dispose();

		builder = new EmptyImageBuilder(width, height, background);
		final BufferedImage im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();

		ug = new UGraphicG2d(colorMapper, g2d, 1.0);
		ug.setBufferedImage(im);
		diagram.draw(ug, 0, 0);
		g2d.dispose();
		return im;
	}

	public final Project2 getProject() {
		return project;
	}

}
