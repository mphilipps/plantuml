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
package net.sourceforge.plantuml.compositediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.compositediagram.CompositeDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkBlock extends SingleLineCommand<CompositeDiagram> {

	public CommandLinkBlock() {
		super("(?i)^([\\p{L}0-9_.]+)[%s]*(\\[\\]|\\*\\))?([=-]+|\\.+)(\\[\\]|\\(\\*)?[%s]*([\\p{L}0-9_.]+)[%s]*(?::[%s]*(\\S*+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(CompositeDiagram diagram, List<String> arg) {
		final IEntity cl1 = diagram.getOrCreateLeaf(Code.of(arg.get(0)), null, null);
		final IEntity cl2 = diagram.getOrCreateLeaf(Code.of(arg.get(4)), null, null);

		final String deco1 = arg.get(1);
		final String deco2 = arg.get(3);
		LinkType linkType = new LinkType(getLinkDecor(deco1), getLinkDecor(deco2));
		
		if ("*)".equals(deco1)) {
			linkType = linkType.getInterfaceProvider();
		} else if ("(*".equals(deco2)) {
			linkType = linkType.getInterfaceUser();
		}

		final String queue = arg.get(2);

		final Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(arg.get(5)), queue.length());
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkDecor getLinkDecor(String s) {
		if ("[]".equals(s)) {
			return LinkDecor.SQUARRE_toberemoved;
		}
		return LinkDecor.NONE;
	}

}
