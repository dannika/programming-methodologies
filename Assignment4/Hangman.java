/*
 * File: Hangman.java
 * ------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * Plays the game of Hangman. The computer picks a random word and prints a row of dashes,
 * one for each letter in the secret word, and prompts the user to guess a letter. If the
 * user guesses a letter that is in the word, the word is redisplayed with all instances
 * of that letter shown in the correct positions, along with any letters correctly guessed
 * on previous turns. If the letter does not appear in the word, the user is charged with
 * an incorrect guess and a body part is drawn on a man hanging from a scaffold. The user 
 * keeps guessing letters until either (1) the user has guessed all the letters in the word
 * or (2) the user has made eight incorrect guesses, or is "completely hung."
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
	
	/* Holds the word to be randomly generated and guessed by the user. */
	private String secretWord;
	
	/* Holds the user's progress (dashes or letters) in guessing secret word. */
	private String guessWord;
	
	/* Stores incorrectly guessed letters. */
	private String incorrectLetters = "";
	
	/* Number of guesses that the user has remaining. */
	private int userGuesses = 8;
	
	/* Holds a randomly generated instance. */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Creates list of potential words from HangmanLexicon. */
	private HangmanLexicon wordList = new HangmanLexicon();
	
	/* Establishes the graphical canvas. */
	private HangmanCanvas canvas;
	
	
	/* Method: init() */
	/* Initializes the graphical canvas and adds it to the window. */
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}
	
	
	/* Method: run() */
	/* 
	 * Runs the Hangman program. Sets up console and graphics display (scaffold and hidden
	 * word). Plays the game.
	 */
	public void run() {
		setUpHangman();
		canvas.reset();
		canvas.displayWord(guessWord);
		playHangman();
	}
	
	
	/* Method: setUpHangman() */
	/*
	 * Sets up the gameplay. Prints a welcome message to the user. Assigns a random word to
	 * the variable secretWord. Sets guessWord to the result of calling getDashedWord(), 
	 * which provides a string of dashes that the user will eventually uncover as letters.
	 * Prints the number of guesses that the user has remaining.
	 */
	private void setUpHangman() {
		println("Welcome to Hangman!");
		secretWord = getRandomWord();
		guessWord = getDashedWord();
		printGuessCount();
	}
	
	
	/* Method: playHangman() */
	/*
	 * Plays the game. While the game has not ended, the user is prompted for a guess,
	 * which in turn is checked for validity (is a single letter). The user is continuously
	 * prompted until a valid input is entered. The user's guess is made upper case, and
	 * checkLetter(guess) is called to check if guess is in the secretWord. If the user at
	 * any time meets a game-ending condition (either guesses word or runs out of guesses),
	 * the program breaks from recursion and the game ends. Otherwise, the user's stats
	 * are updated in the window.
	 */
	private void playHangman() {
		while (true) {
			String guess = readLine("Your guess: ");
			while (guess.length() != 1 || Character.isLetter(guess.charAt(0)) == false) {
				guess = readLine("Invalid input. Your guess: ");
			}
			guess = guess.toUpperCase();
			checkLetter(guess);
			if (checkGameOver() == false) {
				printWordProgress();
				printGuessCount();
			} else {
				break;
			}
		}
	}
	
	
	/* Method: getRandomWord() */
	/*
	 * Returns random String pickedWord. Randomly generates an integer between 0 and 
	 * the size of wordList - 1. Uses random integer as index parameter in wordList.getWord()
	 * to randomly pick a word from the lexicon. Returns that selected word, which becomes
	 * the secretWord that the user is attempting to guess.
	 */
	private String getRandomWord() {
		int wordIndex = rgen.nextInt(0, wordList.getWordCount() - 1);
		String pickedWord = wordList.getWord(wordIndex);
		return pickedWord;
	}
	
	
	/* Method: getDashedWord() */
	/*
	 * Returns String result, which is a string of dashes in which the number of dashes
	 * corresponds with the length of secretWord. Prints dashed result to the console.
	 * Variable guessWord takes the value of getDashedWord(), allowing for updates to
	 * guessWord as the user correctly guesses letters.
	 */
	private String getDashedWord() {
		String result = "";
		for (int i = 0; i < secretWord.length(); i++) {
			result += "-";
		}
		println("The word now looks like this: " + result);
		return result;
	}
	
	
	/* Method: printGuessCount() */
	/* Prints the number of guesses remaining to the console. */
	private void printGuessCount() {
		println("You have " + userGuesses + " guesses left.");
	}
	
	
	/* Method: checkLetter() */
	/*
	 * Determines if the user's guess is correct or incorrect. Takes a String guess as a
	 * parameter. 
	 * 
	 * If the guess is not in the secretWord:
	 * The user is told so. If the user had not already guessed that letter, it is added to
	 * incorrectLetters and the wrongGuessDisplay on the canvas, and the user loses a guess.
	 * If the user had already guessed that letter, the user is NOT penalized again; rather,
	 * the gameplay continues.
	 * 
	 * If the guess is in the secretWord:
	 * The user is told so. The program iterates through secretWord, and whenever the
	 * guessed letter is found at a respective index, the letter replaces the dash of the
	 * corresponding index in guessWord.
	 * 
	 * The hidden word label is updated on the canvas.
	 */
	private void checkLetter(String guess) {
		if (secretWord.indexOf(guess) == -1) {
			println("There are no " + guess + "'s in the word.");
			if (alreadyGuessed(guess) == false) {
				incorrectLetters += guess;
				canvas.noteIncorrectGuess(guess.charAt(0));
				userGuesses--;
			}
		} else {
			println("That guess is correct.");
			for (int i = 0; i < secretWord.length(); i++) {
				if (secretWord.charAt(i) == guess.charAt(0) && i == 0) {
					guessWord = guess + guessWord.substring(i + 1);
				} else if (secretWord.charAt(i) == guess.charAt(0) && i != 0) {
					guessWord = guessWord.substring(0,  i) + guess + guessWord.substring(i + 1);
				}
			}
			canvas.displayWord(guessWord);
		}
	}
	
	
	/* Method: checkGameOver() */
	/*
	 * Determines whether the user has met any game-ending conditions. 
	 * 
	 * Returns true:
	 * a) If the user runs out of guesses, they are completely hung, secretWord is finally 
	 *    printed, and user loses the game. 
	 * b) If the user guesses the entire word within the given number of guesses. A winning
	 *    message is printed, along with the secretWord, and the user wins the game.
	 *    
	 * Otherwise, returns false, and the gameplay continues.
	 */
	private boolean checkGameOver() {
		if (userGuesses == 0) {
			println("You're completely hung.");
			println("The word was " + secretWord);
			println("You lose.");
			return true;
		} else if (guessWord.equals(secretWord)) {
			println("You guessed the word: " + secretWord);
			println("You win.");
			return true;
		}
		return false;
	}
	
	
	/* Method: printWordProgress() */
	/* Prints the user's guessWord as it (hopefully) progresses from dashes to letters. */
	private void printWordProgress() {
		println("The word now looks like this: " + guessWord);
	}
	
	
	/* Method: alreadyGuessed(String guess) */
	/*
	 * Determines if user has already guessed a letter. Accepts String guess as a parameter.
	 * If the user's guess is already in the string incorrectGuesses, implying that the
	 * user has already guessed that letter and that the letter had already been deemed
	 * incorrect, then the method returns true. Otherwise, the method return false.
	 */
	private boolean alreadyGuessed(String guess) {
		if (incorrectLetters.length() > 0) {
			for (int i = 0; i < incorrectLetters.length(); i++) {
				if (incorrectLetters.charAt(i) == guess.charAt(0)) {
					return true;
				}
			}
		}
		return false;
	}	
}
