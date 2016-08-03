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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek2;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CucaDiagramFileMakerHector implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

	public CucaDiagramFileMakerHector(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	final private Map<ILeaf, IEntityImage> images = new LinkedHashMap<ILeaf, IEntityImage>();

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		double singleWidth = 0;
		double singleHeight = 0;
		int nb = 0;
		for (ILeaf leaf : diagram.getLeafsvalues()) {
			final IEntityImage image = computeImage(leaf);
			final Dimension2D dim = TextBlockUtils.getDimension(image);
			if (dim.getWidth() > singleWidth) {
				singleWidth = dim.getWidth();
			}
			if (dim.getHeight() > singleHeight) {
				singleHeight = dim.getHeight();
			}
			images.put(leaf, image);
			nb++;
		}
		final double margin = 10;
		final Dimension2D dim = new Dimension2DDouble(2 * margin + nb * singleWidth, singleHeight + 2 * margin);
		UGraphic2 ug = null;// fileFormatOption.createUGraphic(diagram.getColorMapper(), diagram.getDpiFactor(fileFormatOption),
				// dim, null, false);
		ug = (UGraphic2) ug.apply(new UTranslate(margin, margin));

		double pos = 0;
		for (IEntityImage im : images.values()) {
			im.drawU(ug.apply(new UTranslate(pos, 0)));
			pos += singleWidth;
		}

//		ug.writeImageTOBEMOVED(os, null, diagram.getDpi(fileFormatOption));
//		return new ImageDataSimple(dim);
		throw new UnsupportedOperationException();
	}

	private IEntityImage computeImage(final ILeaf leaf) {
		final IEntityImage image = CucaDiagramFileMakerSvek2.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null, null, null);
		return image;
	}
}
