/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import java.util.Iterator;

/**
 * <CODE>PdfXObjectDictionary</CODE> is a <CODE>PdfResource</CODE>, containing a dictionary of XObjects.
 *
 * @see		PdfResource
 * @see		PdfResources
 */

class PdfXObjectDictionary extends PdfDictionary implements PdfResource {

// constructors

/**
* Constructs a new <CODE>PdfFontDictionary</CODE>.
*/

	PdfXObjectDictionary() { 
		super();
	}

// methods

/**
* Returns the name of a resource.
*
* @return		a <CODE>PdfName</CODE>.
*/

	public PdfName key() {
		return PdfName.XOBJECT;
	}

/**
* Returns the object that represents the resource.
*
* @return		a <CODE>PdfObject</CODE>
*/

	public PdfObject value() {
		return this;
	}

/**
* Checks if the <CODE>XObjectDictionary</CODE> allready contains a
* <CODE>PdfImage</CODE> with this name.
*
* @return		<CODE>true</CODE> if a font with this name allready exists,
*				<CODE>false</CODE> otherwise
*/

	boolean contains(PdfImage image) {
		return hashMap.containsKey(image.name());
	}

/**
* Checks if the <CODE>PdfXObjectDictionary</CODE> contains at least
* one object.
*
* @return		<CODE>true</CODE> if an object was found
*				<CODE>false</CODE> otherwise
*/

	boolean containsXObject() {
		return hashMap.size() > 0;
	}
}