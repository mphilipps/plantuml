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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class GroupPrinter {

	private final PrintWriter pw;

	private GroupPrinter(PrintWriter pw) {
		this.pw = pw;
	}

	private void printGroup(IGroup group) {
		pw.println("<table border=1 cellpadding=8 cellspacing=0>");
		pw.println("<tr>");
		pw.println("<td bgcolor=#DDDDDD>");
		pw.println(group.getCode());
		pw.println("<tr>");
		pw.println("<td>");
		if (group.getLeafsDirect().size() == 0) {
			pw.println("<i>No direct leaf</i>");
		} else {
			for (ILeaf leaf : group.getLeafsDirect()) {
				pw.println("<ul>");
				printLeaf(leaf);
				pw.println("</ul>");
			}
		}
		pw.println("</td>");
		pw.println("</tr>");
		if (group.getChildren().size() > 0) {
			pw.println("<tr>");
			pw.println("<td>");
			for (IGroup g : group.getChildren()) {
				pw.println("<br>");
				printGroup(g);
				pw.println("<br>");
			}
			pw.println("</td>");
			pw.println("</tr>");
		}
		pw.println("</table>");
	}

	private void printLeaf(ILeaf leaf) {
		pw.println("<li>" + leaf.getCode());
	}

	public static void print(File f, IGroup rootGroup) {
		try {
			final PrintWriter pw = new PrintWriter(f);
			pw.println("<html>");
			new GroupPrinter(pw).printGroup(rootGroup);
			pw.println("</html>");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
