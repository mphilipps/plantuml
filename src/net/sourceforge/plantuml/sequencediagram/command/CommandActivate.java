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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandActivate extends SingleLineCommand<SequenceDiagram> {

	public CommandActivate() {
		super(
				"(?i)^(activate|deactivate|destroy|create)[%s]+([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*(#\\w+)?(?:[%s]+(#\\w+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, List<String> arg) {
		final LifeEventType type = LifeEventType.valueOf(StringUtils.goUpperCase(arg.get(0)));
		final Participant p = diagram.getOrCreateParticipant(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(1)));
		final HtmlColor backColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get(2));
		final HtmlColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get(3));
		final String error = diagram.activate(p, type, backColor, lineColor);
		if (error == null) {
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error(error);

	}

}
