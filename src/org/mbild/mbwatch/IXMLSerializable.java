package org.mbild.mbwatch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Interface providing methods for converting data from and to JSON or XML.
 * 
 * @author Matthias Bild
 * @version 0.1
 *
 */
public interface IXMLSerializable {
	// public methods
	/**
	 * Parses the structure of the given XML element.
	 * @param root any XML element.
	 */
	public void parseXML(Element root);
	
	/**
	 * Converts the data of the implementing object to a XML element.
	 * @param doc any XML document.
	 * @return An implementation of an XML element.
	 */
	public Element toXML(Document doc);
}
