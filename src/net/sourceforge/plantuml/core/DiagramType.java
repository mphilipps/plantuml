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
package net.sourceforge.plantuml.core;

public enum DiagramType {
	UML, DITAA, DOT, PROJECT, JCCKIT, SALT, TURING, FLOW, CREOLE, JUNGLE, CUTE, UNKNOWN;

	static public DiagramType getTypeFromArobaseStart(String s) {
		s = s.toLowerCase();
//		if (s.startsWith("@startuml2")) {
//			return UML2;
//		}
		if (s.startsWith("@startuml")) {
			return UML;
		}
		if (s.startsWith("@startdot")) {
			return DOT;
		}
		if (s.startsWith("@startjcckit")) {
			return JCCKIT;
		}
		if (s.startsWith("@startditaa")) {
			return DITAA;
		}
		if (s.startsWith("@startproject")) {
			return PROJECT;
		}
		if (s.startsWith("@startsalt")) {
			return SALT;
		}
		if (s.startsWith("@startturing")) {
			return TURING;
		}
		if (s.startsWith("@startflow")) {
			return FLOW;
		}
		if (s.startsWith("@startcreole")) {
			return CREOLE;
		}
		if (s.startsWith("@starttree")) {
			return JUNGLE;
		}
		if (s.startsWith("@startcute")) {
			return CUTE;
		}
		return UNKNOWN;
	}
}
