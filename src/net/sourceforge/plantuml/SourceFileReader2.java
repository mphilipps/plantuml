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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;

public class SourceFileReader2 implements ISourceFileReader {

	private final File file;
	private final File outputFile;

	private final BlockUmlBuilder builder;
	private FileFormatOption fileFormatOption;

	public SourceFileReader2(Defines defines, final File file, File outputFile, List<String> config, String charset,
			FileFormatOption fileFormatOption) throws IOException {
		this.file = file;
		this.fileFormatOption = fileFormatOption;
		this.outputFile = outputFile;
		if (file.exists() == false) {
			throw new IllegalArgumentException();
		}
		FileSystem.getInstance().setCurrentDir(file.getAbsoluteFile().getParentFile());

		builder = new BlockUmlBuilder(config, charset, defines, getReader(charset), file.getAbsoluteFile()
				.getParentFile(), file.getAbsolutePath());
	}

	public boolean hasError() {
		for (final BlockUml b : builder.getBlockUmls()) {
			if (b.getDiagram() instanceof PSystemError) {
				return true;
			}
		}
		return false;
	}

	public List<GeneratedImage> getGeneratedImages() throws IOException {
		Log.info("Reading file: " + file);

		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();

		for (BlockUml blockUml : builder.getBlockUmls()) {
			final File suggested = outputFile;

			final Diagram system = blockUml.getDiagram();
			OptionFlags.getInstance().logData(file, system);

			for (File f : PSystemUtils.exportDiagrams(system, suggested, fileFormatOption)) {
				final String desc = "[" + file.getName() + "] " + system.getDescription();
				final GeneratedImage generatedImage = new GeneratedImageImpl(f, desc, blockUml);
				result.add(generatedImage);
			}

		}

		Log.info("Number of image(s): " + result.size());

		return Collections.unmodifiableList(result);
	}

	public List<String> getEncodedUrl() throws IOException {
		final List<String> result = new ArrayList<String>();
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
		for (BlockUml blockUml : builder.getBlockUmls()) {
			final String source = blockUml.getDiagram().getSource().getPlainString();
			final String encoded = transcoder.encode(source);
			result.add(encoded);
		}
		return Collections.unmodifiableList(result);
	}

	private Reader getReader(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		if (charset == null) {
			Log.info("Using default charset");
			return new InputStreamReader(new FileInputStream(file));
		}
		Log.info("Using charset " + charset);
		return new InputStreamReader(new FileInputStream(file), charset);
	}

	public final void setFileFormatOption(FileFormatOption fileFormatOption) {
		this.fileFormatOption = fileFormatOption;
	}

	public final Set<FileWithSuffix> getIncludedFiles2() {
		return builder.getIncludedFiles();
	}

}
