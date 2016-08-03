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
import net.sourceforge.plantuml.ugraphic.CompressionTransform;
import net.sourceforge.plantuml.ugraphic.SlotFinder;
import net.sourceforge.plantuml.ugraphic.SlotSet;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicCompress;

public class TextBlockCompressed extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;

	public TextBlockCompressed(TextBlock textBlock) {
		this.textBlock = textBlock;
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final CompressionTransform compressionTransform = getCompressionTransform(stringBounder);
		textBlock.drawU(new UGraphicCompress(ug, compressionTransform));
	}

	private CompressionTransform getCompressionTransform(final StringBounder stringBounder) {
		final SlotFinder slotFinder = new SlotFinder(stringBounder);
		textBlock.drawU(slotFinder);
		final SlotSet ysSlotSet = slotFinder.getYSlotSet().reverse().smaller(5.0);
		final CompressionTransform compressionTransform = new CompressionTransform(ysSlotSet);
		return compressionTransform;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final CompressionTransform compressionTransform = getCompressionTransform(stringBounder);
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		return new Dimension2DDouble(dim.getWidth(), compressionTransform.transform(dim.getHeight()));
	}
}
