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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.factory.AbstractElementFactoryComplex;
import net.sourceforge.plantuml.salt.factory.ElementFactory;
import net.sourceforge.plantuml.salt.factory.ElementFactoryBorder;
import net.sourceforge.plantuml.salt.factory.ElementFactoryButton;
import net.sourceforge.plantuml.salt.factory.ElementFactoryCheckboxOff;
import net.sourceforge.plantuml.salt.factory.ElementFactoryCheckboxOn;
import net.sourceforge.plantuml.salt.factory.ElementFactoryDroplist;
import net.sourceforge.plantuml.salt.factory.ElementFactoryImage;
import net.sourceforge.plantuml.salt.factory.ElementFactoryLine;
import net.sourceforge.plantuml.salt.factory.ElementFactoryMenu;
import net.sourceforge.plantuml.salt.factory.ElementFactoryPyramid;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRadioOff;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRadioOn;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRetrieveFromDictonnary;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTab;
import net.sourceforge.plantuml.salt.factory.ElementFactoryText;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTextField;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTree;

public class SaltUtils {

	public static Element createElement(List<String> data) {
		final DataSourceImpl source = new DataSourceImpl(data);

		final Collection<AbstractElementFactoryComplex> cpx = new ArrayList<AbstractElementFactoryComplex>();

		final Dictionary dictionnary = new Dictionary();

		// cpx.add(new ElementFactorySimpleFrame(source, dictionnary));
		cpx.add(new ElementFactoryPyramid(source, dictionnary));
		cpx.add(new ElementFactoryBorder(source, dictionnary));

		for (AbstractElementFactoryComplex f : cpx) {
			addSimpleFactory(f, source, dictionnary);
		}
		for (AbstractElementFactoryComplex f1 : cpx) {
			for (AbstractElementFactoryComplex f2 : cpx) {
				f1.addFactory(f2);
			}
		}

		for (ElementFactory f : cpx) {
			if (f.ready()) {
				Log.info("Using " + f);
				return f.create().getElement();
			}
		}

		Log.println("data=" + data);
		throw new IllegalArgumentException();

	}

	private static void addSimpleFactory(final AbstractElementFactoryComplex cpxFactory, final DataSource source,
			Dictionary dictionnary) {
		cpxFactory.addFactory(new ElementFactoryMenu(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryTree(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryTab(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryLine(source));
		cpxFactory.addFactory(new ElementFactoryTextField(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryButton(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryDroplist(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryRadioOn(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryRadioOff(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryCheckboxOn(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryCheckboxOff(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryImage(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryRetrieveFromDictonnary(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryText(source, dictionnary));
	}

}
