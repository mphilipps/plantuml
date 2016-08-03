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

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.EventWithDeactivate;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public class CommandReturn extends SingleLineCommand<SequenceDiagram> {

	public CommandReturn() {
		super("(?i)^return[%s]*(.*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram sequenceDiagram, List<String> arg) {

		Message message = sequenceDiagram.getActivatingMessage();
		boolean doDeactivation = true;
		if (message == null) {
			final EventWithDeactivate last = sequenceDiagram.getLastEventWithDeactivate();
			if (last instanceof Message == false) {
				return CommandExecutionResult.error("Nowhere to return to.");
			}
			message = (Message) last;
			doDeactivation = false;
		}

		final ArrowConfiguration arrow = message.getArrowConfiguration().withDotted();

		sequenceDiagram.addMessage(
				new Message(message.getParticipant2(), message.getParticipant1(), Display.getWithNewlines(arg
				.get(0)), arrow, sequenceDiagram.getNextMessageNumber()));

		if (doDeactivation) {
			final String error = sequenceDiagram.activate(message.getParticipant2(), LifeEventType.DEACTIVATE, null);
			if (error != null) {
				return CommandExecutionResult.error(error);
			}
		}
		return CommandExecutionResult.ok();

	}

}
