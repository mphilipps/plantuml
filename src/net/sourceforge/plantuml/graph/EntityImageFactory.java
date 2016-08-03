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
package net.sourceforge.plantuml.graph;

import net.sourceforge.plantuml.cucadiagram.IEntity;

public class EntityImageFactory {

	public AbstractEntityImage createEntityImage(IEntity entity) {
		throw new UnsupportedOperationException();
//		if (entity.getEntityType() == LeafType.CLASS || entity.getEntityType() == LeafType.ANNOTATION
//				|| entity.getEntityType() == LeafType.ABSTRACT_CLASS || entity.getEntityType() == LeafType.INTERFACE
//				|| entity.getEntityType() == LeafType.ENUM) {
//			return new EntityImageClass(entity);
//		}
//		if (entity.getEntityType() == LeafType.ACTIVITY) {
//			return new EntityImageActivity(entity);
//		}
//		if (entity.getEntityType() == LeafType.NOTE) {
//			return new EntityImageNote(entity);
//		}
//		if (entity.getEntityType() == LeafType.POINT_FOR_ASSOCIATION) {
//			return new EntityImageActivityCircle(entity, 4, 4);
//		}
//		if (entity.getEntityType() == LeafType.CIRCLE_START) {
//			return new EntityImageActivityCircle(entity, 18, 18);
//		}
//		if (entity.getEntityType() == LeafType.CIRCLE_END) {
//			return new EntityImageActivityCircle(entity, 18, 11);
//		}
//		if (entity.getEntityType() == LeafType.BRANCH) {
//			return new EntityImageActivityBranch(entity);
//		}
//		if (entity.getEntityType() == LeafType.SYNCHRO_BAR) {
//			return new EntityImageActivityBar(entity);
//		}
//		if (entity.getEntityType() == LeafType.USECASE) {
//			return new EntityImageUsecase(entity);
//		}
//		if (entity.getEntityType() == LeafType.ACTOR) {
//			return new EntityImageActor(entity);
//		}
//		if (entity.getEntityType() == LeafType.CIRCLE_INTERFACE) {
//			return new EntityImageCircleInterface(entity);
//		}
//		if (entity.getEntityType() == LeafType.COMPONENT) {
//			return new EntityImageComponent(entity);
//		}
//		return new EntityImageDefault(entity);
	}

}
