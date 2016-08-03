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
package net.sourceforge.plantuml.hector2;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.hector2.continuity.Skeleton;
import net.sourceforge.plantuml.hector2.continuity.SkeletonBuilder;
import net.sourceforge.plantuml.hector2.graphic.Foo2;
import net.sourceforge.plantuml.hector2.layering.Layer;
import net.sourceforge.plantuml.hector2.layering.LayerFactory;
import net.sourceforge.plantuml.hector2.mpos.Distribution;
import net.sourceforge.plantuml.hector2.mpos.MutationLayer;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.ugraphic.UGraphic2;

public class CucaDiagramFileMakerHectorC1 implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

	public CucaDiagramFileMakerHectorC1(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		final SkeletonBuilder skeletonBuilder = new SkeletonBuilder();
		for (Link link : diagram.getLinks()) {
			skeletonBuilder.add(link);
		}
		final List<Skeleton> skeletons = skeletonBuilder.getSkeletons();
		if (skeletons.size() != 1) {
			throw new UnsupportedOperationException("size=" + skeletons.size());
		}
		final List<Layer> layers = new LayerFactory().getLayers(skeletons.get(0));
		// System.err.println("layers=" + layers);

		final Distribution distribution = new Distribution(layers);
		final double cost1 = distribution.cost(diagram.getLinks());
		System.err.println("cost1=" + cost1);

		final List<MutationLayer> mutations = distribution.getPossibleMutations();
		for (MutationLayer m : mutations) {
			System.err.println(m.toString());
			final Distribution muted = distribution.mute(m);
			final double cost2 = muted.cost(diagram.getLinks());
			System.err.println("cost2=" + cost2);
		}

		final Foo2 foo2 = new Foo2(distribution, diagram);

		final Dimension2D dimTotal = foo2.calculateDimension(TextBlockUtils.getDummyStringBounder());

		UGraphic2 ug = null; //fileFormatOption.createUGraphic(diagram.getColorMapper(), diagram.getDpiFactor(fileFormatOption),
				//dimTotal, null, false);
		foo2.drawU(ug);

//		ug.writeImageTOBEMOVED(os, null, diagram.getDpi(fileFormatOption));
//		return new ImageDataSimple(dimTotal);
		throw new UnsupportedOperationException();
	}

}
