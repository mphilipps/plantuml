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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmbededDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UShape;

class EmbededSystemLine extends AbstractTextBlock implements Line {

	final private List<CharSequence2> lines2;

	public EmbededSystemLine(EmbededDiagram sys) {
		this.lines2 = sys.getLines().as2();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		try {
			final BufferedImage im = getImage();
			return new Dimension2DDouble(im.getWidth(), im.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Dimension2DDouble(42, 42);
	}

	public void drawU(UGraphic ug) {
		try {
			final BufferedImage im = getImage();
			final UShape image = new UImage(im);
			ug.draw(image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private BufferedImage getImage() throws IOException, InterruptedException {
		final Diagram system = getSystem();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		system.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
		os.close();
		final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		final BufferedImage im = ImageIO.read(is);
		is.close();
		return im;
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return HorizontalAlignment.LEFT;
	}

	private Diagram getSystem() throws IOException, InterruptedException {
		final BlockUml blockUml = new BlockUml(lines2, 0);
		return blockUml.getDiagram();

	}
}
