/*
 * File: NameSurferEntry.java
 * --------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This class represents a single entry in the database.  Each NameSurferEntry contains a 
 * name and a list giving the popularity of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

	/* Constructor: NameSurferEntry(line) */
	/**
	 * Creates a new NameSurferEntry from a data line as it appears in the data file. Each 
	 * line begins with the name, which is followed by integers giving the rank of that name
	 * for each decade.
	 * 
	 * @param String line, a line of text (name and rankings)
	 */
	public NameSurferEntry(String line) {
		int firstRankIndex = line.indexOf(' ') + 1;
		name = line.substring(0, firstRankIndex - 1);
		String rankings = line.substring(firstRankIndex);
		StringTokenizer tokenizer = new StringTokenizer(rankings);
		for (int i = 0; tokenizer.hasMoreTokens(); i++) {
			ranks[i] = Integer.parseInt(tokenizer.nextToken());
		}
	}

	
	/* Method: getName() */
	/**
	 * Returns the name associated with this entry.
	 * 
	 * @return String name, the name associated with this entry
	 */
	public String getName() {
		return name;
	}

	
	/* Method: getRank(decade) */
	/**
	 * Returns the rank associated with an entry for a particular decade. The decade value 
	 * is an integer indicating how many decades have passed since the first year in the 
	 * database, which is given by the constant START_DECADE. If a name does not appear in a
	 * decade, the rank value is 0.
	 * 
	 * @param int decade, the decade in which the program requests a certain ranking
	 * @return int rank, a name's rank at that particular decade
	 */
	public int getRank(int decade) {
		return ranks[decade];
	}

	
	/* Method: toString() */
	/**
	 * Returns a string that makes it easy to see the value of a NameSurferEntry. This string
	 * consists of the name plus a bracketed list of rankings by decade.
	 * 
	 * @return String result, a string containing a name and bracketed list of rankings
	 */
	public String toString() {
		String result = "";
		result = result + name + " [";
		for (int i = 0; i < NDECADES; i++) {
			result += ranks[i];
			if (i < NDECADES - 1) {
				result += " ";
			}
		}
		result += "]";
		return result;
	}
	
	
	/* Private instance variables */
	private String name = "";
	private int[] ranks = new int[NDECADES];
	
}
