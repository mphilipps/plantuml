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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileWithNoteOpale extends AbstractFtile implements Stencil {

	private final Ftile tile;
	private final Opale opale;

	// private final HtmlColor arrowColor;
	private final NotePosition notePosition;
	private final double suppSpace = 20;

	public Set<Swimlane> getSwimlanes() {
		return tile.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return tile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile.getSwimlaneOut();
	}

	public FtileWithNoteOpale(Ftile tile, Display note, NotePosition notePosition, ISkinParam skinParam,
			boolean withLink) {
		super(tile.shadowing());
		this.tile = tile;
		this.notePosition = notePosition;
		// this.arrowColor = arrowColor;

		final Rose rose = new Rose();
		// final HtmlColor fontColor = rose.getFontColor(skinParam, FontParam.NOTE);
		// final UFont fontNote = skinParam.getFont(FontParam.NOTE, null, false);

		final HtmlColor noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);

		// final FontConfiguration fc = new FontConfiguration(fontNote, fontColor, skinParam.getHyperlinkColor(),
		// skinParam.useUnderlineForHyperlink());
		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.NOTE, null);

		final Sheet sheet = new CreoleParser(fc, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL)
				.createSheet(note);
		final TextBlock text = new SheetBlock2(new SheetBlock1(sheet, 0, skinParam.getPadding()), this, new UStroke(1));
		opale = new Opale(borderColor, noteBackgroundColor, text, skinParam.shadowing(), withLink);

	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double yForFtile = (dimTotal.getHeight() - dimTile.getHeight()) / 2;
		final double marge;
		if (notePosition == NotePosition.LEFT) {
			marge = dimNote.getWidth() + suppSpace;
		} else {
			marge = 0;
		}

		return new UTranslate(marge, yForFtile);

	}

	private UTranslate getTranslateForOpale(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);

		final double yForNote = (dimTotal.getHeight() - dimNote.getHeight()) / 2;

		if (notePosition == NotePosition.LEFT) {
			return new UTranslate(0, yForNote);
		}
		final double dx = dimTotal.getWidth() - dimNote.getWidth();
		return new UTranslate(dx, yForNote);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);

		if (notePosition == NotePosition.LEFT) {
			final Direction strategy = Direction.RIGHT;
			final Point2D pp1 = new Point2D.Double(dimNote.getWidth(), dimNote.getHeight() / 2);
			final Point2D pp2 = new Point2D.Double(dimNote.getWidth() + suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		} else {
			final Direction strategy = Direction.LEFT;
			final Point2D pp1 = new Point2D.Double(0, dimNote.getHeight() / 2);
			final Point2D pp2 = new Point2D.Double(-suppSpace, dimNote.getHeight() / 2);
			opale.setOpale(strategy, pp1, pp2);
		}
		opale.drawU(ug.apply(getTranslateForOpale(ug)));
		ug.apply(getTranslate(stringBounder)).draw(tile);
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry orig = tile.calculateDimension(stringBounder);
		final UTranslate translate = getTranslate(stringBounder);
		if (orig.hasPointOut()) {
			return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy(),
					orig.getOutY() + translate.getDy());
		}
		return new FtileGeometry(dimTotal, orig.getLeft() + translate.getDx(), orig.getInY() + translate.getDy());
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimNote = opale.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new Dimension2DDouble(dimTile.getWidth() + 1 * dimNote.getWidth() + suppSpace, height);
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return -opale.getMarginX1();
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return opale.calculateDimension(stringBounder).getWidth() - opale.getMarginX1();
	}

}
