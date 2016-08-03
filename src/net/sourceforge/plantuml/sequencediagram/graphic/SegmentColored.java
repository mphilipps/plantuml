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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class SegmentColored {

	final private Segment segment;
	final private SymbolContext colors;
	final private boolean shadowing;
	final private double pos1Initial;

	SegmentColored(double pos1, double pos2, SymbolContext colors, boolean shadowing) {
		this(new Segment(pos1, pos2), colors, shadowing, pos1);
	}

	private SegmentColored(Segment segment, SymbolContext colors, boolean shadowing, double pos1Initial) {
		this.segment = segment;
		this.colors = colors;
		this.shadowing = shadowing;
		this.pos1Initial = pos1Initial;
	}

	public HtmlColor getSpecificBackColor() {
		if (colors == null) {
			return null;
		}
		return colors.getBackColor();
	}

	public HtmlColor getSpecificLineColor() {
		if (colors == null) {
			return null;
		}
		return colors.getForeColor();
	}

	@Override
	public boolean equals(Object obj) {
		final SegmentColored this2 = (SegmentColored) obj;
		return this.segment.equals(this2.segment);
	}

	@Override
	public int hashCode() {
		return this.segment.hashCode();
	}

	@Override
	public String toString() {
		return this.segment.toString();
	}

	public void drawU(UGraphic ug, Component compAliveBox, int level) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UTranslate((level - 1) * compAliveBox.getPreferredWidth(stringBounder) / 2, segment.getPos1()));
		final Dimension2D dim = new Dimension2DDouble(compAliveBox.getPreferredWidth(stringBounder), segment.getPos2()
				- segment.getPos1());
		compAliveBox.drawU(ug, new Area(dim), new SimpleContext2D(false));
	}

	public Collection<SegmentColored> cutSegmentIfNeed(Collection<Segment> allDelays) {
		return new Coll2(segment.cutSegmentIfNeed(allDelays), segment.getPos1());
	}

	public double getPos1Initial() {
		return pos1Initial;
	}

	public SegmentColored merge(SegmentColored this2) {
		final Segment merge = this.segment.merge(this2.segment);
		return new SegmentColored(merge, colors, shadowing, merge.getPos1());
	}

	public final Segment getSegment() {
		return segment;
	}

	class Iterator2 implements Iterator<SegmentColored> {

		private final Iterator<Segment> it;
		private final double pos1Initial;

		public Iterator2(Iterator<Segment> it, double pos1Initial) {
			this.it = it;
			this.pos1Initial = pos1Initial;
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public SegmentColored next() {
			return new SegmentColored(it.next(), colors, shadowing, pos1Initial);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	class Coll2 extends AbstractCollection<SegmentColored> {

		private final Collection<Segment> col;
		private final double pos1Initial;

		public Coll2(Collection<Segment> col, double pos1Initial) {
			this.col = col;
			this.pos1Initial = pos1Initial;
		}

		@Override
		public Iterator<SegmentColored> iterator() {
			return new Iterator2(col.iterator(), pos1Initial);
		}

		@Override
		public int size() {
			return col.size();
		}

	}

}
