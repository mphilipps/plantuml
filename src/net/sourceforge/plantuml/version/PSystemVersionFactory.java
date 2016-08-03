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
package net.sourceforge.plantuml.version;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;

public class PSystemVersionFactory extends PSystemSingleLineFactory {

	@Override
	protected AbstractPSystem executeLine(String line) {
		try {
			if (line.matches("(?i)^(authors?|about)\\s*$")) {
				return PSystemVersion.createShowAuthors();
			}
			if (line.matches("(?i)^version\\s*$")) {
				return PSystemVersion.createShowVersion();
			}
			if (line.matches("(?i)^testdot\\s*$")) {
				return PSystemVersion.createTestDot();
			}
			if (line.matches("(?i)^checkversion\\s*$")) {
				return PSystemVersion.createCheckVersions(null, null);
			}
			final Pattern p1 = Pattern.compile("(?i)^checkversion\\(proxy=([\\w.]+),port=(\\d+)\\)$");
			final Matcher m1 = p1.matcher(line);
			if (m1.matches()) {
				final String host = m1.group(1);
				final String port = m1.group(2);
				return PSystemVersion.createCheckVersions(host, port);
			}
			final Pattern p2 = Pattern.compile("(?i)^checkversion\\(proxy=([\\w.]+)\\)$");
			final Matcher m2 = p2.matcher(line);
			if (m2.matches()) {
				final String host = m2.group(1);
				final String port = "80";
				return PSystemVersion.createCheckVersions(host, port);
			}
		} catch (IOException e) {
			Log.error("Error " + e);

		}
		return null;
	}

}
