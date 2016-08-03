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

import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class SkinParamBackcoloredReference extends SkinParamDelegator {

	final private HtmlColor sequenceReferenceHeaderBackground;
	final private HtmlColor sequenceReferenceBackground;

	public SkinParamBackcoloredReference(ISkinParam skinParam, HtmlColor sequenceReferenceHeaderBackground,
			HtmlColor sequenceReferenceBackground) {
		super(skinParam);
		this.sequenceReferenceBackground = sequenceReferenceBackground;
		this.sequenceReferenceHeaderBackground = sequenceReferenceHeaderBackground;
	}

	@Override
	public HtmlColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		if (param == ColorParam.sequenceReferenceHeaderBackground && sequenceReferenceHeaderBackground != null) {
			return sequenceReferenceHeaderBackground;
		}
		if (param == ColorParam.sequenceReferenceBackground && sequenceReferenceBackground != null) {
			return sequenceReferenceBackground;
		}
		return super.getHtmlColor(param, stereotype, clickable);
	}

}
