/*
 * File: Breakout.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * -------------------
 * Runs the game of Breakout. 
 * 
 * The initial configuration consists of a figure of assorted colored bricks
 * and a paddle that is fixed in the vertical dimension, but is traced by
 * the mouse in the horizontal direction until it reaches the edge of its space.
 * A ball bounces off of any obstacle it collides with (bricks, borders, paddle),
 * but the user moves on to the next turn if the ball falls below the bottom
 * border of the window. If the user can successfully eliminates all of the
 * bricks within three turns, he/she wins the game. Otherwise, the game ends.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** 
	 * Width and height of application window in pixels.  IMPORTANT 
	 * NOTE: ON SOME PLATFORMS THESE CONSTANTS MAY **NOT** ACTUALLY BE
	 * THE DIMENSIONS OF THE GRAPHICS CANVAS.  Use getWidth() and 
	 * getHeight() to get the dimensions of the graphics canvas. 
	 */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/**
	 * Dimensions of game board.  IMPORTANT NOTE: ON SOME PLATFORMS 
	 * THESE CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS OF THE 
	 * GRAPHICS CANVAS.  Use getWidth() and getHeight() to get the 
	 * dimensions of the graphics canvas. 
	 */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	/* Number of milliseconds to pause on each cycle of movement */
	private static final int PAUSE_TIME = 10;
	
	
	/* Establishes GRect variable paddle */
	private GRect paddle;
	
	
	/* Establishes GOval variable ball */
	private GOval ball;
	
	
	/* Establish double velocities vx and vy */
	private double vx, vy;
	
	
	/* Holds random generator instance */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	
	/* Records the number of bricks remaining in the game. */
	private int bricksRemaining = NBRICK_ROWS * NBRICKS_PER_ROW;
	
	
	/* Method: run() */
	/* 
	 * Runs the Breakout program. If the user eliminates all of the
	 * bricks in three turns, he/she wins the game. Otherwise, the
	 * game ends.
	 */
	public void run() {
		setUpGame();
		for (int i = 0; i < NTURNS; i++) {
			playGame();
			if (bricksRemaining == 0) {
				removeAll();
				printResult("Y O U  W I N");
				break;
			}
		}
		if (bricksRemaining > 0) {
			removeAll();
			printResult("G A M E  O V E R");
		}
	}
	
	
	/* Method: setUpGame() */
	/*
	 * Registers the program as a listener for mouse events that occur
	 * within the canvas. Program responds to mouse movement and click.
	 * Calls upon setUpBricks() and setUpPaddle() methods to construct
	 * the brick figure and draw the paddle, respectively.
	 */
	private void setUpGame() {
		addMouseListeners();
		setUpBricks();
		setUpPaddle();
	}
	
	
	/* Method: playGame() */
	/*
	 * Begins the gameplay. A ball is drawn, and once the mouse is 
	 * clicked, the ball "drops," colliding with various barriers
	 * (edges of application window, paddle, bricks). If the ball falls
	 * below lower border, or if the user eliminates all of the bricks, the
	 * program breaks out of recursion (ball stops moving).
	 */
	private void playGame() {
		makeBall();
		waitForClick();
		vNaughtBall();
		while (true) {
			moveBall();
			if (ball.getY() >= HEIGHT) {
				break;
			}
			if (bricksRemaining == 0) {
				break;
			}
		}
		
	}
	
	
	/* Method: setUpBricks() */
	/*
	 * Sets up the figure of bricks of dimensions NBRICK_ROWS x NBRICKS_PER_ROW
	 * by calling buildBrickRow() method NBRICK_ROWS times. Every two rows of 
	 * bricks are a different color.
	 */
	private void setUpBricks() {
		for (int row = 0; row < NBRICK_ROWS; row++) {
			for (int num = 0; num < NBRICKS_PER_ROW; num++) {
				if (row <= 1) {
					buildBrickRow(row, num, Color.RED);
				} else if (row <= 3) {
					buildBrickRow(row, num, Color.ORANGE);
				} else if (row <= 5) {
					buildBrickRow(row, num, Color.YELLOW);
				} else if (row <= 7) {
					buildBrickRow(row, num, Color.GREEN);
				} else {
					buildBrickRow(row, num, Color.CYAN);
				}
			}
		}
	}
	
	
	/* Method: buildBrickRow() */
	/*
	 * Draws a row of bricks. Each brick is of constant WIDTH and
	 * HEIGHT, is filled, and has a constant space of BRICK_SEP between each
	 * brick. Takes parameters row and width, which are affected by the
	 * iterations of setUpBricks(), and color, which is determined by the 
	 * if-else block in setUpBricks().
	 */
	private void buildBrickRow(int row, int num, Color color) {
		GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setColor(color);
		double x = (BRICK_SEP / 2.0) + (BRICK_WIDTH + BRICK_SEP) * num;
		double y = BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * (row + 1);
		add(brick, x, y);
	}
	
	
	/* Method: setUpPaddle() */
	/*
	 * Draws a black rectangular paddle, which is initially centered
	 * along the x-axis and offset PADDLE_Y_OFFSET from the bottom
	 * of the window.
	 */
	private void setUpPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		double x = (WIDTH / 2.0) - (paddle.getWidth() / 2.0);
		double y = (HEIGHT - PADDLE_Y_OFFSET);
		paddle.setFilled(true);
		add(paddle, x, y);
	}
	
	
	/* Method: mouseMoved() */
	/*
	 * Mouse tracks the center of the paddle. Prevents the paddle from
	 * going outside the bounds of the window [0, WIDTH], regardless
	 * of how far outside the bounds the mouse is moved. Only affects
	 * x-coordinate of paddle, as y-coordinate is of constant offset
	 * from bottom border.
	 */
	public void mouseMoved(MouseEvent e) {
		paddle.setLocation(e.getX() - PADDLE_WIDTH / 2.0, 
				HEIGHT - PADDLE_Y_OFFSET);
		if (paddle.getX() <= 0) {
			paddle.setLocation(0, HEIGHT - PADDLE_Y_OFFSET);
		} else if (paddle.getX() + PADDLE_WIDTH >= WIDTH) {
			paddle.setLocation(WIDTH - PADDLE_WIDTH, 
					HEIGHT - PADDLE_Y_OFFSET);
		}
	}
	
	
	/* Method: makeBall() */
	/* Draws a black circular ball initially centered in the window. */
	private void makeBall() {
		ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		double x = (WIDTH / 2.0) - BALL_RADIUS;
		double y = (HEIGHT / 2.0) - BALL_RADIUS;
		add(ball, x, y);
	}
	
	
	/* Method: vNaughtBall() */
	/* Initializes velocity variables, vx being randomly generated. */
	private void vNaughtBall() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = 3.0;
	}
	
	
	/* Method: moveBall() */
	/*
	 * Ball moves throughout window based on velocities vx and vy.
	 * 
	 * If the ball hits a wall or the paddle, it's directional velocity is
	 * reversed. If the ball hits a brick, that brick is removed from 
	 * the window, the ball's directional velocity is reversed, and
	 * bricksRemaining is decreased by one. 
	 * 
	 * The pause method causes a delay of PAUSE_TIME (in milliseconds)
	 * in each cycle of movement.
	 */
	private void moveBall() {
		ball.move(vx, vy);
		checkForWalls();
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			vy = -1 * Math.abs(vy); // prevents ball from 'sticking' to paddle
		} else if (collider != null) {
			remove(collider);
			vy = -vy;
			bricksRemaining--;
		}
		pause(PAUSE_TIME);
	}
	
	
	/* Method: checkForWalls() */
	/*
	 * If the ball hits the left or right borders, its horizontal
	 * velocity is reversed. Likewise, if the ball hits the top border,
	 * its vertical velocity is reversed.
	 */
	private void checkForWalls() {
		if ((ball.getX() <= 0) || (ball.getX() >= (WIDTH - BALL_RADIUS * 2))) {
			vx = -vx;
		}
		if (ball.getY() <= 0) {
			vy = -vy;
		}
	}
	
	
	/* Method: getCollidingObject() */
	/*
	 * Checks if there is an object at each "corner" of the ball, which
	 * essentially determines if the ball has hit either the paddle or
	 * a brick. If the result of calling getElementAt() a corner is not
	 * the value null, then the object at that coordinate (the object
	 * that the ball has collided into) is returned. The value null is
	 * returned if there is no object present, and the ball continues 
	 * to move.
	 */
	private GObject getCollidingObject() {
		double x = ball.getX();
		double y = ball.getY();
		double diameter = 2 * BALL_RADIUS;
		if (getElementAt(x, y) != null) {
			return (getElementAt(x, y));
		} else if (getElementAt(x + diameter, y) != null) {
			return (getElementAt(x + diameter, y));
		} else if (getElementAt(x, y + diameter) != null) {
			return (getElementAt(x, y + diameter));	
		} else if (getElementAt(x + diameter, y + diameter) != null) {
			return (getElementAt(x + diameter, y + diameter));
		} else {
			return (null);
		}
	}
	
	
	/* Method: printResultString() */
	/*
	 * Draws a label that displays a game-ending message to the user. 
	 * Accepts a string as a parameter - the string dependent upon the
	 * result of the game. If the user successfully eliminates all of 
	 * the blocks from the window within three turns, then the user 
	 * wins the game ("YOU WIN"). Otherwise, the game is over ("GAME
	 * OVER.").
	 */
	private void printResult(String result) {
		GLabel label = new GLabel(result);
		label.setFont("SansSerif-40");
		double x = (WIDTH / 2.0) - (label.getWidth() / 2.0);
		double y = (HEIGHT / 2.0) - (label.getAscent() / 2.0);
		add(label, x, y);
	}
}
