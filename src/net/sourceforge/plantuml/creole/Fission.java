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
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;

public class Fission {

	private final Stripe stripe;
	private final double maxWidth;

	public Fission(Stripe stripe, double maxWidth) {
		this.stripe = stripe;
		this.maxWidth = maxWidth;
	}

	public List<Stripe> getSplitted(StringBounder stringBounder) {
		if (maxWidth == 0) {
			return Arrays.asList(stripe);
		}
		final List<Stripe> result = new ArrayList<Stripe>();
		StripeSimple current = new StripeSimple();
		for (Atom a1 : stripe.getAtoms()) {
			for (Atom atom : getSplitted(stringBounder, a1)) {
				final double width = atom.calculateDimension(stringBounder).getWidth();
				if (current.totalWidth + width > maxWidth) {
					result.add(current);
					current = new StripeSimple();
				}
				current.addAtom(atom, width);
			}
		}
		if (current.totalWidth > 0) {
			result.add(current);
		}
		return Collections.unmodifiableList(result);
	}

	private Collection<? extends Atom> getSplitted(StringBounder stringBounder, Atom atom) {
		if (atom instanceof AtomText) {
			return ((AtomText) atom).getSplitted(stringBounder, maxWidth);
		}
		return Collections.singleton(atom);
	}

	private List<Stripe> getSplittedSimple() {
		final StripeSimple result = new StripeSimple();
		for (Atom atom : stripe.getAtoms()) {
			result.addAtom(atom, 0);

		}
		return Arrays.asList((Stripe) result);
	}

	static class StripeSimple implements Stripe {

		private final List<Atom> atoms = new ArrayList<Atom>();
		private double totalWidth;

		public List<Atom> getAtoms() {
			return Collections.unmodifiableList(atoms);
		}

		private void addAtom(Atom atom, double width) {
			this.atoms.add(atom);
			this.totalWidth += width;
		}

	}

}
