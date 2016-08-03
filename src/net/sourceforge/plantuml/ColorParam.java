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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;


public enum ColorParam {
	background(HtmlColorUtils.WHITE, ColorType.BACK),
	hyperlink(HtmlColorUtils.BLUE),
	
	activityBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	activityBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	activityStart(HtmlColorUtils.BLACK),
	activityEnd(HtmlColorUtils.BLACK),
	activityBar(HtmlColorUtils.BLACK),
	activityArrow(HtmlColorUtils.MY_RED, ColorType.ARROW),
	swimlaneBorder(HtmlColorUtils.BLACK),
	
	usecaseBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	usecaseBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	usecaseArrow(HtmlColorUtils.MY_RED, ColorType.ARROW),

	objectBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	objectBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	objectArrow(HtmlColorUtils.MY_RED, ColorType.ARROW),
	
	classHeaderBackground(null, ColorType.BACK),
	classBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	classBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	stereotypeCBackground(HtmlColorUtils.COL_ADD1B2),
	stereotypeABackground(HtmlColorUtils.COL_A9DCDF),
	stereotypeIBackground(HtmlColorUtils.COL_B4A7E5),
	stereotypeEBackground(HtmlColorUtils.COL_EB937F),
	classArrow(HtmlColorUtils.MY_RED, ColorType.ARROW),
		
	packageBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	packageBorder(HtmlColorUtils.BLACK, ColorType.LINE),

	partitionBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	partitionBorder(HtmlColorUtils.BLACK, ColorType.LINE),

	componentBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	componentBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	interfaceBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	interfaceBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	componentArrow(HtmlColorUtils.MY_RED, ColorType.ARROW),

	stateBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	stateBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	stateArrow(HtmlColorUtils.MY_RED, ColorType.ARROW),
	stateStart(HtmlColorUtils.BLACK),
	stateEnd(HtmlColorUtils.BLACK),

	noteBackground(HtmlColorUtils.COL_FBFB77, true, ColorType.BACK),
	noteBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	
	legendBackground(HtmlColorUtils.COL_DDDDDD, true, ColorType.BACK),
	legendBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	
	actorBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	actorBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	participantBackground(HtmlColorUtils.MY_YELLOW, true, ColorType.BACK),
	participantBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	sequenceGroupBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceGroupBackground(HtmlColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceGroupBodyBackground(HtmlColorUtils.RED, true, ColorType.BACK),
	sequenceReferenceBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceReferenceHeaderBackground(HtmlColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceReferenceBackground(HtmlColorUtils.WHITE, true, ColorType.BACK),
	sequenceDividerBackground(HtmlColorUtils.COL_EEEEEE, true, ColorType.BACK),
	sequenceDividerBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceLifeLineBackground(HtmlColorUtils.WHITE, true, ColorType.BACK),
	sequenceLifeLineBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	sequenceNewpageSeparator(HtmlColorUtils.BLACK, ColorType.LINE),
	sequenceArrow(HtmlColorUtils.MY_RED),
	sequenceBoxBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	sequenceBoxBackground(HtmlColorUtils.COL_DDDDDD, true, ColorType.BACK),
	
	artifactBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	artifactBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	cloudBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	cloudBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	queueBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	queueBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	databaseBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	databaseBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	folderBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	folderBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	frameBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	frameBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	nodeBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	nodeBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	rectangleBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	rectangleBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	agentBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	agentBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	storageBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	storageBorder(HtmlColorUtils.BLACK, ColorType.LINE),
	boundaryBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	boundaryBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	controlBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	controlBorder(HtmlColorUtils.MY_RED, ColorType.LINE),
	entityBackground(HtmlColorUtils.MY_YELLOW, ColorType.BACK),
	entityBorder(HtmlColorUtils.MY_RED, ColorType.LINE),

	
	iconPrivate(HtmlColorUtils.COL_C82930),
	iconPrivateBackground(HtmlColorUtils.COL_F24D5C),
	iconPackage(HtmlColorUtils.COL_1963A0),
	iconPackageBackground(HtmlColorUtils.COL_4177AF),
	iconProtected(HtmlColorUtils.COL_B38D22),
	iconProtectedBackground(HtmlColorUtils.COL_FFFF44),
	iconPublic(HtmlColorUtils.COL_038048),
	iconPublicBackground(HtmlColorUtils.COL_84BE84);
	
	private final boolean isBackground;
	private final HtmlColor defaultValue;
	private final ColorType colorType;
	
	private ColorParam(HtmlColor defaultValue, ColorType colorType) {
		this(defaultValue, false, colorType);
	}
	
	private ColorParam(HtmlColor defaultValue) {
		this(defaultValue, false, null);
	}
	
	private ColorParam() {
		this(null, false, null);
	}
	
	private ColorParam(boolean isBackground) {
		this(null, isBackground, null);
	}
	
	private ColorParam(HtmlColor defaultValue, boolean isBackground, ColorType colorType) {
		this.isBackground = isBackground;
		this.defaultValue = defaultValue;
		this.colorType = colorType;
	}

	protected boolean isBackground() {
		return isBackground;
	}

	public final HtmlColor getDefaultValue() {
		return defaultValue;
	}

	public ColorType getColorType() {
		return colorType;
	}
}
