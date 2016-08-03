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
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontPosition;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.ImgValign;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.ugraphic.sprite.Sprite;
import net.sourceforge.plantuml.utils.CharHidder;

public class StripeSimple implements Stripe {

	final private List<Atom> atoms = new ArrayList<Atom>();
	final private List<Command> commands = new ArrayList<Command>();

	private FontConfiguration fontConfiguration;

	final private StripeStyle style;
	final private ISkinSimple skinParam;

	public StripeSimple(FontConfiguration fontConfiguration, StripeStyle style, CreoleContext context,
			ISkinSimple skinParam, CreoleMode modeSimpleLine) {
		this.fontConfiguration = fontConfiguration;
		this.style = style;
		this.skinParam = skinParam;

		// class Splitter
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.ITALIC));
		if (modeSimpleLine == CreoleMode.FULL) {
			this.commands.add(CommandCreoleStyle.createCreole(FontStyle.UNDERLINE));
		}
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.UNDERLINE));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.UNDERLINE));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.STRIKE));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.STRIKE));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.STRIKE));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.WAVE));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.WAVE));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.WAVE));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.BACKCOLOR));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BACKCOLOR));
		this.commands.add(CommandCreoleSizeChange.create());
		this.commands.add(CommandCreoleSizeChange.createEol());
		this.commands.add(CommandCreoleColorChange.create());
		this.commands.add(CommandCreoleColorChange.createEol());
		this.commands.add(CommandCreoleColorAndSizeChange.create());
		this.commands.add(CommandCreoleColorAndSizeChange.createEol());
		this.commands.add(CommandCreoleExposantChange.create(FontPosition.EXPOSANT));
		this.commands.add(CommandCreoleExposantChange.create(FontPosition.INDICE));
		this.commands.add(CommandCreoleImg.create());
		this.commands.add(CommandCreoleOpenIcon.create());
		this.commands.add(CommandCreoleSprite.create());
		this.commands.add(CommandCreoleSpace.create());
		this.commands.add(CommandCreoleFontFamilyChange.create());
		this.commands.add(CommandCreoleFontFamilyChange.createEol());
		this.commands.add(CommandCreoleMonospaced.create(skinParam.getMonospacedFamily()));
		this.commands.add(CommandCreoleUrl.create(skinParam));
		this.commands.add(CommandCreoleSvgAttributeChange.create());

		final Atom header = style.getHeader(fontConfiguration, context);

		if (header != null) {
			atoms.add(header);
		}
	}

	public List<Atom> getAtoms() {
		if (atoms.size() == 0) {
			atoms.add(AtomText.create(" ", fontConfiguration));
		}
		return Collections.unmodifiableList(atoms);
	}

	public FontConfiguration getActualFontConfiguration() {
		return fontConfiguration;
	}

	public void setActualFontConfiguration(FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
	}

	public void analyzeAndAdd(String line) {
		if (line == null) {
			throw new IllegalArgumentException();
		}
		line = CharHidder.hide(line);
		if (style.getType() == StripeStyleType.HEADING) {
			atoms.add(AtomText.createHeading(line, fontConfiguration, style.getOrder()));
		} else if (style.getType() == StripeStyleType.HORIZONTAL_LINE) {
			atoms.add(CreoleHorizontalLine.create(fontConfiguration, line, style.getStyle(), skinParam));
		} else {
			modifyStripe(line);
		}
	}

	public void addImage(String src, double scale) {
		atoms.add(AtomImg.create(src, ImgValign.TOP, 0, scale));
	}

	public void addSpace(int size) {
		atoms.add(AtomSpace.create(size));
	}

	public void addUrl(Url url) {
		atoms.add(AtomText.createUrl(url, fontConfiguration));
	}

	public void addSprite(String src, double scale) {
		final Sprite sprite = skinParam.getSprite(src);
		if (sprite != null) {
			atoms.add(new AtomSprite(sprite.asTextBlock(fontConfiguration.getColor(), scale), fontConfiguration));
		}
	}

	public void addOpenIcon(String src) {
		final OpenIcon openIcon = OpenIcon.retrieve(src);
		if (openIcon != null) {
			atoms.add(new AtomOpenIcon(openIcon, fontConfiguration));
		}
	}

	private void modifyStripe(String line) {
		final StringBuilder pending = new StringBuilder();

		while (line.length() > 0) {
			final Command cmd = searchCommand(line);
			if (cmd == null) {
				pending.append(line.charAt(0));
				line = line.substring(1);
			} else {
				addPending(pending);
				line = cmd.executeAndGetRemaining(line, this);
			}
		}
		addPending(pending);
	}

	private void addPending(StringBuilder pending) {
		if (pending.length() == 0) {
			return;
		}
		atoms.add(AtomText.create(pending.toString(), fontConfiguration));
		pending.setLength(0);
	}

	private Command searchCommand(String line) {
		for (Command cmd : commands) {
			final int i = cmd.matchingSize(line);
			if (i != 0) {
				return cmd;
			}
		}
		return null;
	}

}
