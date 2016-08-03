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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public abstract class AbstractComponentRoseArrow extends AbstractTextualComponent implements ArrowComponent {

	private final int arrowDeltaX = 10;
	private final int arrowDeltaY = 4;
	private final HtmlColor foregroundColor;
	private final ArrowConfiguration arrowConfiguration;

	public AbstractComponentRoseArrow(HtmlColor foregroundColor, FontConfiguration font, Display stringsToDisplay,
			ArrowConfiguration arrowConfiguration, ISkinSimple spriteContainer, HorizontalAlignment textHorizontalAlignment,
			double maxMessageSize) {
		super(stringsToDisplay, font, textHorizontalAlignment, 7, 7, 1, spriteContainer,
				maxMessageSize, false, null, null);
		this.arrowConfiguration = arrowConfiguration;
		this.foregroundColor = foregroundColor;
	}

	protected final HtmlColor getForegroundColor() {
		return foregroundColor;
	}

	final protected int getArrowDeltaX() {
		return arrowDeltaX;
	}

	final protected int getArrowDeltaY() {
		return arrowDeltaY;
	}

	@Override
	public final double getPaddingY() {
		return 4;
	}

	public final ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

}
