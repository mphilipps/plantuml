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
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public class SlotFinder implements UGraphic {
	
	public boolean isSpecialTxt() {
		return false;
	}

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new SlotFinder(stringBounder, yslot, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke) {
			return new SlotFinder(this);
		} else if (change instanceof UChangeBackColor) {
			return new SlotFinder(this);
		} else if (change instanceof UChangeColor) {
			return new SlotFinder(this);
		}
		throw new UnsupportedOperationException();
	}

	private final SlotSet yslot;
	private final StringBounder stringBounder;
	private final UTranslate translate;

	public SlotFinder(StringBounder stringBounder) {
		this(stringBounder, new SlotSet(), new UTranslate());
	}

	private SlotFinder(StringBounder stringBounder, SlotSet yslot, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.yslot = yslot;
		this.translate = translate;
	}

	private SlotFinder(SlotFinder other) {
		this(other.stringBounder, other.yslot, other.translate);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (shape instanceof URectangle) {
			drawRectangle(x, y, (URectangle) shape);
		} else if (shape instanceof UPolygon) {
			drawPolygon(x, y, (UPolygon) shape);
		} else if (shape instanceof UEllipse) {
			drawEllipse(x, y, (UEllipse) shape);
		} else if (shape instanceof UText) {
			drawText(x, y, (UText) shape);
		} else if (shape instanceof UEmpty) {
			drawEmpty(x, y, (UEmpty) shape);
		}
	}

	private void drawEmpty(double x, double y, UEmpty shape) {
		yslot.addSlot(y, y + shape.getHeight());
	}

	private void drawText(double x, double y, UText shape) {
		final TextLimitFinder finder = new TextLimitFinder(stringBounder, false);
		finder.apply(new UTranslate(x, y)).draw(shape);
		yslot.addSlot(finder.getMinY(), finder.getMaxY());
	}

	private void drawEllipse(double x, double y, UEllipse shape) {
		yslot.addSlot(y, y + shape.getHeight());
	}

	private void drawPolygon(double x, double y, UPolygon shape) {
		yslot.addSlot(y + shape.getMinY(), y + shape.getMaxY());
	}

	private void drawRectangle(double x, double y, URectangle shape) {
		yslot.addSlot(y, y + shape.getHeight());
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public SlotSet getYSlotSet() {
		return yslot;
	}

	public void flushUg() {
	}

}
