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
package net.sourceforge.plantuml.cucadiagram;

import java.util.List;

class Magma {

	private final CucaDiagram system;
	private final List<ILeaf> standalones;
	private final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();

	public Magma(CucaDiagram system, List<ILeaf> standalones) {
		this.system = system;
		this.standalones = standalones;
	}

	public void putInSquare() {
		final SquareLinker<ILeaf> linker = new SquareLinker<ILeaf>() {
			public void topDown(ILeaf top, ILeaf down) {
				system.addLink(new Link(top, down, linkType, Display.NULL, 2));
			}

			public void leftRight(ILeaf left, ILeaf right) {
				system.addLink(new Link(left, right, linkType, Display.NULL, 1));
			}
		};
		new SquareMaker<ILeaf>().putInSquare(standalones, linker);
	}

	public IGroup getContainer() {
		final IGroup parent = standalones.get(0).getParentContainer();
		if (parent == null) {
			return null;
		}
		return parent.getParentContainer();
	}

	public boolean isComplete() {
		final IGroup parent = getContainer();
		if (parent == null) {
			return false;
		}
		return parent.size() == standalones.size();
	}

	private int squareSize() {
		return SquareMaker.computeBranch(standalones.size());
	}

	private ILeaf getTopLeft() {
		return standalones.get(0);
	}

	private ILeaf getBottomLeft() {
		int result = SquareMaker.getBottomLeft(standalones.size());
		return standalones.get(result);
	}

	private ILeaf getTopRight() {
		final int s = squareSize();
		return standalones.get(s - 1);
	}

	@Override
	public String toString() {
		return standalones.get(0).getParentContainer() + " " + standalones.toString() + " " + isComplete();
	}

	public void linkToDown(Magma down) {
		system.addLink(new Link(this.getBottomLeft(), down.getTopLeft(), linkType, Display.NULL, 2));

	}

	public void linkToRight(Magma right) {
		system.addLink(new Link(this.getTopRight(), right.getTopLeft(), linkType, Display.NULL, 1));
	}

}
