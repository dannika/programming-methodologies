/*
 * File: StoneMasonKarel.java
 * --------------------------
 * Karel repairs the damage done to the columns of the Quad after the 
 * 1989 earthquake. Each column is of arbitrary height but are four
 * paces apart from one another. The final column is adjacent to a wall.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	/*
	 * Karel walks through the length of the world, using beepers to
	 * repair broken columns of arbitrary heights as they appear every
	 * four paces.
	 */
	public void run() {
		while (frontIsClear()) {
			repairColumn();
			descendColumn();
			walkFourPaces();
		}
		repairFinalColumn();
	}
	
	
	/* Karel ascends the column, adding beepers when necessary. */
	private void repairColumn() {
		turnLeft();
		while (frontIsClear()) {
			if (beepersPresent()) {
				move();
			} else {
				putBeeper();
				move();
			}
		}
		if (noBeepersPresent()) {
			putBeeper();
		}
	}
	
	
	/* Karel returns to the base of the column after repairing it. */
	private void descendColumn() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnLeft();
	}
	
	
	/* Karel moves 4 steps from the base of one column to the next. */
	private void walkFourPaces() {
		move();
		move();
		move();
		move();
	}
	
	
	/* Karel repairs the final column (outside of the while loop). */
	private void repairFinalColumn() {
		repairColumn();
		descendColumn();
	}

}
