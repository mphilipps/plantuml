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

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UGraphicInterceptorUDrawable;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.svek.UGraphicForSnake;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class FtileGroup extends AbstractFtile {

	private final double diffYY2 = 20;
	private final Ftile inner;
	private final TextBlock name;
	private final TextBlock headerNote;
	private final HtmlColor arrowColor;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	private final HtmlColor titleColor;
	private final UStroke stroke;

	public FtileGroup(Ftile inner, Display title, Display displayNote, HtmlColor arrowColor, HtmlColor backColor,
			HtmlColor titleColor, ISkinParam skinParam, HtmlColor borderColor) {
		super(inner.shadowing());
		this.backColor = backColor == null ? HtmlColorUtils.WHITE : backColor;
		this.inner = FtileUtils.addHorizontalMargin(inner, 10);
		this.arrowColor = arrowColor;
		this.titleColor = titleColor;
		this.borderColor = backColor == null ? HtmlColorUtils.BLACK : borderColor;
		final UFont font = skinParam.getFont(null, false, FontParam.PARTITION);
		// final UFont font = new UFont("Serif", Font.PLAIN, 14);
		// final HtmlColor fontColor = HtmlColorUtils.BLACK;
		final HtmlColor fontColor = skinParam.getFontHtmlColor(null, FontParam.PARTITION);
		final FontConfiguration fc = new FontConfiguration(font, fontColor, skinParam.getHyperlinkColor(),
				skinParam.useUnderlineForHyperlink(), skinParam.getTabSize());
		if (title == null) {
			this.name = TextBlockUtils.empty(0, 0);
		} else {
			this.name = title.create(fc, HorizontalAlignment.LEFT, skinParam);
		}
		if (Display.isNull(displayNote)) {
			this.headerNote = TextBlockUtils.empty(0, 0);
		} else {
			this.headerNote = new FloatingNote(displayNote, skinParam);
		}

		final UStroke thickness = skinParam.getThickness(LineParam.partitionBorder, null);
		this.stroke = thickness == null ? new UStroke(2) : thickness;
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return inner.getInLinkRendering();
	}

	public Set<Swimlane> getSwimlanes() {
		return inner.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return inner.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return inner.getSwimlaneOut();
	}

	private double diffHeightTitle(StringBounder stringBounder) {
		final Dimension2D dimTitle = name.calculateDimension(stringBounder);
		return Math.max(25, dimTitle.getHeight() + 20);
	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final double suppWidth = suppWidth(stringBounder);
		return new UTranslate(suppWidth / 2, diffHeightTitle(stringBounder) + headerNoteHeight(stringBounder));
	}

	private static MinMax getMinMax(TextBlock tb, StringBounder stringBounder) {
		final LimitFinder limitFinder = new LimitFinder(stringBounder, false);
		final UGraphicForSnake interceptor = new UGraphicForSnake(limitFinder);
		final UGraphicInterceptorUDrawable interceptor2 = new UGraphicInterceptorUDrawable(interceptor);

		tb.drawU(interceptor2);
		interceptor2.flushUg();
		return limitFinder.getMinMax();
	}

	public double suppWidth(StringBounder stringBounder) {
		final FtileGeometry orig = getInnerDimension(stringBounder);
		final Dimension2D dimTitle = name.calculateDimension(stringBounder);
		final Dimension2D dimHeaderNote = headerNote.calculateDimension(stringBounder);
		final double suppWidth = MathUtils
				.max(orig.getWidth(), dimTitle.getWidth() + 20, dimHeaderNote.getWidth() + 20) - orig.getWidth();
		return suppWidth;
	}

	private FtileGeometry getInnerDimension(StringBounder stringBounder) {
		final FtileGeometry orig = inner.calculateDimension(stringBounder);
		final MinMax minMax = getMinMax(inner, stringBounder);
		final double missingWidth = minMax.getMaxX() - orig.getWidth();
		if (missingWidth > 0) {
			return orig.addDim(missingWidth + 5, 0);
		}
		return orig;
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final FtileGeometry orig = getInnerDimension(stringBounder);
		final double suppWidth = suppWidth(stringBounder);
		final double width = orig.getWidth() + suppWidth;
		final double height = orig.getHeight() + diffHeightTitle(stringBounder) + diffYY2
				+ headerNoteHeight(stringBounder);
		final double titleAndHeaderNoteHeight = diffHeightTitle(stringBounder) + headerNoteHeight(stringBounder);
		if (orig.hasPointOut()) {
			return new FtileGeometry(width, height, orig.getLeft() + suppWidth / 2, orig.getInY()
					+ titleAndHeaderNoteHeight, orig.getOutY() + titleAndHeaderNoteHeight);
		}
		return new FtileGeometry(width, height, orig.getLeft() + suppWidth / 2, orig.getInY()
				+ titleAndHeaderNoteHeight);
	}

	private double headerNoteHeight(StringBounder stringBounder) {
		return headerNote.calculateDimension(stringBounder).getHeight();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final SymbolContext symbolContext = new SymbolContext(backColor, borderColor).withShadow(shadowing())
				.withStroke(stroke);
		USymbol.FRAME.asBig(name, TextBlockUtils.empty(0, 0), dimTotal.getWidth(), dimTotal.getHeight(), symbolContext)
				.drawU(ug);

		final Dimension2D dimHeaderNote = headerNote.calculateDimension(stringBounder);
		headerNote.drawU(ug.apply(new UTranslate(dimTotal.getWidth() - dimHeaderNote.getWidth() - 10,
				diffHeightTitle(ug.getStringBounder()) - 10)));

		ug.apply(getTranslate(stringBounder)).draw(inner);

	}

}
