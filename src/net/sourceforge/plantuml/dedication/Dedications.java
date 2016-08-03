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
package net.sourceforge.plantuml.dedication;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.SignatureUtils;

public class Dedications {

	private static final Map<String, Dedication> all = new HashMap<String, Dedication>();

	static {
		addNormal("Write your own dedication!");
		addCrypted("RyHcSMMTGTW-ZlDelq18AwlwfbZZdfo-Yo0ketavjyFxRAFoKx1mAI032reWO3p4Mog-AV6jFqjXfi8G6pKo7G00");
	}

	private static void addNormal(String sentence) {
		final String signature = SignatureUtils.getSignatureSha512(keepLetter(sentence));
		addCrypted(signature);
	}

	private static void addCrypted(String signature) {
		all.put(signature, new Dedication(signature));
	}

	private Dedications() {
	}

	public static Dedication get(String line) {
		final String signature = SignatureUtils.getSignatureSha512(keepLetter(line));
		return all.get(signature);
	}

	public static String keepLetter(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (Character.isLetter(c)) {
				sb.append(c);
			}
		}
		return sb.toString().toUpperCase();
	}

}
