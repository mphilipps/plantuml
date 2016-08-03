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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;

public class SkinParameter {

	public static final SkinParameter DATABASE = new SkinParameter("DATABASE", ColorParam.databaseBackground,
			ColorParam.databaseBorder, FontParam.DATABASE, FontParam.DATABASE_STEREOTYPE);

	public static final SkinParameter ARTIFACT = new SkinParameter("ARTIFACT", ColorParam.artifactBackground,
			ColorParam.artifactBorder, FontParam.ARTIFACT, FontParam.ARTIFACT_STEREOTYPE);

	public static final SkinParameter COMPONENT1 = new SkinParameter("COMPONENT1", ColorParam.componentBackground,
			ColorParam.componentBorder, FontParam.COMPONENT, FontParam.COMPONENT_STEREOTYPE);

	public static final SkinParameter NODE = new SkinParameter("NODE", ColorParam.nodeBackground,
			ColorParam.nodeBorder, FontParam.NODE, FontParam.NODE_STEREOTYPE);

	public static final SkinParameter STORAGE = new SkinParameter("STORAGE", ColorParam.storageBackground,
			ColorParam.storageBorder, FontParam.STORAGE, FontParam.STORAGE_STEREOTYPE);

	public static final SkinParameter QUEUE = new SkinParameter("QUEUE", ColorParam.queueBackground,
			ColorParam.queueBorder, FontParam.QUEUE, FontParam.QUEUE_STEREOTYPE);

	public static final SkinParameter CLOUD = new SkinParameter("CLOUD", ColorParam.cloudBackground,
			ColorParam.cloudBorder, FontParam.CLOUD, FontParam.CLOUD_STEREOTYPE);

	public static final SkinParameter FRAME = new SkinParameter("FRAME", ColorParam.frameBackground,
			ColorParam.frameBorder, FontParam.FRAME, FontParam.FRAME_STEREOTYPE);

	public static final SkinParameter COMPONENT2 = new SkinParameter("COMPONENT2", ColorParam.componentBackground,
			ColorParam.componentBorder, FontParam.COMPONENT, FontParam.COMPONENT_STEREOTYPE);

	public static final SkinParameter AGENT = new SkinParameter("AGENT", ColorParam.agentBackground,
			ColorParam.agentBorder, FontParam.AGENT, FontParam.AGENT_STEREOTYPE);

	public static final SkinParameter FOLDER = new SkinParameter("FOLDER", ColorParam.folderBackground,
			ColorParam.folderBorder, FontParam.FOLDER, FontParam.FOLDER_STEREOTYPE);

	public static final SkinParameter PACKAGE = new SkinParameter("PACKAGE", ColorParam.packageBackground,
			ColorParam.packageBorder, FontParam.FOLDER, FontParam.FOLDER_STEREOTYPE);

	public static final SkinParameter CARD = new SkinParameter("CARD", ColorParam.rectangleBackground,
			ColorParam.rectangleBorder, FontParam.RECTANGLE, FontParam.RECTANGLE_STEREOTYPE);

	public static final SkinParameter ACTOR = new SkinParameter("ACTOR", ColorParam.actorBackground,
			ColorParam.actorBorder, FontParam.ACTOR, FontParam.ACTOR_STEREOTYPE);

	public static final SkinParameter BOUNDARY = new SkinParameter("BOUNDARY", ColorParam.boundaryBackground,
			ColorParam.boundaryBorder, FontParam.BOUNDARY, FontParam.BOUNDARY_STEREOTYPE);

	public static final SkinParameter CONTROL = new SkinParameter("CONTROL", ColorParam.controlBackground,
			ColorParam.controlBorder, FontParam.CONTROL, FontParam.CONTROL_STEREOTYPE);

	public static final SkinParameter ENTITY_DOMAIN = new SkinParameter("ENTITY_DOMAIN", ColorParam.entityBackground,
			ColorParam.entityBorder, FontParam.ENTITY, FontParam.ENTITY_STEREOTYPE);

	public static final SkinParameter INTERFACE = new SkinParameter("INTERFACE", ColorParam.interfaceBackground,
			ColorParam.interfaceBorder, FontParam.INTERFACE, FontParam.INTERFACE_STEREOTYPE);

	private final ColorParam colorParamBorder;
	private final ColorParam colorParamBack;
	private final FontParam fontParam;
	private final FontParam fontParamStereotype;
	private final String name;

	private SkinParameter(String name, ColorParam colorParamBack, ColorParam colorParamBorder, FontParam fontParam,
			FontParam fontParamStereotype) {
		this.name = name;
		this.colorParamBack = colorParamBack;
		this.colorParamBorder = colorParamBorder;
		this.fontParam = fontParam;
		this.fontParamStereotype = fontParamStereotype;
	}
	
	public String getUpperCaseName() {
		return name;
	}

	public ColorParam getColorParamBorder() {
		return colorParamBorder;
	}

	public ColorParam getColorParamBack() {
		return colorParamBack;
	}

	public FontParam getFontParam() {
		return fontParam;
	}

	public FontParam getFontParamStereotype() {
		return fontParamStereotype;
	}

}
