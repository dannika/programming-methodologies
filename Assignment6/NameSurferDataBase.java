/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This class keeps track of the complete database of names. The constructor reads in the 
 * database from a file, and the only public method makes it possible to look up a name and
 * get back the corresponding NameSurferEntry. Names are matched independent of case, so that
 * "Eric" and "ERIC" are the same names.
 */

import acm.util.*;
import java.io.*;
import java.util.*;

public class NameSurferDataBase implements NameSurferConstants {
	
	/* Constructor: NameSurferDataBase(filename) */
	/**
	 * Creates a new NameSurferDataBase and initializes it using the data in the specified
	 * file. Using a buffered reader, the constructor reads each line of the file, adding
	 * each name and corresponding rankings to a HashMap. The constructor throws an error 
	 * exception if the requested file does not exist or if an error occurs as the file is 
	 * being read.
	 * 
	 * @param String filename, a text file to be read by the BufferedReader
	 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename));
			while (true) {
				String line = rd.readLine();
				if (line == null) break;
				NameSurferEntry nameEntry = new NameSurferEntry(line);
				namesHash.put(nameEntry.getName(), nameEntry);
			}
			rd.close();
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	
	/* Method: findEntry(name) */
	/**
	 * Returns the NameSurferEntry associated with this name, if one exists. The name is
	 * returned in proper name formatting (upper-case first letter and lower-case remaining
	 * letters) If the name does not appear in the database, or if the user does not input 
	 * anything at all, this method returns null.
	 * 
	 * @param String name, the text entered by the user into JTextField nameField
	 * @return NameSurferEntry, entry associated with String name (or null if nonexistent)
	 */
	public NameSurferEntry findEntry(String name) {
		if (name.equals("")) {
			return null;
		} else {
			char firstLetter = name.charAt(0);
			if (Character.isLowerCase(firstLetter)) {
				firstLetter = Character.toUpperCase(firstLetter);
			}
			name = firstLetter + (name.substring(1)).toLowerCase();
			return namesHash.get(name);
		}
	}
	
	
	/* Private instance variables */
	private HashMap<String, NameSurferEntry> namesHash = new HashMap<String, NameSurferEntry>();
	
}

