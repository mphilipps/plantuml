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

public class EntityGenderUtils {

	static public EntityGender byEntityType(final LeafType type) {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return test.getEntityType() == type;
			}
		};
	}

	static public EntityGender byEntityAlone(final IEntity entity) {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return test.getUid().equals(entity.getUid());
			}
		};
	}

	static public EntityGender byStereotype(final String stereotype) {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				if (test.getStereotype() == null) {
					return false;
				}
				return stereotype.equals(test.getStereotype().getLabel(false));
			}
		};
	}

	static public EntityGender byPackage(final IGroup group) {
		if (EntityUtils.groupRoot(group)) {
			throw new IllegalArgumentException();
		}
		return new EntityGender() {
			public boolean contains(IEntity test) {
				if (EntityUtils.groupRoot(test.getParentContainer())) {
					return false;
				}
				if (group == test.getParentContainer()) {
					return true;
				}
				return false;
			}
		};
	}

	static public EntityGender and(final EntityGender g1, final EntityGender g2) {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return g1.contains(test) && g2.contains(test);
			}
		};
	}

	static public EntityGender all() {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return true;
			}
		};
	}

	static public EntityGender emptyMethods() {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return test.getBodier().getMethodsToDisplay().size() == 0;
			}
		};
	}

	static public EntityGender emptyFields() {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return test.getBodier().getFieldsToDisplay().size() == 0;
			}
		};
	}

	static public EntityGender emptyMembers() {
		return new EntityGender() {
			public boolean contains(IEntity test) {
				return test.getBodier().getMethodsToDisplay().size() == 0
						&& test.getBodier().getFieldsToDisplay().size() == 0;
			}
		};
	}

}
