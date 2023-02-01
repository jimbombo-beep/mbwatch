package org.mbild.mbwatch;

import java.io.IOException;

/**
 * Interface forcing instances to implement methods for parsing and converting to 
 * comma-separated values (CSV).
 * 
 * @author Matthias Bild
 *
 */
public interface ICSVSerializable {
	// public methods
	/**
	 * Reads the values from the given CSV-formatted file.
	 * @param filepath the path to the file to read.
	 * @throws IOException i/o-related.
	 */
	public void parseCSV(String filepath) throws IOException;
	/**
	 * Converts the contents of the implementing object to a CSV-formatted file.
	 * @param filepath the path to the file to write.
	 * @throws IOException i/o-related.
	 */
	public void toCSV(String filepath) throws IOException;
}
