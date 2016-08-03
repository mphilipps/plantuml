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
package net.sourceforge.plantuml.classdiagram.command;

import java.util.EnumSet;
import java.util.Set;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.EntityGender;
import net.sourceforge.plantuml.cucadiagram.EntityGenderUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;

public class CommandHideShow extends SingleLineCommand2<UmlDiagram> {

	private static final EnumSet<EntityPortion> PORTION_METHOD = EnumSet.<EntityPortion> of(EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_MEMBER = EnumSet.<EntityPortion> of(EntityPortion.FIELD,
			EntityPortion.METHOD);
	private static final EnumSet<EntityPortion> PORTION_FIELD = EnumSet.<EntityPortion> of(EntityPortion.FIELD);

	public CommandHideShow() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("COMMAND", "(hide|show)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("GENDER",
						"(?:(class|interface|enum|annotation|abstract|[\\p{L}0-9_.]+|[%g][^%g]+[%g]|\\<\\<.*\\>\\>)[%s]+)*?"), //
				new RegexLeaf("EMPTY", "(?:(empty)[%s]+)?"), //
				new RegexLeaf("PORTION", "(members?|attributes?|fields?|methods?|circle\\w*|stereotypes?)"), //
				new RegexLeaf("$"));
	}

	private final EntityGender emptyByGender(Set<EntityPortion> portion) {
		if (portion == PORTION_METHOD) {
			return EntityGenderUtils.emptyMethods();
		}
		if (portion == PORTION_FIELD) {
			return EntityGenderUtils.emptyFields();
		}
		if (portion == PORTION_MEMBER) {
			return EntityGenderUtils.emptyMembers();
		}
		return EntityGenderUtils.all();
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram classDiagram, RegexResult arg) {
		if (classDiagram instanceof ClassDiagram) {
			return executeClassDiagram((ClassDiagram) classDiagram, arg);
		}
		if (classDiagram instanceof DescriptionDiagram) {
			return executeDescriptionDiagram((DescriptionDiagram) classDiagram, arg);
		}
		// Just ignored
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeDescriptionDiagram(DescriptionDiagram diagram, RegexResult arg) {
		final Set<EntityPortion> portion = getEntityPortion(arg.get("PORTION", 0));
		final EntityGender gender;
		final String arg1 = arg.get("GENDER", 0);
		if (arg1 == null) {
			gender = EntityGenderUtils.all();
		} else if (arg1.equalsIgnoreCase("class")) {
			gender = EntityGenderUtils.byEntityType(LeafType.CLASS);
		} else if (arg1.equalsIgnoreCase("interface")) {
			gender = EntityGenderUtils.byEntityType(LeafType.INTERFACE);
		} else if (arg1.equalsIgnoreCase("enum")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ENUM);
		} else if (arg1.equalsIgnoreCase("abstract")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ABSTRACT_CLASS);
		} else if (arg1.equalsIgnoreCase("annotation")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ANNOTATION);
		} else if (arg1.startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg1);
		} else {
			final IEntity entity = diagram.getOrCreateLeaf(Code.of(arg1), null, null);
			gender = EntityGenderUtils.byEntityAlone(entity);
		}

		diagram.hideOrShow(gender, portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeClassDiagram(ClassDiagram classDiagram, RegexResult arg) {

		final Set<EntityPortion> portion = getEntityPortion(arg.get("PORTION", 0));

		EntityGender gender = null;
		final String arg1 = arg.get("GENDER", 0);
		if (arg1 == null) {
			gender = EntityGenderUtils.all();
		} else if (arg1.equalsIgnoreCase("class")) {
			gender = EntityGenderUtils.byEntityType(LeafType.CLASS);
		} else if (arg1.equalsIgnoreCase("interface")) {
			gender = EntityGenderUtils.byEntityType(LeafType.INTERFACE);
		} else if (arg1.equalsIgnoreCase("enum")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ENUM);
		} else if (arg1.equalsIgnoreCase("abstract")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ABSTRACT_CLASS);
		} else if (arg1.equalsIgnoreCase("annotation")) {
			gender = EntityGenderUtils.byEntityType(LeafType.ANNOTATION);
		} else if (arg1.startsWith("<<")) {
			gender = EntityGenderUtils.byStereotype(arg1);
		} else {
			final IEntity entity = classDiagram.getOrCreateLeaf(Code.of(arg1), null, null);
			gender = EntityGenderUtils.byEntityAlone(entity);
		}
		if (gender != null) {
			final boolean empty = arg.get("EMPTY", 0) != null;
			if (empty == true) {
				gender = EntityGenderUtils.and(gender, emptyByGender(portion));
			}
			if (EntityUtils.groupRoot(classDiagram.getCurrentGroup()) == false) {
				gender = EntityGenderUtils.and(gender, EntityGenderUtils.byPackage(classDiagram.getCurrentGroup()));
			}
			classDiagram.hideOrShow(gender, portion, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		}
		return CommandExecutionResult.ok();
	}

	private Set<EntityPortion> getEntityPortion(String s) {
		final String sub = StringUtils.goLowerCase(s.substring(0, 3));
		if (sub.equals("met")) {
			return PORTION_METHOD;
		}
		if (sub.equals("mem")) {
			return PORTION_MEMBER;
		}
		if (sub.equals("att") || sub.equals("fie")) {
			return PORTION_FIELD;
		}
		if (sub.equals("cir")) {
			return EnumSet.<EntityPortion> of(EntityPortion.CIRCLED_CHARACTER);
		}
		if (sub.equals("ste")) {
			return EnumSet.<EntityPortion> of(EntityPortion.STEREOTYPE);
		}
		throw new IllegalArgumentException();
	}

}
