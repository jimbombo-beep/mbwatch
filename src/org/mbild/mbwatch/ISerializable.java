package org.mbild.mbwatch;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Interface providing methods for converting data from and to JSON or XML.
 * 
 * @author Matthias Bild
 * @version 0.1
 *
 */
public interface ISerializable {
	// public methods
	/**
	 * Loads the content of the implementing object from a XML file.
	 * @param filepath the path to the file.
	 * @throws IOException i/o related
	 * @throws ParserConfigurationException XML related
	 * @throws SAXException XML related
	 */
	public void loadXML(String filepath) throws IOException, ParserConfigurationException, SAXException;
	/**
	 * Parses the structure of the given XML element.
	 * @param root any XML element.
	 */
	public void parseXML(Element root);
	/**
	 * Stores the contents of the implementing object into a XML file.
	 * @param filepath the path to the file.
	 * @throws IOException i/o related
	 * @throws ParserConfigurationException XML related
	 * @throws TransformerException XML related
	 */
	public void storeXML(String filepath) throws IOException, ParserConfigurationException, TransformerException;
	/**
	 * Converts the data of the implementing object to a XML element.
	 * @param doc any XML document.
	 * @return An implementation of an XML element.
	 */
	public Element toXML(Document doc);
}
