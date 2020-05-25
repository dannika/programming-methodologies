/*
 * File: HangmanLexicon.java
 * -------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * Contains HangmanLexicon class and constructor. Class provides public methods 
 * getWordCount() and getWord(index) to be called in Hangman program. Constructor
 * reads a text file and extracts words line by line, adding them to an array from
 * which one word will be randomly selected as the secretWord when the Hangman
 * program runs.
 */

import acm.util.*;
import java.io.*;
import java.util.*;

public class HangmanLexicon {
	
	/* Declares empty array which will eventually list words. */
	private ArrayList <String> wordList = new ArrayList<String>();
	

	/* HangmanLexicon Constructor */
	/*
	 * Uses a buffered reader to read "HangmanLexicon.txt" file. Reads each line of text
	 * file one by one, appending each line to wordList array. Since each line of the
	 * file is an individual word, this creates an array of potential words to be accessed
	 * in the Hangman program. Closes file.
	 * 
	 * Implements a try/catch structure to handle file input/output exceptions.
	 */
    public HangmanLexicon() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("HangmanLexicon.txt"));
            while(true) {
                String line = reader.readLine();
                if(line == null) break;
                wordList.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
    }

    
    /* Method: getWordCount() */
    /** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return wordList.size();
	}

	
	/* Method: getWord(int index) */
	/** Returns the word at the specified index. */
	public String getWord(int index) {
		return wordList.get(index);
	}
}
