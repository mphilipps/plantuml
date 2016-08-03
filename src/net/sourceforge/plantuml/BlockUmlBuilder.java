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
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.Preprocessor;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.utils.StartUtils;

final public class BlockUmlBuilder {

	private final List<BlockUml> blocks = new ArrayList<BlockUml>();
	private Set<FileWithSuffix> usedFiles = new HashSet<FileWithSuffix>();
	private final UncommentReadLine reader2;

	public BlockUmlBuilder(List<String> config, String charset, Defines defines, Reader reader, File newCurrentDir,
			String desc) throws IOException {
		Preprocessor includer = null;
		try {
			reader2 = new UncommentReadLine(new ReadLineReader(reader, desc));
			includer = new Preprocessor(reader2, charset, defines, newCurrentDir);
			init(includer, config);
		} finally {
			if (includer != null) {
				includer.close();
				usedFiles = includer.getFilesUsed();
			}
		}
	}

	public BlockUmlBuilder(List<String> config, String charset, Defines defines, Reader reader) throws IOException {
		this(config, charset, defines, reader, null, null);
	}

	private void init(Preprocessor includer, List<String> config) throws IOException {
		CharSequence2 s = null;
		List<CharSequence2> current2 = null;
		boolean paused = false;
		int startLine = 0;
		while ((s = includer.readLine()) != null) {
			if (StartUtils.isArobaseStartDiagram(s)) {
				current2 = new ArrayList<CharSequence2>();
				paused = false;
				startLine = includer.getLineNumber();
			}
			if (StartUtils.isArobasePauseDiagram(s)) {
				paused = true;
				reader2.setPaused(true);
			}
			if (current2 != null && paused == false) {
				current2.add(s);
			} else if (paused) {
				final CharSequence2 append = StartUtils.getPossibleAppend(s);
				if (append != null) {
					current2.add(append);
				}
			}

			if (StartUtils.isArobaseUnpauseDiagram(s)) {
				paused = false;
				reader2.setPaused(false);
			}
			if (StartUtils.isArobaseEndDiagram(s) && current2 != null) {
				current2.addAll(1, convert(config, s.getLocation()));
				blocks.add(new BlockUml(current2, startLine));
				current2 = null;
				reader2.setPaused(false);
			}
		}
	}

	private Collection<CharSequence2> convert(List<String> config, LineLocation location) {
		final List<CharSequence2> result = new ArrayList<CharSequence2>();
		for (String s : config) {
			result.add(new CharSequence2Impl(s, location));
		}
		return result;
	}

	public List<BlockUml> getBlockUmls() {
		return Collections.unmodifiableList(blocks);
	}

	public final Set<FileWithSuffix> getIncludedFiles() {
		return Collections.unmodifiableSet(usedFiles);
	}

	/*
	 * private List<String> getStrings(Reader reader) throws IOException { final List<String> result = new
	 * ArrayList<String>(); Preprocessor includer = null; try { includer = new Preprocessor(reader, defines); String s =
	 * null; while ((s = includer.readLine()) != null) { result.add(s); } } finally { if (includer != null) {
	 * includer.close(); } } return Collections.unmodifiableList(result); }
	 */
}
