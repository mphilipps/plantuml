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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class FtileEmpty extends AbstractFtile {

	private final double width;
	private final double height;
	private final Swimlane swimlaneIn;
	private final Swimlane swimlaneOut;

	public FtileEmpty(boolean shadowing, double width, double height) {
		this(shadowing, width, height, null, null);
	}

	public FtileEmpty(boolean shadowing, double width, double height, Swimlane swimlaneIn, Swimlane swimlaneOut) {
		super(shadowing);
		this.width = width;
		this.height = height;
		this.swimlaneIn = swimlaneIn;
		this.swimlaneOut = swimlaneOut;

	}

	public FtileEmpty(boolean shadowing) {
		this(shadowing, 0, 0, null, null);
	}

	public FtileEmpty(boolean shadowing, Swimlane swimlane) {
		this(shadowing, 0, 0, swimlane, swimlane);
	}

	@Override
	public String toString() {
		return "FtileEmpty";
	}

	public void drawU(UGraphic ug) {
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		return new FtileGeometry(width, height, width / 2, 0, height);
	}

	public Swimlane getSwimlaneIn() {
		return swimlaneIn;
	}

	public Swimlane getSwimlaneOut() {
		return swimlaneOut;
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (swimlaneIn != null) {
			result.add(swimlaneIn);
		}
		if (swimlaneOut != null) {
			result.add(swimlaneOut);
		}
		return Collections.unmodifiableSet(result);
	}

}
