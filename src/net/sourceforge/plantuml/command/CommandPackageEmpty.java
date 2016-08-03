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
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackageEmpty extends SingleLineCommand<AbstractEntityDiagram> {

	public CommandPackageEmpty() {
		super(
				"(?i)^package[%s]+([%g][^%g]+[%g]|[^#%s{}]*)(?:[%s]+as[%s]+([\\p{L}0-9_.]+))?[%s]*(#[0-9a-fA-F]{6}|#?\\w+)?[%s]*\\{[%s]*\\}$");
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, List<String> arg) {
		final Code code;
		final String display;
		if (arg.get(1) == null) {
			if (StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0)).length() == 0) {
				code = Code.of("##" + UniqueSequence.getValue());
				display = null;
			} else {
				code = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0)));
				display = code.getFullName();
			}
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0));
			code = Code.of(arg.get(1));
		}
		final IGroup currentPackage = diagram.getCurrentGroup();
		final IEntity p = diagram.getOrCreateGroup(code, Display.getWithNewlines(display), GroupType.PACKAGE,
				currentPackage);
		final String color = arg.get(2);
		if (color != null) {
			p.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(color));
		}
		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}
