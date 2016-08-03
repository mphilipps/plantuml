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
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierActivity;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramSimplifierState;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UFont;

public final class CucaDiagramFileMakerSvek implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

	static private final StringBounder stringBounder;

	static {
		stringBounder = StringBounderUtils.asStringBounder();
	}

	public CucaDiagramFileMakerSvek(CucaDiagram diagram) throws IOException {
		this.diagram = diagram;
	}

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		try {
			return createFileInternal(os, dotStrings, fileFormatOption);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	private CucaDiagramFileMakerSvek2 buildCucaDiagramFileMakerSvek2(DotMode dotMode) {
		final DotData dotData = new DotData(diagram.getEntityFactory().getRootGroup(), getOrderedLinks(),
				diagram.getLeafsvalues(), diagram.getUmlDiagramType(), diagram.getSkinParam(), diagram, diagram,
				diagram.getColorMapper(), diagram.getEntityFactory(), diagram.isHideEmptyDescriptionForState(),
				dotMode, diagram.getNamespaceSeparator(), diagram.getPragma());
		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData, diagram.getEntityFactory(),
				diagram.getSource(), diagram.getPragma());
		return svek2;

	}

	private ImageData createFileInternal(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			new CucaDiagramSimplifierActivity(diagram, dotStrings);
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			new CucaDiagramSimplifierState(diagram, dotStrings);
		}

		CucaDiagramFileMakerSvek2 svek2 = buildCucaDiagramFileMakerSvek2(DotMode.NORMAL);
		TextBlockBackcolored result = svek2.createFile(diagram.getDotStringSkek());
		if (result instanceof GraphvizCrash) {
			svek2 = buildCucaDiagramFileMakerSvek2(DotMode.NO_LEFT_RIGHT);
			result = svek2.createFile(diagram.getDotStringSkek());
		}
		result = new AnnotatedWorker(diagram, diagram.getSkinParam()).addAdd(result);

		final String widthwarning = diagram.getSkinParam().getValue("widthwarning");
		if (widthwarning != null && widthwarning.matches("\\d+")) {
			this.warningOrError = svek2.getBibliotekon().getWarningOrError(Integer.parseInt(widthwarning));
		} else {
			this.warningOrError = null;
		}
		final Dimension2D dim = result.calculateDimension(stringBounder);
		final double scale = getScale(fileFormatOption, dim);

		final ImageBuilder imageBuilder = new ImageBuilder(diagram.getSkinParam().getColorMapper(), scale,
				result.getBackcolor(), fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null,
				warningOrError, 0, 10, diagram.getAnimation(), diagram.getSkinParam().handwritten());
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);

	}

	private List<Link> getOrderedLinks() {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : diagram.getLinks()) {
			addLinkNew(result, l);
		}
		return result;
	}

	private void addLinkNew(List<Link> result, Link link) {
		for (int i = 0; i < result.size(); i++) {
			final Link other = result.get(i);
			if (other.sameConnections(link)) {
				while (i < result.size() && result.get(i).sameConnections(link)) {
					i++;
				}
				if (i == result.size()) {
					result.add(link);
				} else {
					result.add(i, link);
				}
				return;
			}
		}
		result.add(link);
	}

	private String warningOrError;

	private String getWarningOrError() {
		return warningOrError;
	}

	private final UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(null, false, fontParam);
	}

	private final HtmlColor getFontColor(FontParam fontParam, Stereotype stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFontHtmlColor(stereo, fontParam);
	}

	private double getScale(FileFormatOption fileFormatOption, final Dimension2D dim) {
		final double scale;
		final Scale diagScale = diagram.getScale();
		if (diagScale == null) {
			scale = diagram.getDpiFactor(fileFormatOption);
		} else {
			scale = diagScale.getScale(dim.getWidth(), dim.getHeight());
		}
		return scale;
	}

}
