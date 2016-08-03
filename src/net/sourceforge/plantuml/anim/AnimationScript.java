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
package net.sourceforge.plantuml.anim;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class AnimationScript {

	private final ScriptEngine engine;

	public AnimationScript() {

		final ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");

		// ScriptEngineManager manager = new ScriptEngineManager();
		// List<ScriptEngineFactory> factories = manager.getEngineFactories();
		// for (ScriptEngineFactory factory : factories) {
		// System.out.println("Name : " + factory.getEngineName());
		// System.out.println("Version : " + factory.getEngineVersion());
		// System.out.println("Language name : " + factory.getLanguageName());
		// System.out.println("Language version : " + factory.getLanguageVersion());
		// System.out.println("Extensions : " + factory.getExtensions());
		// System.out.println("Mime types : " + factory.getMimeTypes());
		// System.out.println("Names : " + factory.getNames());
		//
		// }

	}

	public String eval(String line) throws ScriptException {
		final ScriptContext context = engine.getContext();
		final StringWriter sw = new StringWriter();
		context.setWriter(new PrintWriter(sw));
		engine.eval(line, context);
		final String result = sw.toString();
		return result;
	}
}
