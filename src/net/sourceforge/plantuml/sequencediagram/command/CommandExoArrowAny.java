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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;

abstract class CommandExoArrowAny extends SingleLineCommand2<SequenceDiagram> {

	public CommandExoArrowAny(RegexConcat pattern) {
		super(pattern);
	}

	@Override
	final protected CommandExecutionResult executeArg(SequenceDiagram diagram, RegexResult arg) {
		final String body = arg.getLazzy("ARROW_BODYA", 0) + arg.getLazzy("ARROW_BODYB", 0);
		final String dressing = arg.getLazzy("ARROW_DRESSING", 0);
		final Participant p = diagram.getOrCreateParticipant(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("PARTICIPANT", 0)));

		final boolean sync = dressing.length() == 2;
		final boolean dotted = body.contains("--");

		final Display labels;
		if (arg.get("LABEL", 0) == null) {
			labels = Display.create("");
		} else {
			labels = Display.getWithNewlines(arg.get("LABEL", 0));
		}

		final boolean bothDirection = arg.get("ARROW_BOTHDRESSING", 0) != null;

		ArrowConfiguration config = bothDirection ? ArrowConfiguration.withDirectionBoth() : ArrowConfiguration
				.withDirectionNormal();
		if (dotted) {
			config = config.withDotted();
		}
		if (sync) {
			config = config.withHead(ArrowHead.ASYNC);
		}
		config = config.withPart(getArrowPart(dressing));
		config = CommandArrow.applyStyle(arg.getLazzy("ARROW_STYLE", 0), config);
		final MessageExoType messageExoType = getMessageExoType(arg);

		if (messageExoType == MessageExoType.TO_RIGHT || messageExoType == MessageExoType.TO_LEFT) {
			if (containsSymbolExterior(arg, "o")) {
				config = config.withDecoration2(ArrowDecoration.CIRCLE);
			}
			if (containsSymbol(arg, "o")) {
				config = config.withDecoration1(ArrowDecoration.CIRCLE);
			}
		} else {
			if (containsSymbolExterior(arg, "o")) {
				config = config.withDecoration1(ArrowDecoration.CIRCLE);
			}
			if (containsSymbol(arg, "o")) {
				config = config.withDecoration2(ArrowDecoration.CIRCLE);
			}
		}

		if (containsSymbolExterior(arg, "x") || containsSymbol(arg, "x")) {
			config = config.withHead2(ArrowHead.CROSSX);
		}
		// if (messageExoType.getDirection() == 1) {
		// if (containsSymbolExterior(arg2, "x") || containsSymbol(arg2, "x")) {
		// config = config.withHead2(ArrowHead.CROSSX);
		// }
		// } else {
		// if (containsSymbolExterior(arg2, "x") || containsSymbol(arg2, "x")) {
		// config = config.withHead2(ArrowHead.CROSSX);
		// }
		// }

		final String error = diagram.addMessage(new MessageExo(p, messageExoType, labels, config, diagram
				.getNextMessageNumber(), isShortArrow(arg)));
		if (error != null) {
			return CommandExecutionResult.error(error);
		}

		final HtmlColor activationColor = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("LIFECOLOR", 0));

		if (diagram.isAutoactivate() && (config.getHead() == ArrowHead.NORMAL || config.getHead() == ArrowHead.ASYNC)) {
			if (config.isDotted()) {
				diagram.activate(p, LifeEventType.DEACTIVATE, null);
			} else {
				diagram.activate(p, LifeEventType.ACTIVATE, activationColor);
			}

		}

		return CommandExecutionResult.ok();
	}

	private ArrowPart getArrowPart(String dressing) {
		if (dressing.contains("/")) {
			return ArrowPart.BOTTOM_PART;
		}
		if (dressing.contains("\\")) {
			return ArrowPart.TOP_PART;
		}
		return ArrowPart.FULL;
	}

	abstract MessageExoType getMessageExoType(RegexResult arg2);

	private boolean isShortArrow(RegexResult arg2) {
		final String s = arg2.get("SHORT", 0);
		if (s != null && s.contains("?")) {
			return true;
		}
		return false;
	}

	private boolean containsSymbolExterior(RegexResult arg2, String symbol) {
		final String s = arg2.get("SHORT", 0);
		if (s != null && s.contains(symbol)) {
			return true;
		}
		return false;
	}

	private boolean containsSymbol(RegexResult arg2, String symbol) {
		final String s = arg2.get("ARROW_SUPPCIRCLE", 0);
		if (s != null && s.contains(symbol)) {
			return true;
		}
		return false;
	}

}
