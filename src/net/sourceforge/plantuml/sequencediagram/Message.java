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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public final class Message extends AbstractMessage {

	final private Participant p1;
	final private Participant p2;

	public Message(Participant p1, Participant p2, Display label, ArrowConfiguration arrowConfiguration,
			String messageNumber) {
		super(label, arrowConfiguration, messageNumber);
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public String toString() {
		return super.toString() + " " + p1 + "->" + p2 + " " + getLabel();
	}

	public Participant getParticipant1() {
		return p1;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public boolean dealWith(Participant someone) {
		return someone == p1 || someone == p2;
	}

	@Override
	public boolean compatibleForCreate(Participant p) {
		return p1 != p && p2 == p;
	}

	public boolean isSelfMessage() {
		return p1 == p2;
	}

}
