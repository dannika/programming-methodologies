/*
 * File: PythagoreanTheorem.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * -----------------------------
 * Accepts values for a and b as positive doubles and uses the 
 * pythagorean theorem to calculate c.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	
	/* Finds c by inputting doubles a and b to pythagorean theroem. */
	public void run() {
		findC();
	}
	
	
	/*
	 * Accepts doubles a and b from the user and calculates the solution
	 * of c using the pythagorean theorem. Prints value of c.
	 */
	private void findC() {
		println("Enter values to compute Pythagorean Theorem: ");
		double a = readDouble("a: ");
		double b = readDouble("b: ");
		double c = pythagorean(a, b);
		println("c = " + c);
	}
	
	
	/* 
	 * Parameters are two doubles, a and b. Calculates solution of c
	 * by inputting a and b into the pythagorean theorem. Returns c as
	 * a double.
	 */
	private double pythagorean(double a, double b) {
		double c = Math.sqrt(a * a + b * b);
		return (c);
	}
}
