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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TextBlockSpotted extends TextBlockSimple {

	private final CircledCharacter circledCharacter;

	public TextBlockSpotted(CircledCharacter circledCharacter, Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer) {
		super(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0);
		this.circledCharacter = circledCharacter;
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double widthCircledCharacter = getCircledCharacterWithAndMargin(stringBounder);
		final double heightCircledCharacter = circledCharacter.getPreferredHeight(stringBounder);

		final Dimension2D dim = super.calculateDimension(stringBounder);
		return new Dimension2DDouble(dim.getWidth() + widthCircledCharacter, Math.max(heightCircledCharacter,
				dim.getHeight()));
	}

	private double getCircledCharacterWithAndMargin(StringBounder stringBounder) {
		return circledCharacter.getPreferredWidth(stringBounder) + 6.0;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		circledCharacter.drawU(ug);

		final double widthCircledCharacter = getCircledCharacterWithAndMargin(stringBounder);

		super.drawU(ug.apply(new UTranslate(widthCircledCharacter, 0)));
	}


}
