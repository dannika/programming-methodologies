/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * Karel creates a checkerboard pattern of beepers.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	/* 
	 * Karel creates a checkerboard pattern of beepers. Conditional
	 * statement checks for special case of single-column world. 
	 */
	public void run() {
		putBeeper();
		if (frontIsBlocked()) {
			turnLeft();
			fillCheckerboardRow();
		} else {
			while (frontIsClear()) {
				headEast();
				headWest();
			}	
		}
	}
	
	
	/*
	 * Karel heads towards the East, dropping a beeper on every other
	 * corner of the street.
	 */
	private void headEast() {
		if (facingEast()) {
			fillCheckerboardRow();
			checkEastWall();
		}
	}
	
	
	/*
	 * Karel heads towards the West, dropping a beeper on every other
	 * corner of the Street.
	 */
	private void headWest() {
		if (facingWest()) {
			fillCheckerboardRow();
			checkWestWall();
		}
	}
	
	
	/*
	 * Karel checks the eastern wall to determine if it should go onto
	 * the next row and whether it should drop a beeper on the first
	 * corner of the next row.
	 */
	private void checkEastWall() {
		if (noBeepersPresent()) {
			turnLeft();
			/* Executes if Karel has not hit ceiling. */
			if (frontIsClear()) {
				move();
				turnLeft();
				putBeeper();
			}
		} else {
			turnLeft();
			/* Executes if Karel has not hit ceiling. */
			if (frontIsClear()) {
				move();
				turnLeft();
			}
		}
	}
	
	
	/*
	 * Karel checks the western wall to determine if it should go onto
	 * the next row and whether it should drop a beeper on the first
	 * corner of the next row.
	 */
	private void checkWestWall() {
		if (noBeepersPresent()) {
			turnRight();
			/* Executes if Karel has not hit ceiling. */
			if (frontIsClear()) {
				move();
				turnRight();
				putBeeper();
			}
		} else {
			turnRight();
			/* Executes is Karel has not hit ceiling. */
			if (frontIsClear()) {
				move();
				turnRight();
			}
		}
	}
	
	
	/* Karel completes a row of the checkerboard. */
	private void fillCheckerboardRow() {
		while (frontIsClear()) {
			alternateCorners();
		}
	}
	
	
	/* Allows Karel to place beepers at alternating corners. */
	private void alternateCorners() {
		if (noBeepersPresent()) {
			move();
			putBeeper();
		} else {
			move();
		}
	}
}