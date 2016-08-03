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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class SourceStringReader {

	final private List<BlockUml> blocks;

	public SourceStringReader(String source) {
		this(new Defines(), source, Collections.<String> emptyList());
	}

	public SourceStringReader(String source, String charset) {
		this(new Defines(), source, "UTF-8", Collections.<String> emptyList());
	}

	public SourceStringReader(Defines defines, String source, List<String> config) {
		this(defines, source, "UTF-8", config);
	}

	public SourceStringReader(Defines defines, String source, String charset, List<String> config) {
		// WARNING GLOBAL LOCK HERE
		synchronized (SourceStringReader.class) {
			try {
				final BlockUmlBuilder builder = new BlockUmlBuilder(config, charset, defines, new StringReader(source));
				this.blocks = builder.getBlockUmls();
			} catch (IOException e) {
				Log.error("error " + e);
				throw new IllegalStateException(e);
			}
		}
	}

	public String generateImage(OutputStream os) throws IOException {
		return generateImage(os, 0);
	}

	public String generateImage(File f) throws IOException {
		final OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
		final String result = generateImage(os, 0);
		os.close();
		return result;
	}

	public String generateImage(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		return generateImage(os, 0, fileFormatOption);
	}

	public String generateImage(OutputStream os, int numImage) throws IOException {
		return generateImage(os, numImage, new FileFormatOption(FileFormat.PNG));
	}

	public String generateImage(OutputStream os, int numImage, FileFormatOption fileFormatOption) throws IOException {
		if (blocks.size() == 0) {
			noStartumlFound(os, fileFormatOption);
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				// final CMapData cmap = new CMapData();
				final ImageData imageData = system.exportDiagram(os, numImage, fileFormatOption);
				if (imageData.containsCMapData()) {
					return system.getDescription().getDescription() + "\n" + imageData.getCMapData("plantuml");
				}
				return system.getDescription().getDescription();
			}
			numImage -= nbInSystem;
		}
		Log.error("numImage is too big = " + numImage);
		return null;

	}

	private void noStartumlFound(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		final GraphicStrings error = GraphicStrings.createDefault(Arrays.asList("No @startuml found"),
				fileFormatOption.isUseRedForError());
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, error.getBackcolor(), null,
				null, 0, 0, null, false);
		imageBuilder.setUDrawable(error);
		imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);
	}

	public DiagramDescription generateDiagramDescription(OutputStream os) throws IOException {
		return generateDiagramDescription(os, 0);
	}

	public DiagramDescription generateDiagramDescription(File f) throws IOException {
		final OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
		final DiagramDescription result = generateDiagramDescription(os, 0);
		os.close();
		return result;
	}

	public DiagramDescription generateDiagramDescription(OutputStream os, FileFormatOption fileFormatOption)
			throws IOException {
		return generateDiagramDescription(os, 0, fileFormatOption);
	}

	public DiagramDescription generateDiagramDescription(OutputStream os, int numImage) throws IOException {
		return generateDiagramDescription(os, numImage, new FileFormatOption(FileFormat.PNG));
	}

	public DiagramDescription generateDiagramDescription(OutputStream os, int numImage,
			FileFormatOption fileFormatOption) throws IOException {
		if (blocks.size() == 0) {
			noStartumlFound(os, fileFormatOption);
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				// final CMapData cmap = new CMapData();
				final ImageData imageData = system.exportDiagram(os, numImage, fileFormatOption);
				if (imageData.containsCMapData()) {
					return ((DiagramDescriptionImpl) system.getDescription()).withCMapData(imageData
							.getCMapData("plantuml"));
				}
				return system.getDescription();
			}
			numImage -= nbInSystem;
		}
		Log.error("numImage is too big = " + numImage);
		return null;

	}

	public final List<BlockUml> getBlocks() {
		return Collections.unmodifiableList(blocks);
	}

}
