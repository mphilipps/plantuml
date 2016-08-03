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
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterDecoration;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageEmptyPackage extends AbstractEntityImage {

	final private TextBlock desc;
	final private static int MARGIN = 10;
	final private HtmlColor specificBackColor;
	final private ISkinParam skinParam;
	final private Stereotype stereotype;

	public EntityImageEmptyPackage(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		this.skinParam = skinParam;
		this.specificBackColor = entity.getColors(skinParam).getColor(ColorType.BACK);
		this.stereotype = entity.getStereotype();
		this.desc = entity.getDisplay().create(
				new FontConfiguration(getSkinParam(), FontParam.PACKAGE, stereotype),
				HorizontalAlignment.CENTER, skinParam);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = desc.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, MARGIN * 2, MARGIN * 2 + dim.getHeight() * 2);
	}

	private UStroke getStroke() {
		UStroke stroke = getSkinParam().getThickness(LineParam.packageBorder, getStereo());
		if (stroke == null) {
			stroke = new UStroke(2.0);
		}
		return stroke;
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();

		final HtmlColor back = Cluster.getBackColor(specificBackColor, skinParam, stereotype);

		final ClusterDecoration decoration = new ClusterDecoration(getSkinParam().getPackageStyle(), null, desc,
				TextBlockUtils.empty(0, 0), 0, 0, widthTotal, heightTotal, getStroke());

		decoration.drawU(ug, back,
				SkinParamUtils.getColor(getSkinParam(), ColorParam.packageBorder, getStereo()), getSkinParam().shadowing());
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
