/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * --> Copyright 2001 by Bruno Lowagie <--
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://www.lowagie.com/iText/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */

package com.lowagie.examples.objects.fonts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Special rendering of Chunks.
 * 
 * @author blowagie
 */

public class UsingFontFactory {

	/**
	 * Special rendering of Chunks.
	 * 
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {

		System.out.println("Fonts in the FontFactory");

		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer that listens to the document
			PdfWriter.getInstance(document,
					new FileOutputStream("FontFactory.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			String name;
			Paragraph p = new Paragraph("Font Families", FontFactory.getFont(FontFactory.HELVETICA, 16f));
			document.add(p);
			FontFactory.registerDirectories();
			TreeSet families = new TreeSet(FontFactory.getRegisteredFamilies());
			for (Iterator i = families.iterator(); i.hasNext(); ) {
				name = (String) i.next();
				p = new Paragraph(name);
				document.add(p);
			}
			document.newPage();
			String quick = "quick brown fox jumps over the lazy dog";
			p = new Paragraph("Fonts", FontFactory.getFont(FontFactory.HELVETICA, 16f));
			TreeSet fonts = new TreeSet(FontFactory.getRegisteredFonts());
			for (Iterator i = families.iterator(); i.hasNext(); ) {
				name = (String) i.next();
				p = new Paragraph(name);
				document.add(p);
				p = new Paragraph(quick, FontFactory.getFont(name, BaseFont.WINANSI, BaseFont.EMBEDDED));
				document.add(p);
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}