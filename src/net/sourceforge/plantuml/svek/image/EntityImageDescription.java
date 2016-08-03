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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbolFolder;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageDescription extends AbstractEntityImage {

	private final ShapeType shapeType;

	final private Url url;

	private final TextBlock asSmall;

	private final TextBlock name;

	public EntityImageDescription(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, entity.getColors(skinParam).mute(skinParam));
		final Stereotype stereotype = entity.getStereotype();
		USymbol symbol = entity.getUSymbol() == null ? (getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2
				: USymbol.COMPONENT1) : entity.getUSymbol();
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		shapeType = symbol == USymbol.FOLDER ? ShapeType.FOLDER : ShapeType.RECTANGLE;

		final Display codeDisplay = Display.getWithNewlines(entity.getCode());
		final TextBlock desc = (entity.getDisplay().equals(codeDisplay) && symbol instanceof USymbolFolder)
				|| entity.getDisplay().isWhite() ? TextBlockUtils.empty(0, 0) : new BodyEnhanced(entity.getDisplay(),
				symbol.getFontParam(), getSkinParam(), HorizontalAlignment.CENTER, stereotype,
				symbol.manageHorizontalLine(), false, false);

		this.url = entity.getUrl99();

		HtmlColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), symbol.getColorParamBack(), getStereo());
		}
		// backcolor = HtmlColorUtils.BLUE;
		final HtmlColor forecolor = SkinParamUtils.getColor(getSkinParam(), symbol.getColorParamBorder(), getStereo());
		final SymbolContext ctx = new SymbolContext(backcolor, forecolor).withStroke(new UStroke(1.5)).withShadow(
				getSkinParam().shadowing2(symbol.getSkinParameter()));

		TextBlock stereo = TextBlockUtils.empty(0, 0);

		if (stereotype != null && stereotype.getSprite() != null
				&& getSkinParam().getSprite(stereotype.getSprite()) != null) {
			symbol = symbol.withStereoAlignment(HorizontalAlignment.RIGHT);
			stereo = getSkinParam().getSprite(stereotype.getSprite()).asTextBlock(stereotype.getHtmlColor(), 1);
		} else if (stereotype != null && stereotype.getLabel(false) != null
				&& portionShower.showPortion(EntityPortion.STEREOTYPE, entity)) {
			stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().useGuillemet())).create(
					new FontConfiguration(getSkinParam(), symbol.getFontParamStereotype(), stereotype),
					HorizontalAlignment.CENTER, getSkinParam());
		}

		name = new BodyEnhanced(codeDisplay, symbol.getFontParam(), getSkinParam(), HorizontalAlignment.CENTER,
				stereotype, symbol.manageHorizontalLine(), false, false);

		asSmall = symbol.asSmall(name, desc, stereo, ctx);
	}

	public Dimension2D getNameDimension(StringBounder stringBounder) {
		return name.calculateDimension(stringBounder);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return asSmall.calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		asSmall.drawU(ug);

		if (url != null) {
			ug.closeAction();
		}
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public int getShield() {
		return 0;
	}

}
