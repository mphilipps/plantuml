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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockGeneric;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.HeaderLayout;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageClassHeader2 extends AbstractEntityImage {

	final private HeaderLayout headerLayout;

	public EntityImageClassHeader2(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);

		final boolean italic = entity.getEntityType() == LeafType.ABSTRACT_CLASS
				|| entity.getEntityType() == LeafType.INTERFACE;

		final HtmlColor color = SkinParamUtils.getFontColor(getSkinParam(), FontParam.CLASS, getStereo());
		final Stereotype stereotype = entity.getStereotype();
		final String generic = entity.getGeneric();
		FontConfiguration fontConfigurationName = new FontConfiguration(getSkinParam(), FontParam.CLASS, stereotype);
		if (italic) {
			fontConfigurationName = fontConfigurationName.italic();
		}
		final TextBlock name = TextBlockUtils.withMargin(
				entity.getDisplay().createWithNiceCreoleMode(fontConfigurationName, HorizontalAlignment.CENTER,
						skinParam), 3, 3, 0, 0);

		final TextBlock stereo;
		if (stereotype == null || stereotype.getLabel(false) == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			stereo = null;
		} else {
			stereo = TextBlockUtils.withMargin(
					Display.create(stereotype.getLabels(skinParam.useGuillemet())).create(
							new FontConfiguration(getSkinParam(), FontParam.CLASS_STEREOTYPE, stereotype),
							HorizontalAlignment.CENTER, skinParam), 1, 0);
		}

		TextBlock genericBlock;
		if (generic == null) {
			genericBlock = null;
		} else {
			genericBlock = Display.getWithNewlines(generic).create(
					new FontConfiguration(getSkinParam(), FontParam.CLASS_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, skinParam);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
			final HtmlColor classBackground = SkinParamUtils
					.getColor(getSkinParam(), ColorParam.background, stereotype);
			// final HtmlColor classBorder = getColor(ColorParam.classBorder, stereotype);
			final HtmlColor classBorder = SkinParamUtils.getFontColor(getSkinParam(), FontParam.CLASS_STEREOTYPE,
					stereotype);
			genericBlock = new TextBlockGeneric(genericBlock, classBackground, classBorder);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
		}

		final TextBlock circledCharacter;
		if (portionShower.showPortion(EntityPortion.CIRCLED_CHARACTER, (ILeaf) getEntity())) {
			circledCharacter = TextBlockUtils.withMargin(getCircledCharacter(entity, skinParam), 4, 0, 5, 5);
		} else {
			circledCharacter = null;
		}
		this.headerLayout = new HeaderLayout(circledCharacter, stereo, name, genericBlock);
	}

	private TextBlock getCircledCharacter(ILeaf entity, ISkinParam skinParam) {
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null && stereotype.getSprite() != null) {
			return skinParam.getSprite(stereotype.getSprite()).asTextBlock(stereotype.getHtmlColor(), 1);
		}
		final UFont font = SkinParamUtils.getFont(getSkinParam(), FontParam.CIRCLED_CHARACTER, null);
		final HtmlColor classBorder = SkinParamUtils.getColor(getSkinParam(), ColorParam.classBorder, stereotype);
		if (stereotype != null && stereotype.getCharacter() != 0) {
			return new CircledCharacter(stereotype.getCharacter(), getSkinParam().getCircledCharacterRadius(), font,
					stereotype.getHtmlColor(), classBorder, SkinParamUtils.getFontColor(getSkinParam(),
							FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getEntityType() == LeafType.ANNOTATION) {
			return new CircledCharacter('@', getSkinParam().getCircledCharacterRadius(), font, SkinParamUtils.getColor(
					getSkinParam(), ColorParam.stereotypeABackground, stereotype), classBorder,
					SkinParamUtils.getFontColor(getSkinParam(), FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getEntityType() == LeafType.ABSTRACT_CLASS) {
			return new CircledCharacter('A', getSkinParam().getCircledCharacterRadius(), font, SkinParamUtils.getColor(
					getSkinParam(), ColorParam.stereotypeABackground, stereotype), classBorder,
					SkinParamUtils.getFontColor(getSkinParam(), FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getEntityType() == LeafType.CLASS) {
			return new CircledCharacter('C', getSkinParam().getCircledCharacterRadius(), font, SkinParamUtils.getColor(
					getSkinParam(), ColorParam.stereotypeCBackground, stereotype), classBorder,
					SkinParamUtils.getFontColor(getSkinParam(), FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getEntityType() == LeafType.INTERFACE) {
			return new CircledCharacter('I', getSkinParam().getCircledCharacterRadius(), font, SkinParamUtils.getColor(
					getSkinParam(), ColorParam.stereotypeIBackground, stereotype), classBorder,
					SkinParamUtils.getFontColor(getSkinParam(), FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getEntityType() == LeafType.ENUM) {
			return new CircledCharacter('E', getSkinParam().getCircledCharacterRadius(), font, SkinParamUtils.getColor(
					getSkinParam(), ColorParam.stereotypeEBackground, stereotype), classBorder,
					SkinParamUtils.getFontColor(getSkinParam(), FontParam.CIRCLED_CHARACTER, null));
		}
		assert false;
		return null;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return headerLayout.getDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double width, double height) {
		headerLayout.drawU(ug, width, height);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
