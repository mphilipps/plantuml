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
package net.sourceforge.plantuml.donors;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	public static final String DONORS = "UDfTKyjosp0ClEChUDQa7w7OhLlPJZ9Msixa1bk8H9icL99oAtrRR_snGcvbKkP96Bns5wlSPFyVENYXG4xbDh3LU81NokBZCsmuXiv3FhNY7bOkfgY7alReWqQhdhYnaDkAdLenXO76p_TtgPZAqS86aqKsm59FWJQGnz5yWRAB47fOwMp-B4CneUmyb87QXgnQCBV2Rpdj8G_hyrqhErYmqLRjkFC4HNUNpuuiHAQWrR1DKECv3MLLRkWNsahaN3HQfjReUc6wkz6sDJobwCC0pqPVf66f3oT1Vije2n_zRRHDG6HqWULmri7rQeCDLmZ5AFfRWp8-gSl8n7F9E_KYayKWTO9FS1N_mDDfnifMi_3QgMMGaRPd5_G0dsEwOjZgGqD6FQ8sVSh032aOx--X5OJsGu677nrvjUps3kMq9jB_niT0XqQj7NgCa6eInoO7_3aQJKhKcYywzStLELyZVusmIxmyhfZnF4ftcxYCJwDGZ-pGecaVr1i5vDR3JlYYgzHQZvU6JuRTAF_spszZhXw8_Z29LyLTcvkx74xYZoPevenyOfXHg1USlBFJC_YvqQf4hEI_SZMfxsHhSWzgcxz-ANFu2z-Q0eS0";

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final GraphicStrings result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, result.getBackcolor(),
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, os);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add("<b>Special thanks to our sponsors and donors !");
		lines.add(" ");
		int i = 0;
		final List<String> donors = getDonors();
		final int maxLine = (donors.size() + 1) / 2;
		for (String d : donors) {
			lines.add(d);
			i++;
			if (i == maxLine) {
				lines.add(" ");
				lines.add(" ");
				i = 0;
			}
		}
		lines.add(" ");
		final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
		final GraphicStrings graphicStrings = new GraphicStrings(lines, font, HtmlColorUtils.BLACK,
				HtmlColorUtils.WHITE, UAntiAliasing.ANTI_ALIASING_ON, PSystemVersion.getPlantumlImage(),
				GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT);
		graphicStrings.setMaxLine(maxLine + 2);
		return graphicStrings;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<String>();
		final Transcoder t = new TranscoderImpl();
		final String s = t.decode(DONORS).replace('*', '.');
		final StringTokenizer st = new StringTokenizer(s, "\n");
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Donors)", getClass());
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
