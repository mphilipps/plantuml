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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.Colors;

public class Note extends AbstractEvent implements Event, SpecificBackcolorable {

	private final Participant p;
	private final Participant p2;

	private final Display strings;

	private final NotePosition position;
	private NoteStyle style = NoteStyle.NORMAL;

	// private Stereotype stereotype;

	private final Url url;

	public Note(Participant p, NotePosition position, Display strings) {
		this(p, null, position, strings);
	}

	public Note(Participant p, Participant p2, Display strings) {
		this(p, p2, NotePosition.OVER_SEVERAL, strings);
	}

	private Note(Participant p, Participant p2, NotePosition position, Display strings) {
		this.p = p;
		this.p2 = p2;
		this.position = position;
		if (strings != null && strings.size() > 0) {
			final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.AT_START);
			final String s = strings.asStringWithHiddenNewLine();
			this.url = urlBuilder.getUrl(s);
		} else {
			this.url = null;
		}

		if (this.url == null) {
			this.strings = strings;
		} else {
			this.strings = strings.removeUrlHiddenNewLineUrl();
		}
	}

	public Participant getParticipant() {
		return p;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public Display getStrings() {
		return strings;
	}

	public NotePosition getPosition() {
		return position;
	}

	public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	// public void setSpecificColorTOBEREMOVED(ColorType type, HtmlColor color) {
	// if (color != null) {
	// this.colors = colors.add(type, color);
	// }
	// }

	private Colors colors = Colors.empty();

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public boolean dealWith(Participant someone) {
		return p == someone || p2 == someone;
	}

	public Url getUrl() {
		return url;
	}

	public boolean hasUrl() {
		return url != null;
	}

	public final NoteStyle getStyle() {
		return style;
	}

	public final void setStyle(NoteStyle style) {
		this.style = style;
	}

	public ISkinParam getSkinParamBackcolored(ISkinParam skinParam) {
		// return new SkinParamBackcolored(skinParam, getColors(skinParam).getColor(ColorType.BACK));
		return colors.mute(skinParam);
	}

	public void setStereotype(Stereotype stereotype) {
		// this.stereotype = stereotype;
	}

	@Override
	public String toString() {
		return super.toString() + " " + strings;
	}

}
