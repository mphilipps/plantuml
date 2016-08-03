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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Font;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.rose.ComponentRoseDestroy;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingElse;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingSpace;
import net.sourceforge.plantuml.skin.rose.ComponentRoseReference;
import net.sourceforge.plantuml.skin.rose.ComponentRoseTitle;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public class BlueModern implements Skin {

	private final UFont bigFont = new UFont("SansSerif", Font.BOLD, 20);
	private final UFont participantFont = new UFont("SansSerif", Font.PLAIN, 17);
	private final UFont normalFont = new UFont("SansSerif", Font.PLAIN, 13);
	private final UFont smallFont = new UFont("SansSerif", Font.BOLD, 11);

	private final HtmlColor hyperlinkColor = HtmlColorUtils.BLUE;
	private final boolean useUnderlineForHyperlink = true;
	private final HtmlColor blue1 = HtmlColorUtils.COL_527BC6;
	private final HtmlColor blue2 = HtmlColorUtils.COL_D1DBEF;
	private final HtmlColor blue3 = HtmlColorUtils.COL_D7E0F2;

	private final HtmlColor red = HtmlColorUtils.MY_RED;

	private final HtmlColor lineColor = HtmlColorUtils.COL_989898;
	private final HtmlColor borderGroupColor = HtmlColorUtils.COL_BBBBBB;

	public Component createComponent(ComponentType type, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {

		if (type.isArrow()) {
			final HtmlColor sequenceArrow = config.getColor() == null ? HtmlColorUtils.BLACK : config.getColor();
			if (config.isSelfArrow()) {
				return new ComponentBlueModernSelfArrow(sequenceArrow, normalFont.toFont2(HtmlColorUtils.BLACK,
						useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()), stringsToDisplay, config, param);
			}
			return new ComponentBlueModernArrow(sequenceArrow, useUnderlineForHyperlink, normalFont.toFont2(
					HtmlColorUtils.BLACK, useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()),
					stringsToDisplay, config, param);
		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentBlueModernParticipant(blue1, blue2, participantFont.toFont2(HtmlColorUtils.WHITE,
					useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()), stringsToDisplay, param);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentBlueModernParticipant(blue1, blue2, participantFont.toFont2(HtmlColorUtils.WHITE,
					useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()), stringsToDisplay, param);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.CONTINUE_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentBlueModernActor(blue2, blue1, participantFont.toFont2(blue1, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), stringsToDisplay, true, param);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentBlueModernActor(blue2, blue1, participantFont.toFont2(blue1, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), stringsToDisplay, false, param);
		}
		if (type == ComponentType.NOTE) {
			return new ComponentBlueModernNote(HtmlColorUtils.WHITE, HtmlColorUtils.BLACK, normalFont.toFont2(
					HtmlColorUtils.BLACK, useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()),
					stringsToDisplay, param);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
			return new ComponentBlueModernActiveLine(blue1, true, true);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN) {
			return new ComponentBlueModernActiveLine(blue1, true, false);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			return new ComponentBlueModernActiveLine(blue1, false, true);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN) {
			return new ComponentBlueModernActiveLine(blue1, false, false);
		}
		if (type == ComponentType.DELAY_LINE) {
			return new ComponentBlueModernDelayLine(lineColor);
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentBlueModernDelayText(param.getFont(null, false, FontParam.SEQUENCE_DELAY).toFont2(
					HtmlColorUtils.BLACK, useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()),
					stringsToDisplay, param);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(red);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentBlueModernGroupingHeader(blue1, blue3, borderGroupColor, HtmlColorUtils.BLACK,
					normalFont.toFont2(HtmlColorUtils.WHITE, useUnderlineForHyperlink, hyperlinkColor,
							param.getTabSize()), smallFont, stringsToDisplay, param);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(HtmlColorUtils.BLACK, smallFont.toFont2(HtmlColorUtils.BLACK,
					useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()), stringsToDisplay.get(0), param,
					blue3);
		}
		if (type == ComponentType.GROUPING_SPACE) {
			return new ComponentRoseGroupingSpace(7);
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(bigFont.toFont2(HtmlColorUtils.BLACK, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), stringsToDisplay, param);
		}
		if (type == ComponentType.REFERENCE) {
			return new ComponentRoseReference(normalFont.toFont2(HtmlColorUtils.BLACK, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), new SymbolContext(blue1, borderGroupColor).withStroke(Rose
					.getStroke(param, LineParam.sequenceDividerBorder, 2)), normalFont.toFont2(HtmlColorUtils.WHITE,
					useUnderlineForHyperlink, hyperlinkColor, param.getTabSize()), stringsToDisplay,
					HorizontalAlignment.CENTER, param, blue3);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentBlueModernNewpage(blue1);
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentBlueModernDivider(normalFont.toFont2(HtmlColorUtils.BLACK, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), blue2, blue1, HtmlColorUtils.BLACK, stringsToDisplay, param);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(smallFont.toFont2(HtmlColorUtils.BLACK, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), Display.create("This skin was created ", "in April 2009."),
					param);
		}
		if (type == ComponentType.ENGLOBER) {
			return new ComponentBlueModernEnglober(blue1, blue3, stringsToDisplay, param.getFont(
					null, false, FontParam.SEQUENCE_BOX).toFont2(HtmlColorUtils.BLACK, useUnderlineForHyperlink,
					hyperlinkColor, param.getTabSize()), param);
		}

		return null;

	}

	public Object getProtocolVersion() {
		return 1;
	}

}
