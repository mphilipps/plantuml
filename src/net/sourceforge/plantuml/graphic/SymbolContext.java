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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class SymbolContext {

	private final HtmlColor backColor;
	private final HtmlColor foreColor;
	private final UStroke stroke;
	private final boolean shadowing;
	private final double deltaShadow;

	private SymbolContext(HtmlColor backColor, HtmlColor foreColor, UStroke stroke, boolean shadowing,
			double deltaShadow) {
		this.backColor = backColor;
		this.foreColor = foreColor;
		this.stroke = stroke;
		this.shadowing = shadowing;
		this.deltaShadow = deltaShadow;
		// if (backColor instanceof HtmlColorTransparent) {
		// throw new UnsupportedOperationException();
		// }
	}

	@Override
	public String toString() {
		return super.toString() + " backColor=" + backColor + " foreColor=" + foreColor;
	}

	final public UGraphic apply(UGraphic ug) {
		return applyStroke(applyColors(ug));
	}

	public UGraphic applyColors(UGraphic ug) {
		return ug.apply(new UChangeColor(foreColor)).apply(new UChangeBackColor(backColor));
	}

	public UGraphic applyStroke(UGraphic ug) {
		return ug.apply(stroke);
	}

	public SymbolContext(HtmlColor backColor, HtmlColor foreColor) {
		this(backColor, foreColor, new UStroke(), false, 0);
	}

	public SymbolContext withShadow(boolean newShadow) {
		return new SymbolContext(backColor, foreColor, stroke, newShadow, deltaShadow);
	}

	public SymbolContext withDeltaShadow(double deltaShadow) {
		return new SymbolContext(backColor, foreColor, stroke, shadowing, deltaShadow);
	}

	public SymbolContext withStroke(UStroke newStroke) {
		return new SymbolContext(backColor, foreColor, newStroke, shadowing, deltaShadow);
	}

	public SymbolContext withBackColor(HtmlColor backColor) {
		return new SymbolContext(backColor, foreColor, stroke, shadowing, deltaShadow);
	}

	public HtmlColor getBackColor() {
		return backColor;
	}

	public HtmlColor getForeColor() {
		return foreColor;
	}

	public UStroke getStroke() {
		return stroke;
	}

	public boolean isShadowing() {
		return shadowing || deltaShadow > 0;
	}

	public double getDeltaShadow() {
		return deltaShadow;
	}

}
