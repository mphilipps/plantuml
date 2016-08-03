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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek2;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CucaDiagramFileMakerHectorB2 implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private SkeletonConfiguration configuration;

	private double singleWidth;
	private double singleHeight;
	private double nodeDistanceX = 30;
	private double nodeDistanceY = 50;

	private UnlinarCompressedPlan unlinarCompressedPlan;

	public CucaDiagramFileMakerHectorB2(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	final private Map<Pin, IEntityImage> images = new LinkedHashMap<Pin, IEntityImage>();
	// final private Map<Pin, Box2D> boxes = new LinkedHashMap<Pin, Box2D>();

	final private Map<Link, PinLink> links = new LinkedHashMap<Link, PinLink>();

	// final private List<Box2D> forbidden = new ArrayList<Box2D>();

	private double getX(Pin pin) {
		return nodeDistanceX * configuration.getCol(pin);
	}

	private double getY(Pin pin) {
		return nodeDistanceY * pin.getRow();
	}

	private Point2D getPoint(Pin pin) {
		return new Point2D.Double(getX(pin), getY(pin));
	}

	// private double getCenterX(Pin pin) {
	// return singleWidth * configuration.getCol(pin) + singleWidth / 2.0;
	// }
	//
	// private double getCenterY(Pin pin) {
	// return singleHeight * pin.getRow() + singleHeight / 2.0;
	// }

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		final PinFactory pinFactory = new PinFactory();
		final SkeletonBuilder skeletonBuilder = new SkeletonBuilder();
		links.clear();
		for (Link link : diagram.getLinks()) {
			final PinLink pinLink = pinFactory.createPinLink(link);
			links.put(link, pinLink);
			skeletonBuilder.add(pinLink);
		}

		final Skeleton skeleton = skeletonBuilder.createSkeletons().get(0);
		this.configuration = SkeletonConfigurationUtils.getBest(skeleton);

		this.singleWidth = 0;
		this.singleHeight = 0;

		images.clear();
		for (Pin pin : skeleton.getPins()) {
			final ILeaf leaf = (ILeaf) pin.getUserData();
			final IEntityImage image = computeImage(leaf);
			final Dimension2D dim = TextBlockUtils.getDimension(image);
			if (dim.getWidth() > singleWidth) {
				singleWidth = dim.getWidth();
			}
			if (dim.getHeight() > singleHeight) {
				singleHeight = dim.getHeight();
			}
			images.put(pin, image);
		}

		unlinarCompressedPlan = new UnlinarCompressedPlan(singleWidth, nodeDistanceX, singleHeight, nodeDistanceY);

		MinMax minMax = MinMax.getEmpty(false);
		for (Pin pin : skeleton.getPins()) {
			minMax = minMax.addPoint(unlinarCompressedPlan.uncompress(getX(pin), getY(pin),
					UnlinearCompression.Rounding.BORDER_2));
		}

		final double borderMargin = 10;

		final Dimension2D dimTotal = new Dimension2DDouble(2 * borderMargin + minMax.getMaxX(), 2 * borderMargin
				+ minMax.getMaxY());
		UGraphic2 ug = null; // fileFormatOption.createUGraphic(diagram.getColorMapper(), diagram.getDpiFactor(fileFormatOption),
				// dimTotal, null, false);
		ug = (UGraphic2) ug.apply(new UTranslate(borderMargin, borderMargin));

		for (PinLink pinLink : skeleton.getPinLinks()) {
			drawPinLink(ug, pinLink);
		}

		for (Pin pin : skeleton.getPins()) {
			drawPin(ug, pin);
		}

//		ug.writeImageTOBEMOVED(os, null, diagram.getDpi(fileFormatOption));
//		return new ImageDataSimple(dimTotal);
		throw new UnsupportedOperationException();
	}

	private void drawPin(UGraphic ug, Pin pin) {
		final Point2D pt = unlinarCompressedPlan.uncompress(getPoint(pin), UnlinearCompression.Rounding.BORDER_1);

		final double x = pt.getX();
		final double y = pt.getY();
		final UShape rect = new URectangle(unlinarCompressedPlan.getInnerX(), unlinarCompressedPlan.getInnerY());
		ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UChangeBackColor(HtmlColorUtils.BLACK)).apply(
				new UTranslate(x, y)).draw(rect);
	}

	private void drawPinLink(UGraphic ug, PinLink pinLink) {
		final Point2D pp1 = getPoint(pinLink.getPin1());
		final Point2D pp2 = getPoint(pinLink.getPin2());

		final Rose rose = new Rose();
		final HtmlColor color = rose.getHtmlColor(diagram.getSkinParam(), ColorParam.classArrow);
		
		final HectorPath path = unlinarCompressedPlan.uncompressSegment(pp1, pp2);
		path.draw(ug, color);

//		final Point2D p1 = unlinarCompressedPlan.uncompress(pp1, UnlinearCompression.Rounding.CENTRAL);
//		final Point2D p2 = unlinarCompressedPlan.uncompress(pp2, UnlinearCompression.Rounding.CENTRAL);
//		final SmartConnection connection = new SmartConnection(p1, p2, new ArrayList<Box2D>());
//		connection.draw(ug, color);
	}

	private IEntityImage computeImage(final ILeaf leaf) {
		final IEntityImage image = CucaDiagramFileMakerSvek2.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null, null, null);
		return image;
	}
}
