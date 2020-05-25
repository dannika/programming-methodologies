/*
 * File: Hailstone.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * --------------------
 * Demonstrates the Hailstone sequence. The user inputs a positive
 * integer n. While n does not equal 1, if n is even, it will be
 * divided by 2; otherwise, it will be subject to 3n + 1. Each iteration
 * is printed to the console, along with a final count of total 
 * iterations.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	
	/*
	 * Gets a user input in the form of a positive integer. A method is
	 * called that executes the Hailstone sequence.
	 */
	public void run() {
		int n = getPositiveInput();
		computeHailstoneSequence(n);
	}
	
	
	/*
	 * Gets an integer from the user. A while loop is used to ensure
	 * that the user input is positive and will not throw the sequence
	 * into an infinite loop. Returns integer.
	 */
	private int getPositiveInput() {
		int n = readInt("Enter a number: ");
		while (n <= 0) {
			n = readInt("Invalid input. Enter a number: ");
		}
		return (n);
	}
	
	
	/*
	 * Executes the Hailstone sequence on the user input. While the
	 * integer n is not equal to 1, if it is even, it will be divided by
	 * 2; otherwise, it will be subjected to 3n + 1.
	 * 
	 * The variable count is declared to record the number of iterations
	 * that n goes through. The final count will be printed once n = 1.
	 */
	private void computeHailstoneSequence(int n) {
		int count = 0;
		while (n != 1) {
			if (n % 2 == 0) {
				println(n + " is even, so I take half: " + n / 2);
				n /= 2;
				count++;
			} else {
				println(n + " is odd, so I make 3n+1: " + (3 * n + 1));
				n = n * 3 + 1;
				count++;
			}
		}
		println("The process took " + count + " to reach 1");
	}
}

