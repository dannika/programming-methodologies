/*
 * File: HangmanCanvas.java
 * ------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This file keeps track of the Hangman display. Displays scaffold and adds body parts
 * to hanging man as user guesses incorrectly. Also updates and reprints labels of the
 * word as it is being uncovered and the user's incorrect guesses.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	
	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	/* Dimensions of canvas. */
	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;
	
	/* Reference coordinates used when adding scaffold and body parts to canvas. */
	private double x = (WIDTH / 2) - BEAM_LENGTH;
	private double y = (HEIGHT / 2) + (SCAFFOLD_HEIGHT / 4);
	private double bodyX = x + BEAM_LENGTH;
	private double bodyY = y - SCAFFOLD_HEIGHT + ROPE_LENGTH;
	
	/* Display guessWord and incorrectLetters as they are updated throughout gameplay. */
	private GLabel secretWordDisplay = null;
	private GLabel wrongGuessDisplay = null;
	
	/* Stores incorrect letters as they are guessed. Used to update wrongGuessDisplay. */
	private String wrongGuesses = "";
	
	/* Number of incorrect guesses (applied in drawBody() switch/case statement) */
	private int userGuesses = 0;
	
	
	/* Method: reset() */
	/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		drawScaffold();
	}
	
	
	/* Method: drawScaffold() */
	/* Draws the scaffold from which the man will be hung as the game progresses. */
	private void drawScaffold() {
		GLine scaffold = new GLine(x, y, x, y - SCAFFOLD_HEIGHT);
		GLine beam = new GLine(x, y - SCAFFOLD_HEIGHT, x + BEAM_LENGTH, y - SCAFFOLD_HEIGHT);
		GLine rope = new GLine(x + BEAM_LENGTH, y - SCAFFOLD_HEIGHT, 
				x + BEAM_LENGTH, y - SCAFFOLD_HEIGHT + ROPE_LENGTH);
		
		add(scaffold);
		add(beam);
		add(rope);
	}

	
	/* Method: displayWord(String word) */
	/**
	 * Updates the word on the screen to correspond to the current
	 * state of the game.  The argument string shows what letters have
	 * been guessed so far; unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		if (secretWordDisplay != null) {
			remove(secretWordDisplay);
		}
		secretWordDisplay = new GLabel(word, x, y + 50);
		secretWordDisplay.setFont("SansSerif-35");
		add(secretWordDisplay);
	}

	
	/* Method: noteIncorrectGuess(char letter) */
	/**
	 * Updates the display to correspond to an incorrect guess by the
	 * user.  Calling this method causes the next body part to appear
	 * on the scaffold (addBodyPart()) and adds the letter to the list of
	 * incorrect guesses that appears at the bottom of the window.
	 */
	public void noteIncorrectGuess(char letter) {
		if (wrongGuessDisplay != null) {
			remove(wrongGuessDisplay);
		}
		wrongGuesses += letter;
		userGuesses++;
		addBodyPart();
		
		wrongGuessDisplay = new GLabel(wrongGuesses, x, y + 100);
		wrongGuessDisplay.setFont("SansSerif-20");
		add(wrongGuessDisplay);
	}
	
	
	/* Method: addBodyPart() */
	/*
	 * Draws a body part to the canvas depending on how many incorrect guesses the user
	 * has entered. Implements switch/case control flow, in which the number userGuesses
	 * corresponds with a body part do be drawn with each incorrect guess.
	 */
	private void addBodyPart() {
		switch(userGuesses) {
		case 0: 
			break;
		case 1: 
			drawHead();
			break;
		case 2:
			drawBody();
			break;
		case 3:
			drawLeftArm();
			break;
		case 4:
			drawRightArm();
			break;
		case 5:
			drawLeftLeg();
			break;
		case 6:
			drawRightLeg();
			break;
		case 7:
			drawLeftFoot();
			break;
		case 8:
			drawRightFoot();
			break;
		}
	}
	
	
	/* Method: drawHead() */
	/* Draws the hangman's head. */
	private void drawHead() {
		GOval head = new GOval(bodyX - HEAD_RADIUS, bodyY, 
				2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		add(head);
	}
	
	
	/* Method: drawBody() */
	/* Draws the hangman's body. */
	private void drawBody() {
		GLine body = new GLine(bodyX, bodyY + 2 * HEAD_RADIUS, 
				bodyX, bodyY + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(body);
	}
	
	
	/* Method: drawLeftArm() */
	/* Draws the hangman's left arm. */
	private void drawLeftArm() {
		GLine leftArm = new GLine(bodyX, bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				bodyX - UPPER_ARM_LENGTH, bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		GLine leftForearm = new GLine(bodyX - UPPER_ARM_LENGTH, 
				bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				bodyX - UPPER_ARM_LENGTH, 
				bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(leftArm);
		add(leftForearm);
	}
	
	
	/* Method: drawRightArm() */
	/* Draws the hangman's right arm. */
	private void drawRightArm() {
		GLine rightArm = new GLine(bodyX, bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				bodyX + UPPER_ARM_LENGTH, bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		GLine rightForearm = new GLine(bodyX + UPPER_ARM_LENGTH,
				bodyY + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				bodyX + UPPER_ARM_LENGTH,
				bodyY + 2*HEAD_RADIUS+ ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(rightArm);
		add(rightForearm);
	}
	
	
	/* Method: drawLeftLeg() */
	/* Draws the hangman's left leg. */
	private void drawLeftLeg() {
		GLine leftUpperLeg = new GLine(bodyX, bodyY + 2 * HEAD_RADIUS+ BODY_LENGTH,
				bodyX - HIP_WIDTH, bodyY + 2*HEAD_RADIUS+ BODY_LENGTH);
		GLine leftLowerLeg = new GLine(bodyX - HIP_WIDTH, 
				bodyY + 2 * HEAD_RADIUS + BODY_LENGTH,
				bodyX - HIP_WIDTH, bodyY + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(leftUpperLeg);
		add(leftLowerLeg);
	}
	
	
	/* Method: drawRightLeg() */
	/* Draws the hangman's right leg. */
	private void drawRightLeg() {
		GLine rightUpperLeg = new GLine(bodyX, bodyY + 2 * HEAD_RADIUS + BODY_LENGTH,
				bodyX + HIP_WIDTH, bodyY + 2 * HEAD_RADIUS + BODY_LENGTH);
		GLine rightLowerLeg = new GLine(bodyX + HIP_WIDTH, 
				bodyY + 2 * HEAD_RADIUS + BODY_LENGTH,
				bodyX + HIP_WIDTH, bodyY + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(rightUpperLeg);
		add(rightLowerLeg);
	}
	
	
	/* Method: drawLeftFoot() */
	/* Draws the hangman's left foot. */
	private void drawLeftFoot() {
		GLine leftFoot = new GLine(bodyX - HIP_WIDTH,
				bodyY + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH,
				bodyX - HIP_WIDTH - FOOT_LENGTH,
				bodyY + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(leftFoot);
	}

	
	/* Method: drawRightFoot() */
	/* Draws the hangman's right foot. */
	private void drawRightFoot() {
		GLine rightFoot = new GLine(bodyX + HIP_WIDTH,
				 bodyY + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH,
				 bodyX + HIP_WIDTH + FOOT_LENGTH,
				 bodyY + 2 * HEAD_RADIUS+ BODY_LENGTH+ LEG_LENGTH);
		add(rightFoot);
	}
}
