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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;

public class OptionFlags {

	// static public final boolean PBBACK = false;
	// static public boolean GRAPHVIZCACHE = false;
	static public final boolean TRACE_DOT = false;

	static public boolean ALLOW_INCLUDE = true;

	static public final boolean USE_HECTOR = false;
	static public boolean ADD_NICE_FOR_DOT = false;
	static public final boolean STRICT_SELFMESSAGE_POSITION = true;

	// static public final boolean USE_IF_VERTICAL = true;
	static public final boolean FORCE_TEOZ = false;
	static public final boolean USE_INTERFACE_EYE1 = false;
	static public final boolean USE_INTERFACE_EYE2 = false;
	static public final boolean SWI2 = false;
	// static public final boolean USE_COMPOUND = false;
	static public final boolean OMEGA_CROSSING = false;
	// static public final boolean USE_JDOT = false;

	public void reset() {
		reset(false);
	}

	private void reset(boolean exit) {
		keepTmpFiles = false;
		verbose = false;
		metadata = false;
		word = false;
		systemExit = exit;
		GraphvizUtils.setDotExecutable(null);
		gui = false;
		quiet = false;
		checkDotError = false;
		printFonts = false;
		useSuggestEngine = true;
		// failOnError = false;
		encodesprite = false;
		// PIC_LINE = false;
	}

	public boolean useJavaInsteadOfDot() {
		return false;
	}

	private static final OptionFlags singleton = new OptionFlags();

	private boolean keepTmpFiles;
	private boolean verbose;
	private boolean metadata;
	private boolean word;
	private boolean systemExit;
	private boolean gui;
	private boolean quiet;
	private boolean checkDotError;
	private boolean printFonts;
	private boolean useSuggestEngine;
	// private boolean failOnError;
	private boolean encodesprite;
	private boolean overwrite;
	private File logData;

	private OptionFlags() {
		reset(true);
	}

	public static OptionFlags getInstance() {
		return singleton;
	}

	public synchronized final boolean isKeepTmpFiles() {
		return keepTmpFiles;
	}

	public synchronized final void setKeepTmpFiles(boolean keepTmpFiles) {
		this.keepTmpFiles = keepTmpFiles;
	}

	public final boolean isVerbose() {
		return verbose;
	}

	public final void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public final boolean isMetadata() {
		return metadata;
	}

	public final void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}

	public final boolean isWord() {
		return word;
	}

	public final void setWord(boolean word) {
		this.word = word;
	}

	public final boolean isSystemExit() {
		return systemExit;
	}

	public final void setSystemExit(boolean systemExit) {
		this.systemExit = systemExit;
	}

	public final boolean isGui() {
		return gui;
	}

	public final void setGui(boolean gui) {
		this.gui = gui;
	}

	public final boolean isQuiet() {
		return quiet;
	}

	public final void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public final boolean isCheckDotError() {
		return checkDotError;
	}

	public final void setCheckDotError(boolean checkDotError) {
		this.checkDotError = checkDotError;
	}

	private final AtomicBoolean logDataInitized = new AtomicBoolean(false);

	public void logData(File file, Diagram system) {
		final String warnOrError = system.getWarningOrError();
		if (warnOrError == null) {
			return;
		}
		synchronized (logDataInitized) {
			if (logData == null && logDataInitized.get() == false) {
				final String s = GraphvizUtils.getenvLogData();
				if (s != null) {
					setLogData(new File(s));
				}
				logDataInitized.set(true);
			}

			if (logData == null) {
				return;
			}
			// final PSystemError systemError = (PSystemError) system;
			PrintStream ps = null;
			try {
				ps = new PrintStream(new FileOutputStream(logData, true));
				ps.println("Start of " + file.getName());
				ps.println(warnOrError);
				ps.println("End of " + file.getName());
				ps.println();
			} catch (FileNotFoundException e) {
				Log.error("Cannot open " + logData);
				e.printStackTrace();
			} finally {
				if (ps != null) {
					ps.close();
				}
			}
		}
	}

	// public static void logErrorFile(final PSystemError systemError, PrintStream ps) {
	// ps.println(systemError.getDescription());
	// for (CharSequence t : systemError.getTitle()) {
	// ps.println(t);
	// }
	// systemError.print(ps);
	// for (String s : systemError.getSuggest()) {
	// ps.println(s);
	// }
	// }

	public final void setLogData(File logData) {
		this.logData = logData;
		logData.delete();
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(logData));
			ps.println();
		} catch (FileNotFoundException e) {
			Log.error("Cannot open " + logData);
			e.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public final boolean isPrintFonts() {
		return printFonts;
	}

	public final void setPrintFonts(boolean printFonts) {
		this.printFonts = printFonts;
	}

	public final boolean isUseSuggestEngine() {
		return useSuggestEngine;
	}

	public final void setUseSuggestEngine(boolean useSuggestEngine) {
		this.useSuggestEngine = useSuggestEngine;
	}

	// public final boolean isFailOnError() {
	// return failOnError;
	// }
	//
	// public final void setFailOnError(boolean failOnError) {
	// this.failOnError = failOnError;
	// }

	public final boolean isEncodesprite() {
		return encodesprite;
	}

	public final void setEncodesprite(boolean encodesprite) {
		this.encodesprite = encodesprite;
	}

	public final boolean isOverwrite() {
		return overwrite;
	}

	public final void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

}
