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
package net.sourceforge.plantuml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.sourceforge.plantuml.code.AsciiEncoder;

public class SignatureUtils {

	public static String getSignature(String s) {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			msgDigest.update(s.getBytes("UTF-8"));
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static String getSignatureSha512(String s) {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			msgDigest.update(s.getBytes("UTF-8"));
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static String getSignatureWithoutImgSrc(String s) {
		s = getSignature(purge(s));
		return s;
	}

	public static String purge(String s) {
		final String regex = "(?i)\\<img\\s+src=\"(?:[^\"]+[/\\\\])?([^/\\\\\\d.]+)\\d*(\\.\\w+)\"/\\>";
		s = s.replaceAll(regex, "<img src=\"$1$2\"/>");
		final String regex2 = "(?i)image=\"(?:[^\"]+[/\\\\])?([^/\\\\\\d.]+)\\d*(\\.\\w+)\"";
		s = s.replaceAll(regex2, "image=\"$1$2\"");
		return s;
	}

	public static String getSignature(File f) throws IOException {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			final FileInputStream is = new FileInputStream(f);
			int read = -1;
			while ((read = is.read()) != -1) {
				msgDigest.update((byte) read);
			}
			is.close();
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}
}
