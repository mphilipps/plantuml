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
package net.sourceforge.plantuml.descdiagram;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.StringUtils;

public class DescriptionDiagram extends AbstractEntityDiagram {

	private String namespaceSeparator = null;

	@Override
	public ILeaf getOrCreateLeaf(Code code, LeafType type, USymbol symbol) {
		if (namespaceSeparator != null) {
			code = code.withSeparator(namespaceSeparator);
		}
		if (namespaceSeparator != null && code.getFullName().contains(namespaceSeparator)) {
			// System.err.println("code=" + code);
			final Code fullyCode = code;
			// final String namespace = fullyCode.getNamespace(getLeafs());
			// System.err.println("namespace=" + namespace);
		}
		if (type == null) {
			String code2 = code.getFullName();
			if (code2.startsWith("[") && code2.endsWith("]")) {
				final USymbol sym = getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2 : USymbol.COMPONENT1;
				return getOrCreateLeafDefault(code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
						LeafType.DESCRIPTION, sym);
			}
			if (code2.startsWith(":") && code2.endsWith(":")) {
				return getOrCreateLeafDefault(code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:"),
						LeafType.DESCRIPTION, USymbol.ACTOR);
			}
			if (code2.startsWith("()")) {
				code2 = StringUtils.trin(code2.substring(2));
				code2 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code2);
				return getOrCreateLeafDefault(Code.of(code2), LeafType.DESCRIPTION, USymbol.INTERFACE);
			}
			code = code.eventuallyRemoveStartingAndEndingDoubleQuote("\"([:");
			return getOrCreateLeafDefault(code, LeafType.STILL_UNKNOWN, symbol);
		}
		return getOrCreateLeafDefault(code, type, symbol);
	}

	// @Override
	// public ILeaf createLeaf(Code code, List<? extends CharSequence> display, LeafType type) {
	// if (type != LeafType.COMPONENT) {
	// return super.createLeaf(code, display, type);
	// }
	// code = code.getFullyQualifiedCode(getCurrentGroup());
	// if (super.leafExist(code)) {
	// throw new IllegalArgumentException("Already known: " + code);
	// }
	// return createEntityWithNamespace(code, display, type);
	// }

	// private ILeaf createEntityWithNamespace(Code fullyCode, List<? extends CharSequence> display, LeafType type) {
	// IGroup group = getCurrentGroup();
	// final String namespace = fullyCode.getNamespace(getLeafs());
	// if (namespace != null && (EntityUtils.groupRoot(group) || group.getCode().equals(namespace) == false)) {
	// group = getOrCreateGroupInternal(Code.of(namespace), StringUtils.getWithNewlines(namespace), namespace,
	// GroupType.PACKAGE, getRootGroup());
	// }
	// return createLeafInternal(fullyCode,
	// display == null ? StringUtils.getWithNewlines(fullyCode.getShortName(getLeafs())) : display, type,
	// group);
	// }

	private boolean isUsecase() {
		for (ILeaf leaf : getLeafsvalues()) {
			final LeafType type = leaf.getEntityType();
			final USymbol usymbol = leaf.getUSymbol();
			if (type == LeafType.USECASE || usymbol == USymbol.ACTOR) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		final LeafType defaultType = isUsecase() ? LeafType.DESCRIPTION : LeafType.DESCRIPTION;
		final USymbol defaultSymbol = isUsecase() ? USymbol.ACTOR : USymbol.INTERFACE;
		for (ILeaf leaf : getLeafsvalues()) {
			if (leaf.getEntityType() == LeafType.STILL_UNKNOWN) {
				leaf.muteToType(defaultType, defaultSymbol);
			}
		}
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.DESCRIPTION;
	}

	public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparator = namespaceSeparator;
	}
	
	public String getNamespaceSeparator() {
		return namespaceSeparator;
	}


}
