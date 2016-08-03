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
package net.sourceforge.plantuml.suggest;

public class VariatorAddOneCharBetweenWords extends VariatorIteratorAdaptor {

	private final String data;
	private final char toAdd;
	private int i;

	public VariatorAddOneCharBetweenWords(String data, char toAdd) {
		this.data = data;
		this.toAdd = toAdd;
		i++;
		ensureBetweenWords();
	}

	private void ensureBetweenWords() {
		while (i < data.length() && inWord()) {
			i++;
		}

	}

	private boolean inWord() {
		return Character.isLetterOrDigit(data.charAt(i - 1)) && Character.isLetterOrDigit(data.charAt(i));
	}

	@Override
	Variator getVariator() {
		return new Variator() {
			public String getData() {
				if (i > data.length() - 1) {
					return null;
				}
				return data.substring(0, i) + toAdd + data.substring(i);
			}

			public void nextStep() {
				i++;
				ensureBetweenWords();
			}
		};
	}
}
