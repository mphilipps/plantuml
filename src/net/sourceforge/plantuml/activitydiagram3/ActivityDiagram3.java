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
package net.sourceforge.plantuml.activitydiagram3;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlanes;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockCompressed;
import net.sourceforge.plantuml.graphic.TextBlockRecentred;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class ActivityDiagram3 extends UmlDiagram {

	enum SwimlaneStrategy {
		SWIMLANE_FORBIDDEN, SWIMLANE_ALLOWED;
	}

	private SwimlaneStrategy swimlaneStrategy;

	private final Swimlanes swinlanes = new Swimlanes(getSkinParam(), getPragma());

	private void manageSwimlaneStrategy() {
		if (swimlaneStrategy == null) {
			swimlaneStrategy = SwimlaneStrategy.SWIMLANE_FORBIDDEN;
		}
	}

	public CommandExecutionResult swimlane(String name, HtmlColor color, Display label) {
		if (swimlaneStrategy == null) {
			swimlaneStrategy = SwimlaneStrategy.SWIMLANE_ALLOWED;
		}
		if (swimlaneStrategy == SwimlaneStrategy.SWIMLANE_FORBIDDEN) {
			return CommandExecutionResult.error("This swimlane must be defined at the start of the diagram.");
		}

		swinlanes.swimlane(name, color, label);
		return CommandExecutionResult.ok();
	}

	private void setCurrent(Instruction ins) {
		swinlanes.setCurrent(ins);
	}

	private Instruction current() {
		return swinlanes.getCurrent();
	}

	private LinkRendering nextLinkRenderer() {
		return swinlanes.nextLinkRenderer();
	}

	public void addActivity(Display activity, BoxStyle style, Url url, Colors colors) {
		manageSwimlaneStrategy();
		final InstructionSimple ins = new InstructionSimple(activity, nextLinkRenderer(),
				swinlanes.getCurrentSwimlane(), style, url, colors);
		current().add(ins);
		setNextLinkRendererInternal(null);
		manageHasUrl(activity);
		if (url != null) {
			hasUrl = true;
		}
	}

	public CommandExecutionResult addGoto(String name) {
		final InstructionGoto ins = new InstructionGoto(swinlanes.getCurrentSwimlane(), name);
		current().add(ins);
		setNextLinkRendererInternal(null);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult addLabel(String name) {
		final InstructionLabel ins = new InstructionLabel(swinlanes.getCurrentSwimlane(), name);
		current().add(ins);
		setNextLinkRendererInternal(null);
		return CommandExecutionResult.ok();
	}

	public void start() {
		manageSwimlaneStrategy();
		current().add(new InstructionStart(swinlanes.getCurrentSwimlane()));
	}

	public void stop() {
		manageSwimlaneStrategy();
		current().add(new InstructionStop(swinlanes.getCurrentSwimlane(), nextLinkRenderer()));
	}

	public void end() {
		manageSwimlaneStrategy();
		current().add(new InstructionEnd(swinlanes.getCurrentSwimlane(), nextLinkRenderer()));
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("activity3", getClass());
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		// BUG42
		// COMPRESSION
		// TextBlock result = swinlanes;
		TextBlock result = new TextBlockCompressed(swinlanes);
		result = new TextBlockRecentred(result);
		final ISkinParam skinParam = getSkinParam();
		result = new AnnotatedWorker(this, skinParam).addAdd(result);
		final Dimension2D dim = TextBlockUtils.getMinMax(result).getDimension();
		final double margin = 10;
		final double dpiFactor = getDpiFactor(fileFormatOption, Dimension2DDouble.delta(dim, 2 * margin, 0));

		final ImageBuilder imageBuilder = new ImageBuilder(skinParam.getColorMapper(), dpiFactor, getSkinParam()
				.getBackgroundColor(), fileFormatOption.isWithMetadata() ? getMetadata() : null, getWarningOrError(),
				margin, margin, getAnimation(), getSkinParam().handwritten());
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);

	}

	private final double getDpiFactor(FileFormatOption fileFormatOption, final Dimension2D dim) {
		final double dpiFactor;
		final Scale scale = getScale();
		if (scale == null) {
			dpiFactor = getDpiFactor(fileFormatOption);
		} else {
			dpiFactor = scale.getScale(dim.getWidth(), dim.getHeight());
		}
		return dpiFactor;
	}

	// private final UFont getFont(FontParam fontParam) {
	// final ISkinParam skinParam = getSkinParam();
	// return skinParam.getFont(null, false, fontParam);
	// }
	//
	// private final HtmlColor getFontColor(FontParam fontParam, Stereotype stereotype2) {
	// final ISkinParam skinParam = getSkinParam();
	// return skinParam.getFontHtmlColor(stereotype2, fontParam);
	// }

	public void fork() {
		final InstructionFork instructionFork = new InstructionFork(current(), nextLinkRenderer());
		current().add(instructionFork);
		setNextLinkRendererInternal(null);
		setCurrent(instructionFork);
	}

	public CommandExecutionResult forkAgain() {
		if (current() instanceof InstructionFork) {
			final InstructionFork currentFork = (InstructionFork) current();
			currentFork.manageOutRendering(nextLinkRenderer());
			setNextLinkRendererInternal(null);
			currentFork.forkAgain();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public CommandExecutionResult endFork() {
		if (current() instanceof InstructionFork) {
			final InstructionFork currentFork = (InstructionFork) current();
			currentFork.manageOutRendering(nextLinkRenderer());
			setNextLinkRendererInternal(null);
			setCurrent(currentFork.getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public void split() {
		final InstructionSplit instructionSplit = new InstructionSplit(current(), nextLinkRenderer());
		setNextLinkRendererInternal(null);
		current().add(instructionSplit);
		setCurrent(instructionSplit);
	}

	public CommandExecutionResult splitAgain() {
		if (current() instanceof InstructionSplit) {
			((InstructionSplit) current()).splitAgain(nextLinkRenderer());
			setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find split");
	}

	public CommandExecutionResult endSplit() {
		if (current() instanceof InstructionSplit) {
			((InstructionSplit) current()).endSplit(nextLinkRenderer());
			setNextLinkRendererInternal(null);
			setCurrent(((InstructionSplit) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find split");
	}

	public void startIf(Display test, Display whenThen, HtmlColor color) {
		manageSwimlaneStrategy();
		final InstructionIf instructionIf = new InstructionIf(swinlanes.getCurrentSwimlane(), current(), test,
				whenThen, nextLinkRenderer(), color, getSkinParam());
		current().add(instructionIf);
		setNextLinkRendererInternal(null);
		setCurrent(instructionIf);
	}

	public CommandExecutionResult elseIf(Display test, Display whenThen, HtmlColor color) {
		if (current() instanceof InstructionIf) {
			final boolean ok = ((InstructionIf) current()).elseIf(test, whenThen, nextLinkRenderer(), color);
			if (ok == false) {
				return CommandExecutionResult.error("You cannot put an elseIf here");
			}
			setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult else2(Display whenElse) {
		if (current() instanceof InstructionIf) {
			final boolean result = ((InstructionIf) current()).swithToElse2(whenElse, nextLinkRenderer());
			if (result == false) {
				return CommandExecutionResult.error("Cannot find if");
			}
			setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult endif() {
		// System.err.println("Activity3::endif");
		if (current() instanceof InstructionIf) {
			((InstructionIf) current()).endif(nextLinkRenderer());
			setNextLinkRendererInternal(null);
			setCurrent(((InstructionIf) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public void startRepeat(HtmlColor color) {
		manageSwimlaneStrategy();
		final InstructionRepeat instructionRepeat = new InstructionRepeat(swinlanes.getCurrentSwimlane(), current(),
				nextLinkRenderer(), color);
		current().add(instructionRepeat);
		setCurrent(instructionRepeat);
		setNextLinkRendererInternal(null);

	}

	public CommandExecutionResult repeatWhile(Display label, Display yes, Display out, Display linkLabel,
			HtmlColor linkColor) {
		manageSwimlaneStrategy();
		if (current() instanceof InstructionRepeat) {
			final InstructionRepeat instructionRepeat = (InstructionRepeat) current();
			final LinkRendering back = new LinkRendering(linkColor);
			back.setDisplay(linkLabel);
			instructionRepeat.setTest(label, yes, out, nextLinkRenderer(), back);
			setCurrent(instructionRepeat.getParent());
			this.setNextLinkRendererInternal(null);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find repeat");

	}

	public void doWhile(Display test, Display yes, HtmlColor color) {
		manageSwimlaneStrategy();
		final InstructionWhile instructionWhile = new InstructionWhile(swinlanes.getCurrentSwimlane(), current(), test,
				nextLinkRenderer(), yes, color, getSkinParam());
		current().add(instructionWhile);
		setCurrent(instructionWhile);
	}

	public CommandExecutionResult endwhile(Display out) {
		if (current() instanceof InstructionWhile) {
			((InstructionWhile) current()).endwhile(nextLinkRenderer(), out);
			setNextLinkRendererInternal(null);
			setCurrent(((InstructionWhile) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find while");
	}

	final public CommandExecutionResult kill() {
		if (current().kill() == false) {
			return CommandExecutionResult.error("kill cannot be used here");
		}
		return CommandExecutionResult.ok();
	}

	public void startGroup(Display name, HtmlColor backColor, HtmlColor titleColor, HtmlColor borderColor) {
		manageSwimlaneStrategy();
		final InstructionGroup instructionGroup = new InstructionGroup(current(), name, backColor, titleColor,
				swinlanes.getCurrentSwimlane(), borderColor);
		current().add(instructionGroup);
		setCurrent(instructionGroup);
	}

	public CommandExecutionResult endGroup() {
		if (current() instanceof InstructionGroup) {
			setCurrent(((InstructionGroup) current()).getParent());
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find group");
	}

	private void setNextLinkRendererInternal(LinkRendering link) {
		// System.err.println("setNextLinkRendererInternal=" + link);
		swinlanes.setNextLinkRenderer(link);
	}

	private void setNextLink(LinkRendering linkRenderer) {
		// System.err.println("setNextLink=" + linkRenderer);
		if (current() instanceof InstructionCollection) {
			final Instruction last = ((InstructionCollection) current()).getLast();
			if (last instanceof InstructionWhile) {
				((InstructionWhile) last).afterEndwhile(linkRenderer);
			} else if (last instanceof InstructionIf) {
				((InstructionIf) last).afterEndwhile(linkRenderer);
			}
		}
		this.setNextLinkRendererInternal(linkRenderer);
	}

	private final Rose rose = new Rose();

	public void setLabelNextArrow(Display label) {
		if (current() instanceof InstructionWhile && ((InstructionWhile) current()).getLast() == null) {
			((InstructionWhile) current()).overwriteYes(label);
			return;
		}

		if (nextLinkRenderer() == null) {
			final HtmlColor arrowColor = rose.getHtmlColor(getSkinParam(), ColorParam.activityArrow);
			this.setNextLink(new LinkRendering(arrowColor));
		}
		nextLinkRenderer().setDisplay(label);
	}

	public void setColorNextArrow(HtmlColor color) {
		if (color == null) {
			return;
		}
		final LinkRendering link = new LinkRendering(color);
		setNextLink(link);
	}

	public CommandExecutionResult addNote(Display note, NotePosition position) {
		final boolean ok = current().addNote(note, position);
		if (ok == false) {
			return CommandExecutionResult.error("Cannot add note here");
		}
		manageHasUrl(note);
		return CommandExecutionResult.ok();
	}

	private boolean hasUrl = false;

	private void manageHasUrl(Display display) {
		if (display.hasUrl()) {
			hasUrl = true;
		}
	}

	@Override
	public boolean hasUrl() {
		return hasUrl;
	}

}
