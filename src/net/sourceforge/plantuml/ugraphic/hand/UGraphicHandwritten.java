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
package net.sourceforge.plantuml.ugraphic.hand;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;

public class UGraphicHandwritten implements UGraphic {

	private final UGraphic ug;

	public UGraphicHandwritten(UGraphic ug) {
		this.ug = ug;
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}

	public void draw(UShape shape) {
		// http://www.ufonts.com/fonts/felt-tip-roman.html
		// http://webdesignledger.com/freebies/20-amazing-free-handwritten-fonts-for-your-designs
		if (shape instanceof ULine) {
			drawHand((ULine) shape);
		} else if (shape instanceof URectangle) {
			drawHand((URectangle) shape);
		} else if (shape instanceof UPolygon) {
			drawHand((UPolygon) shape);
		} else if (shape instanceof UEllipse) {
			drawHand((UEllipse) shape);
		} else if (shape instanceof DotPath) {
			drawHand((DotPath) shape);
		} else if (shape instanceof UPath) {
			drawHand((UPath) shape);
		} else {
			ug.draw(shape);
		}
	}

	private void drawHand(UPath shape) {
		final UPathHand uline = new UPathHand(shape);
		ug.draw(uline.getHanddrawn());
	}

	private void drawHand(DotPath shape) {
		final UDotPathHand uline = new UDotPathHand(shape);
		ug.draw(uline.getHanddrawn());
	}

	private void drawHand(UPolygon shape) {
		final UPolygonHand hand = new UPolygonHand(shape);
		ug.draw(hand.getHanddrawn());
	}

	private void drawHand(URectangle shape) {
		final URectangleHand hand = new URectangleHand(shape);
		ug.draw(hand.getHanddrawn());
	}

	private void drawHand(ULine shape) {
		final ULineHand uline = new ULineHand(shape);
		ug.draw(uline.getHanddrawn());
	}

	private void drawHand(UEllipse shape) {
		final UEllipseHand uline = new UEllipseHand(shape);
		ug.draw(uline.getHanddrawn());
	}

	public UGraphic apply(UChange change) {
		return new UGraphicHandwritten(ug.apply(change));
	}

	public ColorMapper getColorMapper() {
		return ug.getColorMapper();
	}

	public void startUrl(Url url) {
		ug.startUrl(url);
	}

	public void closeAction() {
		ug.closeAction();
	}

	public void flushUg() {
		ug.flushUg();
	}

	public boolean isSpecialTxt() {
		return false;
	}

}
