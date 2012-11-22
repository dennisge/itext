/*
 * $Id: FontsResourceAnchor.java 4784 2011-03-15 08:33:00Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Ram Narayan, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.indic;

import java.util.Map;

/**
 * Superclass for processors that can convert a String of bytes in
 * an Indic language to a String in the same language of which the
 * bytes are reordered for rendering using a font that contains the
 * necessary glyphs.
 */
public abstract class IndicLigaturizer {

	// Hashtable Indexes
	public static final int MATRA_AA = 0;
	public static final int MATRA_I = 1;
	public static final int MATRA_AI = 2;
	public static final int MATRA_HLR = 3;
	public static final int MATRA_HLRR = 4;
	public static final int LETTER_A = 5;
	public static final int LETTER_AU = 6;
	public static final int LETTER_KA = 7;
	public static final int LETTER_HA = 8;
	public static final int HALANTA = 9;

	/**
	 * The table mapping specific character indexes to the characters in a
	 * specific language.
	 */
	protected Map<Integer, Character> langTable;

	/**
	 * Reorders the bytes in a String making Indic ligatures
	 * @param s	the original String
	 * @return	the ligaturized String
	 */
	public String process(String s) {
		if (s == null || s.length() == 0)
			return "";
		StringBuilder res = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char letter = s.charAt(i);

			if (IsVyanjana(letter) || IsSwaraLetter(letter)) {
				res.append(letter);
			} else if (IsSwaraMatra(letter)) {
				int prevCharIndex = res.length() - 1;
				boolean svaraAppended = false;
				if (prevCharIndex >= 0) {
					 // a Halanta followed by swara matra, causes it to lose its identity
					if (res.charAt(prevCharIndex) == langTable.get(HALANTA)) {
						res.deleteCharAt(prevCharIndex);
						res.append(letter);
						svaraAppended = true;
					}
					int prevPrevCharIndex = res.length() - 2;

					if (letter == langTable.get(MATRA_I)
							&& prevPrevCharIndex >= 0) {
						swap(res, prevPrevCharIndex, res.length() - 1);
					} else {
						if (!svaraAppended)
							res.append(letter);
					}
				} else {
					res.append(letter);
				}
			} else {
				res.append(letter);
			}
		}

		return res.toString();
	}
	
	/**
	 * Checks if a character is in a specific range.
	 * @param ch	the character that needs to be checked
	 * @return	true if the characters falls within the range
	 */
	protected boolean IsSwaraLetter(char ch) {
		return (ch >= langTable.get(LETTER_A) && ch <= langTable.get(LETTER_AU));
	}

	/**
	 * Checks if a character is in a specific range.
	 * @param ch	the character that needs to be checked
	 * @return	true if the characters falls within the range
	 */
	protected boolean IsSwaraMatra(char ch) {
		return ((ch >= langTable.get(MATRA_AA) && ch <= langTable.get(MATRA_AI))
				|| ch == langTable.get(MATRA_HLR) || ch == langTable
					.get(MATRA_HLRR));
	}

	/**
	 * Checks if a character is in a specific range.
	 * @param ch	the character that needs to be checked
	 * @return	true if the characters falls within the range
	 */
	protected boolean IsVyanjana(char ch) {
		return (ch >= langTable.get(LETTER_KA) && ch <= langTable.get(LETTER_HA));
	}

	/**
	 * Swaps two characters in a StringBuilder object
	 * @param s	the StringBuilder
	 * @param i	the index of one character
	 * @param j the index of the other character
	 */
	private static void swap(StringBuilder s, int i, int j) {
		char temp = s.charAt(i);
		s.setCharAt(i, s.charAt(j));
		s.setCharAt(j, temp);
	}
}
