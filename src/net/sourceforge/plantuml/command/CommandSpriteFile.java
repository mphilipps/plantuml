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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.ugraphic.sprite.SpriteImage;
import net.sourceforge.plantuml.version.PSystemVersion;

public class CommandSpriteFile extends SingleLineCommand2<UmlDiagram> {

	public CommandSpriteFile() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("sprite[%s]+\\$?"), //
				new RegexLeaf("NAME", "([\\p{L}0-9_]+)[%s]*"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("FILE", "(.*)"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram system, RegexResult arg) {
		final String src = arg.get("FILE", 0);
		final BufferedImage im;
		try {
			if (src.startsWith("jar:")) {
				final String inner = src.substring(4) + ".png";
				final InputStream is = SpriteImage.getInternalSprite(inner);
				if (is == null) {
					return CommandExecutionResult.error("No such internal sprite: " + inner);
				}
				im = ImageIO.read(is);
			} else {
				final File f = FileSystem.getInstance().getFile(src);
				if (f.exists() == false) {
					return CommandExecutionResult.error("File does not exist: " + src);
				}
				im = ImageIO.read(f);
			}
		} catch (IOException e) {
			Log.error("Error reading " + src + " " + e);
			return CommandExecutionResult.error("Cannot read: " + src);
		}
		system.addSprite(arg.get("NAME", 0), new SpriteImage(im));
		return CommandExecutionResult.ok();
	}

}
