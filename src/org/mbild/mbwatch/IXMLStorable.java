package org.mbild.mbwatch;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * Interface for storable objects.
 * 
 * @author Matthias Bild
 *
 */
public interface IXMLStorable {
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
	 * Stores the contents of the implementing object into a XML file.
	 * @param filepath the path to the file.
	 * @throws IOException i/o related
	 * @throws ParserConfigurationException XML related
	 * @throws TransformerException XML related
	 */
	public void storeXML(String filepath) throws IOException, ParserConfigurationException, TransformerException;
}
