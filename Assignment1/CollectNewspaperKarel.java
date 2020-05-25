/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * Karel walks to the door of its house, picks up the newspaper
 * (represented by a beeper), and then returns to its
 * initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {
	
	/*
	 * Karel walks to the door of its house, picks up the newspaper
	 * (beeper), and returns to its original position in the upper left
	 * corner of the house.
	 */
	public void run() {
		walkToDoor();
		pickUpNewspaper();
		returnToCorner();
	}
	
	
	/* Karel walks to the door. */
	private void walkToDoor() {
		move();
		turnRight();
		move();
		turnLeft();
		move();
		move();
	}
	
	
	/* Karel picks up the newspaper. */
	private void pickUpNewspaper() {
		pickBeeper();
	}
	
	
	/*
	 * Karel turns around and returns to its original position and
	 * orientation in the upper left corner of the house.
	 */
	private void returnToCorner() {
		turnAround();
		move();
		move();
		move();
		turnRight();
		move();
		turnRight();
	}
	
}
