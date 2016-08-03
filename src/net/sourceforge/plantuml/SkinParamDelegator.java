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

import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.graphic.SkinParameter;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.sprite.Sprite;

public class SkinParamDelegator implements ISkinParam {

	final private ISkinParam skinParam;

	public SkinParamDelegator(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public HtmlColor getHyperlinkColor() {
		return skinParam.getHyperlinkColor();
	}

	public HtmlColor getBackgroundColor() {
		return skinParam.getBackgroundColor();
	}

	public int getCircledCharacterRadius() {
		return skinParam.getCircledCharacterRadius();
	}

	public UFont getFont(Stereotype stereotype, boolean inPackageTitle, FontParam... fontParam) {
		return skinParam.getFont(stereotype, false, fontParam);
	}

	public HtmlColor getFontHtmlColor(Stereotype stereotype, FontParam... param) {
		return skinParam.getFontHtmlColor(stereotype, param);
	}

	public HtmlColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		return skinParam.getHtmlColor(param, stereotype, clickable);
	}

	public String getValue(String key) {
		return skinParam.getValue(key);
	}

	public int classAttributeIconSize() {
		return skinParam.classAttributeIconSize();
	}

	public int getDpi() {
		return skinParam.getDpi();
	}

	public DotSplines getDotSplines() {
		return skinParam.getDotSplines();
	}

	public String getDotExecutable() {
		return skinParam.getDotExecutable();
	}

	public HorizontalAlignment getHorizontalAlignment(AlignParam param) {
		return skinParam.getHorizontalAlignment(param);
	}

	public ColorMapper getColorMapper() {
		return skinParam.getColorMapper();
	}

	public boolean shadowing() {
		return skinParam.shadowing();
	}

	public boolean shadowing2(SkinParameter skinParameter) {
		return skinParam.shadowing2(skinParameter);
	}

	public PackageStyle getPackageStyle() {
		return skinParam.getPackageStyle();
	}

	public Sprite getSprite(String name) {
		return skinParam.getSprite(name);
	}

	public boolean useUml2ForComponent() {
		return skinParam.useUml2ForComponent();
	}

	public boolean stereotypePositionTop() {
		return skinParam.stereotypePositionTop();
	}

	public boolean useSwimlanes(UmlDiagramType type) {
		return skinParam.useSwimlanes(type);
	}

	public double getNodesep() {
		return skinParam.getNodesep();
	}

	public double getRanksep() {
		return skinParam.getRanksep();
	}

	public double getRoundCorner() {
		return skinParam.getRoundCorner();
	}

	public UStroke getThickness(LineParam param, Stereotype stereotype) {
		return skinParam.getThickness(param, stereotype);
	}

	public double maxMessageSize() {
		return skinParam.maxMessageSize();
	}

	public boolean strictUmlStyle() {
		return skinParam.strictUmlStyle();
	}

	public boolean forceSequenceParticipantUnderlined() {
		return skinParam.forceSequenceParticipantUnderlined();
	}

	public ConditionStyle getConditionStyle() {
		return skinParam.getConditionStyle();
	}

	public double minClassWidth() {
		return skinParam.minClassWidth();
	}

	public boolean sameClassWidth() {
		return skinParam.sameClassWidth();
	}

	public Rankdir getRankdir() {
		return skinParam.getRankdir();
	}

	public boolean useOctagonForActivity(Stereotype stereotype) {
		return skinParam.useOctagonForActivity(stereotype);
	}

	public IHtmlColorSet getIHtmlColorSet() {
		return skinParam.getIHtmlColorSet();
	}

	public boolean useUnderlineForHyperlink() {
		return skinParam.useUnderlineForHyperlink();
	}

	public HorizontalAlignment getDefaultTextAlignment() {
		return skinParam.getDefaultTextAlignment();
	}

	public double getPadding() {
		return skinParam.getPadding();
	}

	public int groupInheritance() {
		return skinParam.groupInheritance();
	}

	public boolean useGuillemet() {
		return skinParam.useGuillemet();
	}

	public boolean handwritten() {
		return skinParam.handwritten();
	}

	public String getSvgLinkTarget() {
		return skinParam.getSvgLinkTarget();
	}

	public String getMonospacedFamily() {
		return skinParam.getMonospacedFamily();
	}

	public Colors getColors(ColorParam param, Stereotype stereotype) {
		return skinParam.getColors(param, stereotype);
	}

	public int getTabSize() {
		return skinParam.getTabSize();
	}

	public boolean shadowingForNote(Stereotype stereotype) {
		return shadowingForNote(stereotype);
	}

	public int maxAsciiMessageLength() {
		return skinParam.maxAsciiMessageLength();
	}

}
