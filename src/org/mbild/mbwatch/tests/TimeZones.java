package org.mbild.mbwatch.tests;

import java.util.TimeZone;

/**
 * Time zone test app.
 * 
 * @author Matthias Bild
 *
 */
public class TimeZones {
	// constructors
	/**
	 * Default constructor.
	 */
	public TimeZones() { /* */ }
	
	// public static methods
	/**
	 * Program entry.
	 * @param args program arguments.
	 */
	public static void main(String[] args) {
		String[] ids = TimeZone.getAvailableIDs();
		
		for(String id : ids) {
			System.out.println(id);
		}
	}
}
