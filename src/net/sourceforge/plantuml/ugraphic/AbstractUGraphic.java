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
package net.sourceforge.plantuml.ugraphic;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUGraphic<O> extends AbstractCommonUGraphic {

	private final O g2d;

	private final Map<Class<? extends UShape>, UDriver<O>> drivers = new HashMap<Class<? extends UShape>, UDriver<O>>();

	public AbstractUGraphic(ColorMapper colorMapper, O g2d) {
		super(colorMapper);
		this.g2d = g2d;
	}

	protected AbstractUGraphic(AbstractUGraphic<O> other) {
		super(other);
		this.g2d = other.g2d;
		// this.drivers.putAll(other.drivers);
	}

	protected final O getGraphicObject() {
		return g2d;
	}

	protected boolean manageHiddenAutomatically() {
		return true;
	}

	final protected void registerDriver(Class<? extends UShape> cl, UDriver<O> driver) {
		this.drivers.put(cl, driver);
	}

	public final void draw(UShape shape) {
		if (shape instanceof UEmpty) {
			return;
		}
		final UDriver<O> driver = drivers.get(shape.getClass());
		if (driver == null) {
			throw new UnsupportedOperationException(shape.getClass().toString() + " " + this.getClass());
		}
		if (getParam().isHidden() && manageHiddenAutomatically()) {
			return;
		}
		beforeDraw();
		if (shape instanceof Scalable) {
			final double scale = getParam().getScale();
			shape = ((Scalable) shape).getScaled(scale);
			driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), g2d);
		} else {
			driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), g2d);
		}
		afterDraw();
	}

	protected void beforeDraw() {
	}

	protected void afterDraw() {
	}

}
