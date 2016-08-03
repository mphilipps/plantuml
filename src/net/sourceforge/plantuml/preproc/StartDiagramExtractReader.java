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
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.utils.StartUtils;

public class StartDiagramExtractReader implements ReadLine {

	private final ReadLine raw;
	private boolean finished = false;

	public StartDiagramExtractReader(File f, int num, String charset) throws IOException {
		this(getReadLine(f, charset), num, charset);
	}

	public StartDiagramExtractReader(URL url, int num, String charset) throws IOException {
		this(getReadLine(url, charset), num, charset);
	}

	private StartDiagramExtractReader(ReadLine raw, int num, String charset) throws IOException {
		if (num < 0) {
			throw new IllegalArgumentException();
		}
		this.raw = raw;
		CharSequence2 s = null;
		while ((s = raw.readLine()) != null) {
			if (StartUtils.isArobaseStartDiagram(s)) {
				if (num == 0) {
					return;
				}
				num--;
			}
		}
		finished = true;
	}

	private static ReadLine getReadLine(File f, String charset) throws IOException {

		if (charset == null) {
			Log.info("Using default charset");
			return new UncommentReadLine(new ReadLineReader(new FileReader(f), f.getAbsolutePath()));
		}
		Log.info("Using charset " + charset);
		return new UncommentReadLine(new ReadLineReader(new InputStreamReader(new FileInputStream(f), charset),
				f.getAbsolutePath()));
	}

	private static ReadLine getReadLine(URL url, String charset) throws IOException {

		if (charset == null) {
			Log.info("Using default charset");
			return new UncommentReadLine(new ReadLineReader(new InputStreamReader(url.openStream()), url.toString()));
		}
		Log.info("Using charset " + charset);
		return new UncommentReadLine(
				new ReadLineReader(new InputStreamReader(url.openStream(), charset), url.toString()));
	}

	static public boolean containsStartDiagram(File f, String charset) throws IOException {
		final ReadLine r = getReadLine(f, charset);
		return containsStartDiagram(r);
	}

	static public boolean containsStartDiagram(URL url, String charset) throws IOException {
		final ReadLine r = getReadLine(url, charset);
		return containsStartDiagram(r);
	}

	private static boolean containsStartDiagram(final ReadLine r) throws IOException {
		try {
			CharSequence2 s = null;
			while ((s = r.readLine()) != null) {
				if (StartUtils.isArobaseStartDiagram(s)) {
					return true;
				}
			}
		} finally {
			if (r != null) {
				r.close();
			}
		}
		return false;
	}

	public CharSequence2 readLine() throws IOException {
		if (finished) {
			return null;
		}
		final CharSequence2 result = raw.readLine();
		if (result != null && StartUtils.isArobaseEndDiagram(result)) {
			finished = true;
			return null;
		}
		return result;
	}

	public void close() throws IOException {
		raw.close();
	}

}
