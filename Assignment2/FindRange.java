/*
 * File: FindRange.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * --------------------
 * Reads a list of integers, one per line, until a sentinel value of 0
 * is entered. Once the sentinel is inputed, the program displays the
 * smallest and largest of the integers. If no values are given, the
 * program displays so. If only one integer is given, it is displayed
 * as both the smallest and largest.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	/*
	 * Constant that, when inputed to getIntsUntilSentinel(), indicates
	 * the end of user inputs and causes a break from the while loop.
	 */
	private static final int SENTINEL = 0;
	
	/* Instance variable that stores value of largest integer.*/
	private int max;
	
	/* Instance variable that stores value of smallest integer.*/
	private int min;
	
	
	/*
	 * Finds the smallest and largest of a list of integers as they are
	 * inputed one per line. Once the SENTINAL is entered, the program
	 * stops prompting the user and the smallest and largest of the
	 * inputed integers is printed to the console.
	 */
	public void run() {
		findMinAndMax();
	}	
	
	
	/*
	 * Prints description of the program. Calls getIntsUntilSentinel()
	 * to prompt user for integers and find the smallest and largest of
	 * the list. Calls printMaxAndMin() to print instance variables
	 * min and max.
	 */
	private void findMinAndMax() {
		println("This program finds the largest and smallest numbers.");
		getIntsUntilSentinel();
		printMaxAndMin();
	}
	
	
	/*
	 * Prompts user for list of integers, one per line, until a SENTINEL
	 * is entered. If the integer is greater than or less than the
	 * value of max or min, respectively, then the integer is stored
	 * to the corresponding variable. If no integers are entered, the 
	 * programs prints so. 
	 */
	private void getIntsUntilSentinel() {
		int n = readInt("? ");
		
		if (n == SENTINEL) {
			println("No values were entered.");
		} else {
			max = n;
			min = n;
			
			while (true) {
				n = readInt("? ");
				if (n == SENTINEL) break;
				
				if (n > max) {
					max = n;
				} else if (n < min) {
					min = n;
				}
			}
		}
	}
	
	
	/*
	 * Prints the values of instance variables max and min if the first
	 * user input is not the SENTINEL.
	 */
	private void printMaxAndMin() {
		if (max != SENTINEL) {
			if (min != SENTINEL) {
				println("smallest: " + min);
				println("largest: " + max);
			}
		}
	}
}

