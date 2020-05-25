/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * Karel finds the midpoint of 1st Street, dropping a beeper at the
 * central corner if the total number of corners on 1st Street is odd,
 * and at the first of the two central corners if the total number of
 * corners is even.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	/*
	 * Karel finds the midpoint of 1st Street. Karel will drop a beeper
	 * at the central corner of the street if the total number of 
	 * corners is odd, and at the first of the two central corners if
	 * the total number of corners is even.
	 */
	public void run() {
		dropAllBeepers();
		pickEdgeBeepers();
		pickAllOtherBeepers();
		dropMidpointBeeper();
	}
	
	
	/* Karel drops a beeper on every corner on 1st Street. */
	private void dropAllBeepers() {
		while (frontIsClear()) {
			putBeeper();
			move();
		}
		putBeeper();
		turnAround();
	}
	
	
	/* Karel picks up the two beepers nearest each wall. */
	private void pickEdgeBeepers() {
		pickBeeper();
		
		while (frontIsClear()) {
			move();
		}
		pickBeeper();
		turnAround();
	}
	
	
	/*
	 * Karel oscillates East and West, picking up all remaining beepers
	 * as it approaches the center.
	 */
	private void pickAllOtherBeepers() {
		move();
		while (beepersPresent()) {
			move();
			
			if (noBeepersPresent()) {
				turnAround();
				move();
				pickBeeper();
				move();
			}
		}
	}
	
	
	/* 
	 * Karel drops a beeper at the central corner (or the first of two
	 * central midpoints if even) after it has removed all other beepers
	 * and located the midpoint.
	 */
	private void dropMidpointBeeper() {
		turnAround();
		move();
		putBeeper();
	}
}
