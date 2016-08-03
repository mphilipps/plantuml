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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.ConditionalBuilder;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svek.ConditionStyle;

public class FtileFactoryDelegatorIf extends FtileFactoryDelegator {

	private final Pragma pragma;

	public FtileFactoryDelegatorIf(FtileFactory factory, ISkinParam skinParam, Pragma pragma) {
		super(factory, skinParam);
		this.pragma = pragma;
	}

	@Override
	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering) {

		final ConditionStyle conditionStyle = getSkinParam().getConditionStyle();
		final Branch branch0 = thens.get(0);

		final HtmlColor borderColor = getRose().getHtmlColor(getSkinParam(), ColorParam.activityBorder);
		final HtmlColor backColor = branch0.getColor() == null ? getRose().getHtmlColor(getSkinParam(),
				ColorParam.activityBackground) : branch0.getColor();
		final HtmlColor arrowColor = getRose().getHtmlColor(getSkinParam(), ColorParam.activityArrow);

		final FontConfiguration fcArrow = new FontConfiguration(getSkinParam(), FontParam.ACTIVITY_ARROW, null);
		// .changeColor(fontColor(FontParam.ACTIVITY_DIAMOND));
		if (thens.size() > 1) {
			if (pragma.useVerticalIf()/* OptionFlags.USE_IF_VERTICAL */)
				return FtileIfLongVertical.create(swimlane, borderColor, backColor, arrowColor, getFactory(),
						conditionStyle, thens, elseBranch, fcArrow, topInlinkRendering, afterEndwhile);
			return FtileIfLongHorizontal.create(swimlane, borderColor, backColor, arrowColor, getFactory(),
					conditionStyle, thens, elseBranch, fcArrow, topInlinkRendering, afterEndwhile);
		}
		final FontParam testParam = conditionStyle == ConditionStyle.INSIDE ? FontParam.ACTIVITY_DIAMOND
				: FontParam.ACTIVITY_ARROW;
		final FontConfiguration fcTest = new FontConfiguration(getSkinParam(), testParam, null)
				.changeColor(fontColor(FontParam.ACTIVITY_DIAMOND));

		return ConditionalBuilder.create(swimlane, borderColor, backColor, arrowColor, getFactory(), conditionStyle,
				thens.get(0), elseBranch, getSkinParam(), getStringBounder(), fcArrow, fcTest);
	}

	private HtmlColor fontColor(FontParam param) {
		return getSkinParam().getFontHtmlColor(null, param);
	}

}
