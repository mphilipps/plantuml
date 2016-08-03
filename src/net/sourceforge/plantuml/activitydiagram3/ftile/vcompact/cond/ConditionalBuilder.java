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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamondInside;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ConditionalBuilder {

	private final Swimlane swimlane;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	private final HtmlColor arrowColor;
	private final FtileFactory ftileFactory;
	private final ConditionStyle conditionStyle;
	private final Branch branch1;
	private final Branch branch2;
	private final ISkinParam skinParam;
	private final StringBounder stringBounder;
	private final FontConfiguration fontArrow;
	private final FontConfiguration fontTest;

	private final Ftile tile1;
	private final Ftile tile2;

	public ConditionalBuilder(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, HtmlColor arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, Branch branch1, Branch branch2,
			ISkinParam skinParam, StringBounder stringBounder, FontConfiguration fontArrow, FontConfiguration fontTest) {
		this.swimlane = swimlane;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.arrowColor = arrowColor;
		this.ftileFactory = ftileFactory;
		this.conditionStyle = conditionStyle;
		this.branch1 = branch1;
		this.branch2 = branch2;
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
		this.fontArrow = fontArrow;
		this.fontTest = fontTest;

		this.tile1 = new FtileMinWidth(branch1.getFtile(), 30);
		this.tile2 = new FtileMinWidth(branch2.getFtile(), 30);

	}

	static public Ftile create(Swimlane swimlane, HtmlColor borderColor, HtmlColor backColor, HtmlColor arrowColor,
			FtileFactory ftileFactory, ConditionStyle conditionStyle, Branch branch1, Branch branch2,
			ISkinParam skinParam, StringBounder stringBounder, FontConfiguration fcArrow, FontConfiguration fcTest) {
		final ConditionalBuilder builder = new ConditionalBuilder(swimlane, borderColor, backColor, arrowColor,
				ftileFactory, conditionStyle, branch1, branch2, skinParam, stringBounder, fcArrow, fcTest);
		return builder.createWithLinks();
		// return builder.createWithDiamonds();
		// return builder.createNude();

	}

	private Ftile createNude() {
		return new FtileIfNude(tile1, tile2, swimlane);

	}

	private Ftile createWithDiamonds() {
		final Ftile diamond1 = getDiamond1();
		final Ftile diamond2 = getDiamond2();
		final FtileIfWithDiamonds ftile = new FtileIfWithDiamonds(diamond1, tile1, tile2, diamond2, swimlane,
				stringBounder);
		final Dimension2D label1 = getLabelBranch1().calculateDimension(stringBounder);
		final Dimension2D label2 = getLabelBranch2().calculateDimension(stringBounder);
		final double diff1 = ftile.computeMarginNeedForBranchLabe1(stringBounder, label1);
		final double diff2 = ftile.computeMarginNeedForBranchLabe2(stringBounder, label2);
		Ftile result = FtileUtils.addHorizontalMargin(ftile, diff1, diff2);
		final double suppHeight = ftile.computeVerticalMarginNeedForBranchs(stringBounder, label1, label2);
		result = FtileUtils.addVerticalMargin(result, suppHeight, 0);
		return result;
	}

	private Ftile createWithLinks() {
		final Ftile diamond1 = getDiamond1();
		final Ftile diamond2 = getDiamond2();
		final Ftile tmp1 = FtileUtils.addHorizontalMargin(tile1, 10);
		final Ftile tmp2 = FtileUtils.addHorizontalMargin(tile2, 10);
		final FtileIfWithLinks ftile = new FtileIfWithLinks(diamond1, tmp1, tmp2, diamond2, swimlane, arrowColor,
				stringBounder);
		final Dimension2D label1 = getLabelBranch1().calculateDimension(stringBounder);
		final Dimension2D label2 = getLabelBranch2().calculateDimension(stringBounder);
		final double diff1 = ftile.computeMarginNeedForBranchLabe1(stringBounder, label1);
		final double diff2 = ftile.computeMarginNeedForBranchLabe2(stringBounder, label2);
		final double suppHeight = ftile.computeVerticalMarginNeedForBranchs(stringBounder, label1, label2);
		Ftile result = ftile.addLinks(branch1, branch2, stringBounder);
		result = FtileUtils.addHorizontalMargin(result, diff1, diff2);
		result = FtileUtils.addVerticalMargin(result, suppHeight, 0);
		return result;
	}

	private Ftile getDiamond1() {
		final Display labelTest = branch1.getLabelTest();
		final TextBlock tb1 = getLabelBranch1();
		final TextBlock tb2 = getLabelBranch2();

		final Sheet sheet = new CreoleParser(fontTest, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
				.createSheet(labelTest);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, 0, skinParam.getPadding());
		final TextBlock tbTest = new SheetBlock2(sheetBlock1, Diamond.asStencil(sheetBlock1), new UStroke(1.5));

		final Ftile diamond1;
		if (conditionStyle == ConditionStyle.INSIDE) {
			diamond1 = new FtileDiamondInside(tile1.shadowing(), backColor, borderColor, swimlane, tbTest)
					.withWestAndEast(tb1, tb2);
		} else if (conditionStyle == ConditionStyle.DIAMOND) {
			diamond1 = new FtileDiamond(tile1.shadowing(), backColor, borderColor, swimlane).withNorth(tbTest)
					.withWestAndEast(tb1, tb2);
		} else {
			throw new IllegalStateException();
		}
		return diamond1;
	}

	private TextBlock getLabelBranch2() {
		final TextBlock tb2 = branch2.getLabelPositive().create(fontArrow, HorizontalAlignment.LEFT, ftileFactory,
				CreoleMode.SIMPLE_LINE);
		return tb2;
	}

	private TextBlock getLabelBranch1() {
		final TextBlock tb1 = branch1.getLabelPositive().create(fontArrow, HorizontalAlignment.LEFT, ftileFactory,
				CreoleMode.SIMPLE_LINE);
		return tb1;
	}

	private Ftile getDiamond2() {
		final Ftile diamond2;
		if (hasTwoBranches()) {
			final Display out1 = LinkRendering.getDisplay(branch1.getFtile().getOutLinkRendering());
			final TextBlock tbout1 = out1 == null ? null : out1.create(fontArrow, HorizontalAlignment.LEFT,
					ftileFactory, CreoleMode.SIMPLE_LINE);
			final Display out2 = LinkRendering.getDisplay(branch2.getFtile().getOutLinkRendering());
			final TextBlock tbout2 = out2 == null ? null : out2.create(fontArrow, HorizontalAlignment.LEFT,
					ftileFactory, CreoleMode.SIMPLE_LINE);
			diamond2 = new FtileDiamond(tile1.shadowing(), backColor, borderColor, swimlane).withWest(tbout1).withEast(
					tbout2);
		} else {
			// diamond2 = new FtileEmpty(tile1.shadowing(), Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2,
			// swimlane, swimlane);
			diamond2 = new FtileEmpty(tile1.shadowing(), 0, Diamond.diamondHalfSize / 2, swimlane, swimlane);
		}
		return diamond2;
	}

	public boolean hasTwoBranches() {
		return tile1.calculateDimension(stringBounder).hasPointOut()
				&& tile2.calculateDimension(stringBounder).hasPointOut();
	}

	// private HtmlColor fontColor() {
	// return skinParam.getFontHtmlColor(FontParam.ACTIVITY_DIAMOND, null);
	// }

}
