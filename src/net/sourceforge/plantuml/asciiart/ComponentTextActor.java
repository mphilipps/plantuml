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
package net.sourceforge.plantuml.asciiart;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;
import net.sourceforge.plantuml.StringUtils;

public class ComponentTextActor extends AbstractComponentText {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;

	public ComponentTextActor(ComponentType type, Display stringsToDisplay, FileFormat fileFormat) {
		this.type = type;
		this.stringsToDisplay = stringsToDisplay;
		this.fileFormat = fileFormat;
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		// final int textWidth = StringUtils.getWidth(stringsToDisplay);
		final int height = (int) dimensionToUse.getHeight();
		charArea.fillRect(' ', 0, 0, width, height);

		final int xman = width / 2 - 1;
		if (type == ComponentType.ACTOR_HEAD) {
			charArea.drawStringsLR(stringsToDisplay.as(), 1, getStickManHeight());
			if (fileFormat == FileFormat.UTXT) {
				charArea.drawStickManUnicode(xman, 0);
			} else {
				charArea.drawStickMan(xman, 0);
			}
		} else if (type == ComponentType.ACTOR_TAIL) {
			charArea.drawStringsLR(stringsToDisplay.as(), 1, 0);
			if (fileFormat == FileFormat.UTXT) {
				charArea.drawStickManUnicode(xman, 1);
			} else {
				charArea.drawStickMan(xman, 1);
			}
		} else {
			assert false;
		}
	}

	private int getStickManHeight() {
		if (fileFormat == FileFormat.UTXT) {
			return UmlCharArea.STICKMAN_UNICODE_HEIGHT;
		}
		return UmlCharArea.STICKMAN_HEIGHT;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + getStickManHeight();
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return StringUtils.getWidth(stringsToDisplay) + 2;
	}

}
