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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.teoz.Bordered;
import net.sourceforge.plantuml.sequencediagram.teoz.LivingSpace;
import net.sourceforge.plantuml.sequencediagram.teoz.TileArguments;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Englober {

	final private ParticipantEnglober participantEnglober;
	final private List<Participant> participants = new ArrayList<Participant>();
	final private TileArguments tileArguments;
	final private Real core1;
	final private Real core2;

	@Deprecated
	public Englober(ParticipantEnglober participantEnglober, Participant first, ISkinParam skinParam, Skin skin) {
		this(participantEnglober, first, convertFunctionToBeRemoved(skinParam, skin));
	}

	private static TileArguments convertFunctionToBeRemoved(ISkinParam skinParam, Skin skin) {
		final TileArguments result = new TileArguments(TextBlockUtils.getDummyStringBounder(), null, skin, skinParam,
				null);
		return result;
	}

	public Englober(ParticipantEnglober participantEnglober, Participant first, TileArguments tileArguments) {
		if (tileArguments == null) {
			throw new IllegalArgumentException();
		}
		this.participantEnglober = participantEnglober;
		this.participants.add(first);
		this.tileArguments = tileArguments;
		final double preferredWidth = getPreferredWidth();
		if (tileArguments.getLivingSpaces() == null) {
			this.core1 = null;
			this.core2 = null;
		} else {
			this.core1 = getMiddle().addFixed(-preferredWidth / 2);
			this.core2 = getMiddle().addFixed(preferredWidth / 2);
		}
	}

	public final Participant getFirst2TOBEPRIVATE() {
		return participants.get(0);
	}

	public final Participant getLast2TOBEPRIVATE() {
		return participants.get(participants.size() - 1);
	}

	private Real getMiddle() {
		return RealUtils.middle(getPosB(), getPosD());
	}

	private Real getPosB() {
		return getFirstLivingSpace().getPosB();
	}

	private Real getPosD() {
		return getLastLivingSpace().getPosD(tileArguments.getStringBounder());
	}

	private Real getPosAA() {
		final LivingSpace previous = tileArguments.getLivingSpaces().previous(getFirstLivingSpace());
		if (previous == null) {
			return tileArguments.getOrigin();
		}
		return previous.getPosD(tileArguments.getStringBounder());
	}

	private Real getPosZZ() {
		final LivingSpace next = tileArguments.getLivingSpaces().next(getLastLivingSpace());
		if (next == null) {
			// return tileArguments.getMaxAbsolute();
			return null;
		}
		return next.getPosB();
	}

	private LivingSpace getFirstLivingSpace() {
		return tileArguments.getLivingSpace(getFirst2TOBEPRIVATE());
	}

	private LivingSpace getLastLivingSpace() {
		return tileArguments.getLivingSpace(getLast2TOBEPRIVATE());
	}

	private Component getComponent() {
		final ParticipantEnglober englober = getParticipantEnglober();
		final ISkinParam s = englober.getBoxColor() == null ? tileArguments.getSkinParam() : new SkinParamBackcolored(
				tileArguments.getSkinParam(), englober.getBoxColor());
		return tileArguments.getSkin().createComponent(ComponentType.ENGLOBER, null, s, englober.getTitle());
	}

	public final ParticipantEnglober getParticipantEnglober() {
		return participantEnglober;
	}

	public boolean contains(Participant p) {
		return participants.contains(p);
	}

	public void add(Participant p) {
		if (participants.contains(p)) {
			throw new IllegalArgumentException();
		}
		participants.add(p);
	}

	@Override
	public String toString() {
		return "ParticipantEngloberContexted:" + participantEnglober.getTitle().toString() + " " + participants;
	}

	private double getPreferredWidth() {
		return getComponent().getPreferredWidth(tileArguments.getStringBounder());
	}

	public double getPreferredHeight() {
		final Component comp = tileArguments.getSkin().createComponent(ComponentType.ENGLOBER, null,
				tileArguments.getSkinParam(), getParticipantEnglober().getTitle());
		return comp.getPreferredHeight(tileArguments.getStringBounder());
	}

	public void drawEnglober(UGraphic ug, double height, Context2D context) {
		final double x1 = getX1().getCurrentValue() - 4;
		final double x2 = getX2().getCurrentValue() + 4;

		final Dimension2DDouble dim = new Dimension2DDouble(x2 - x1, height);
		getComponent().drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
	}

	private Real getX2() {
		return RealUtils.max(getPosD(), core2);
	}

	private Real getX1() {
		return RealUtils.min(getPosB(), core1);
	}

	public void addInternalConstraints() {
		getX1().ensureBiggerThan(getPosAA().addFixed(10));
		final Real posZZ = getPosZZ();
		final Real limit = getX2().addFixed(10);
		if (posZZ != null) {
			posZZ.ensureBiggerThan(limit);
		}
	}

	public void addConstraintAfter(Englober current) {
		current.getX1().ensureBiggerThan(getX2().addFixed(10));
	}

	public Real getMinX(StringBounder stringBounder) {
		return getX1();
	}

	public Real getMaxX(StringBounder stringBounder) {
		return getX2().addFixed(10);
	}


}
