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
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class CommandUrl extends SingleLineCommand<AbstractEntityDiagram> {

	public CommandUrl() {
		super("(?i)^url[%s]*(?:of|for)?[%s]+([\\p{L}0-9_.]+|[%g][^%g]+[%g])[%s]+(?:is)?[%s]*(" + UrlBuilder.getRegexp() + ")$");
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, List<String> arg) {
		final Code code = Code.of(arg.get(0));
		final String urlString = arg.get(1);
		final IEntity entity;
		if (diagram.leafExist(code)) {
			entity = diagram.getOrCreateLeaf(code, null, null);
		} else if (diagram.isGroup(code)) {
			entity = diagram.getGroup(code);
		} else {
			return CommandExecutionResult.error(code + " does not exist");
		}
		// final IEntity entity = diagram.getOrCreateLeaf(code, null);
		final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
		final Url url = urlBuilder.getUrl(urlString);
		entity.addUrl(url);
		return CommandExecutionResult.ok();
	}

}
