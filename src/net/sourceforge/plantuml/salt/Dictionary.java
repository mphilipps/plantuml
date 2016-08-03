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
package net.sourceforge.plantuml.salt;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.creole.CommandCreoleMonospaced;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.WrappedElement;
import net.sourceforge.plantuml.ugraphic.sprite.Sprite;

public class Dictionary implements SpriteContainer, ISkinSimple {

	private final Map<String, Element> data = new HashMap<String, Element>();

	public void put(String name, Element element) {
		data.put(name, element);
	}

	public Element get(String name) {
		final Element result = data.get(name);
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return new WrappedElement(result);
	}

	public Sprite getSprite(String name) {
		return null;
	}

	public String getValue(String key) {
		return null;
	}

	public double getPadding() {
		return 0;
	}

	public boolean useGuillemet() {
		return true;
	}

	public String getMonospacedFamily() {
		return CommandCreoleMonospaced.MONOSPACED;
	}

	public int getTabSize() {
		return 8;
	}

	public IHtmlColorSet getIHtmlColorSet() {
		return new HtmlColorSetSimple();
	}

}
