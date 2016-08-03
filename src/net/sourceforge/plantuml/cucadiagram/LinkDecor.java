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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrowAndCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleConnect;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCross;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDiamond;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryParenthesis;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryPlus;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactorySquarre;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryTriangle;

public enum LinkDecor {

	NONE(2, false, 0), EXTENDS(30, false, 2), COMPOSITION(15, true, 1.3), AGREGATION(15, false, 1.3),

	ARROW(10, true, 0.5), ARROW_TRIANGLE(10, true, 0.8), ARROW_AND_CIRCLE(10, false, 0.5),

	CIRCLE(0, false, 0.5), CIRCLE_CONNECT(0, false, 0.5), PARENTHESIS(0, false, OptionFlags.USE_INTERFACE_EYE2 ? 0.5
			: 1.0), SQUARRE(0, false, 0.5),

	CIRCLE_CROSS(0, false, 0.5), PLUS(0, false, 1.5), SQUARRE_toberemoved(30, false, 0);

	private final double arrowSize;
	private final int margin;
	private final boolean fill;

	private LinkDecor(int margin, boolean fill, double arrowSize) {
		this.margin = margin;
		this.fill = fill;
		this.arrowSize = arrowSize;
	}

	public int getMargin() {
		return margin;
	}

	public boolean isFill() {
		return fill;
	}

	public double getArrowSize() {
		return arrowSize;
	}

	public ExtremityFactory getExtremityFactory() {
		if (this == LinkDecor.PLUS) {
			return new ExtremityFactoryPlus();
		} else if (this == LinkDecor.ARROW_TRIANGLE) {
			return new ExtremityFactoryTriangle();
		} else if (this == LinkDecor.CIRCLE_CROSS) {
			return new ExtremityFactoryCircleCross();
		} else if (this == LinkDecor.ARROW) {
			return new ExtremityFactoryArrow();
		} else if (this == LinkDecor.ARROW_AND_CIRCLE) {
			return new ExtremityFactoryArrowAndCircle();
		} else if (this == LinkDecor.AGREGATION) {
			return new ExtremityFactoryDiamond(false);
		} else if (this == LinkDecor.COMPOSITION) {
			return new ExtremityFactoryDiamond(true);
		} else if (this == LinkDecor.CIRCLE) {
			return new ExtremityFactoryCircle();
		} else if (this == LinkDecor.SQUARRE) {
			return new ExtremityFactorySquarre();
		} else if (this == LinkDecor.PARENTHESIS) {
			return new ExtremityFactoryParenthesis();
		} else if (this == LinkDecor.CIRCLE_CONNECT) {
			return new ExtremityFactoryCircleConnect();
		}

		return null;
	}

}
