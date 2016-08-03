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

import net.sourceforge.plantuml.suggest.SuggestEngineResult;
import net.sourceforge.plantuml.suggest.SuggestEngineStatus;

public class ErrorUml {

	private final String error;
	private final int position;
	private final ErrorUmlType type;
	private SuggestEngineResult suggest;
	private final LineLocation lineLocation;

	public ErrorUml(ErrorUmlType type, String error, int position, LineLocation lineLocation) {
		if (error == null || type == null || StringUtils.isEmpty(error)) {
			throw new IllegalArgumentException();
		}
		this.error = error;
		this.type = type;
		this.position = position;
		this.lineLocation = lineLocation;
	}

	@Override
	public boolean equals(Object obj) {
		final ErrorUml this2 = (ErrorUml) obj;
		return this.type == this2.type && this.position == this2.position && this.error.equals(this2.error);
	}

	@Override
	public int hashCode() {
		return error.hashCode() + type.hashCode() + position + (suggest == null ? 0 : suggest.hashCode());
	}

	@Override
	public String toString() {
		return type.toString() + " " + position + " " + error + " " + suggest;
	}

	public final String getError() {
		return error;
	}

	public final ErrorUmlType getType() {
		return type;
	}

	public int getPosition() {
		return position;
	}

	public LineLocation getLineLocation() {
		return lineLocation;
	}

	public final SuggestEngineResult getSuggest() {
		return suggest;
	}

	public final boolean hasSuggest() {
		return suggest != null && suggest.getStatus() == SuggestEngineStatus.ONE_SUGGESTION;
	}

	public void setSuggest(SuggestEngineResult suggest) {
		this.suggest = suggest;
	}


}
