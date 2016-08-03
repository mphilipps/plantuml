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
package net.sourceforge.plantuml.skin;

public enum ComponentType {

	ARROW,

	ACTOR_HEAD, ACTOR_TAIL,
	
	BOUNDARY_HEAD, BOUNDARY_TAIL,
	CONTROL_HEAD, CONTROL_TAIL,
	ENTITY_HEAD, ENTITY_TAIL,
	DATABASE_HEAD, DATABASE_TAIL,

	//
	ALIVE_BOX_CLOSE_CLOSE, ALIVE_BOX_CLOSE_OPEN, ALIVE_BOX_OPEN_CLOSE, ALIVE_BOX_OPEN_OPEN,

	DELAY_TEXT, DESTROY,

	DELAY_LINE, PARTICIPANT_LINE, CONTINUE_LINE,

	//
	GROUPING_ELSE, GROUPING_HEADER, GROUPING_SPACE,
	//
	NEWPAGE, NOTE, NOTE_HEXAGONAL, NOTE_BOX, DIVIDER, REFERENCE, ENGLOBER,

	//
	PARTICIPANT_HEAD, PARTICIPANT_TAIL,

	//
	TITLE, SIGNATURE;

	public boolean isArrow() {
		return this == ARROW;
	}
}
