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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorate;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class FtileWithConnection extends FtileDecorate {

	private final List<Connection> connections = new ArrayList<Connection>();

	FtileWithConnection(Ftile ftile, Collection<Connection> connections) {
		super(ftile);
		if (connections == null || connections.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.connections.addAll(connections);
	}

	@Override
	public String toString() {
		return super.toString() + " " + connections;
	}

	public FtileWithConnection(Ftile ftile, Connection connection) {
		this(ftile, Arrays.asList(connection));
		if (connection == null) {
			throw new IllegalArgumentException();
		}
	}

	public void drawU(UGraphic ug) {
		getFtileDelegated().drawU(ug);
		for (Connection c : connections) {
			ug.draw(c);
		}
	}

	public Collection<Connection> getInnerConnections() {
		final List<Connection> result = new ArrayList<Connection>(super.getInnerConnections());
		result.addAll(connections);
		return Collections.unmodifiableList(connections);
	}

}
