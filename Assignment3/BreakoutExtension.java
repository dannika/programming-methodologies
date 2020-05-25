/*
 * File: Breakout.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * -------------------
 * Extended version of Breakout game. 
 * 
 * Additions include: 
 * 1) A welcome message on a refreshingly black background.
 * 2) Audible bounces. 
 * 3) Gradual horizontal velocity increase with every paddle hit. 
 * 4) Better user control of gameplay (ball bounces in both x and y direction
 *    when it hits either end of the paddle traveling inward).
 * 5) Ball absorbs color of each brick it hits.
 * 6) Scoring system. Bricks are worth 1, 3, 5, 7, or 9 points depending on color.
 * 7) Visible brick count, score count, and lives remaining count (as GLabels).
 * 	     Aside: These GLabels are established as instance variables. Although I
 *       understand why excessive instance variable declaration is frowned upon,
 *       it was exceedingly difficult/inefficient/messy to try to use parameters
 *       in every single method (including run()?) given the amount of stress
 *       that is placed on decomposition. Stylistically, I just believe this is a
 *       better call, as the code is easy to read/comprehend/follow and is 
 *       appropriately decomposed.
 * 8) Option to replay at the end of every game.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import com.sun.glass.ui.CommonDialogs.Type;

public class BreakoutExtension extends GraphicsProgram {

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
	
	
	/* Number of milliseconds to pause on each cycle */
	private static final int PAUSE_TIME = 10;
	
	
	/* Establishes GRect variable paddle */
	private GRect paddle;
	
	
	/* Establishes GOval variable ball */
	private GOval ball;
	
	
	/* Establish double velocities vx and vy */
	private double vx, vy;
	
	
	/* Holds random generator instance */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	
	/* Holds stat values as they change during play (see displayUserStats()). */
	private int bricksRemaining, points, lives;


	/* Displays various stats in window (see displayUserStats() for details). */
	private GLabel brickCount, score, lifeCount;
	
	
	/* Audio clip of a bounce */
	private AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");

	
	/* Method: run() */
	/* 
	 * Runs the BreakoutExtension program. If the user eliminates all of the
	 * bricks in three turns, he/she wins the game. Otherwise, the game ends.
	 * User is then prompted to click to replay.
	 */
	public void run() {
		welcomeMessage();
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
		promptPlayAgain();
	}
	
	
	/* Method: welcomeMessage() */
	/*
	 * Image source: http://giphy.com/gifs/space-stars-galaxy-peFJZc82UZ4iY 
	 * 
	 * Displays a welcome message to the user alongside an aesthetically
	 * pleasing arrangement of rainbow-colored bricks. Prompts user for a click.
	 * When the window is clicked, the welcome message is removed, allowing for
	 * the setup and gameplay.
	 */
	private void welcomeMessage() {
		GImage space = new GImage("space.gif");
		space.setSize(getWidth(), getHeight());
		add(space);
		setUpBricks();
		
		GLabel welcome = new GLabel("BREAKOUT 2.0");
		welcome.setFont("SansSerif-40");
		welcome.setColor(Color.WHITE);
		double x = (WIDTH / 2.0) - (welcome.getWidth() / 2.0);
		double y = (HEIGHT / 2.0) - (welcome.getAscent() / 2.0);
		add(welcome, x, y);
		
		GLabel taunt = new GLabel("You aren't ready.");
		taunt.setFont("SansSerif-20");
		taunt.setColor(Color.WHITE);
		double x2 = (WIDTH / 2.0) - (taunt.getWidth() / 2.0);
		double y2 = y + (2 * taunt.getAscent());
		add(taunt, x2, y2);
		
		GLabel ready = new GLabel("Click to start.");
		ready.setFont("SansSerif-20");
		ready.setColor(Color.WHITE);
		double x3 = (WIDTH / 2.0) - (ready.getWidth() / 2.0);
		double y3 = y2 + (2 * ready.getAscent());
		add(ready, x3, y3);
		
		waitForClick();
		removeAll();
	}
	
	
	/* Method: setUpGame() */
	/*
	 * Registers the program as a listener for mouse events that occur
	 * within the window. Program responds to mouse movement and click. Calls
	 * displayUserStats() to draw stat labels to the window. Calls setBricks()
	 * and setUpPaddle() methods to construct the brick figure and draw the
	 * paddle, respectively.
	 */
	private void setUpGame() {
		setBackground(Color.BLACK);
		addMouseListeners();
		displayUserStats();
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
				remove(ball);
				lives--;
				lifeCount.setLabel("lives remaining: " + lives);
				break;
			}
			if (bricksRemaining == 0) {
				break;
			}
		}
		
	}
	
	
	/* Method: displayUserStats() */
	/*
	 * Displays the following game statistics (as GLabels) in the window:
	 *   brickCount: number of bricks remaining in the game
	 *   score: the user's accumulation of points
	 *   ballCount: number of turns (balls) the user has left
	 */
	private void displayUserStats() {
		bricksRemaining = NBRICK_ROWS * NBRICKS_PER_ROW;
		brickCount = new GLabel("bricks remaining: " + bricksRemaining);
		brickCount.setColor(Color.WHITE);
		double brickCountY = brickCount.getAscent();
		add(brickCount, 0, brickCountY);
		
		points = 0;
		score = new GLabel("score: " + points);
		score.setColor(Color.WHITE);
		double scoreX = (WIDTH / 2.0) - (score.getWidth() / 2.0);
		double scoreY = HEIGHT - score.getAscent() / 2.0;
		add(score, scoreX, scoreY);
		
		lives = 3;
		lifeCount = new GLabel("lives remaining: " + lives);
		lifeCount.setColor(Color.WHITE);
		double ballCountX = WIDTH - lifeCount.getWidth();
		double ballCountY = lifeCount.getAscent();
		add(lifeCount, ballCountX, ballCountY);
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
	 * Draws a white rectangular paddle, which is initially centered
	 * along the x-axis and offset PADDLE_Y_OFFSET from the bottom
	 * of the window.
	 */
	private void setUpPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		double x = (WIDTH / 2.0) - (paddle.getWidth() / 2.0);
		double y = (HEIGHT - PADDLE_Y_OFFSET);
		paddle.setFilled(true);
		paddle.setColor(Color.WHITE);
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
	/* Draws a white circular ball initially centered in the window. */
	private void makeBall() {
		ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		ball.setColor(Color.WHITE);
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
	 * Each time the ball hits a colliding object, a bouncing sound is generated.
	 *  
	 * If the ball hits a wall, it's directional velocity is reversed. If the ball
	 * hits the paddle, depending on where along the paddle the collision occurred 
	 * and the ball's incoming velocity, the ball's outgoing velocity is changed.
	 * If the ball hits a brick, the statements in brickHit() execute. 
	 * 
	 * The pause method causes a delay of PAUSE_TIME (in milliseconds) in 
	 * each cycle of movement.
	 */
	private void moveBall() {
		ball.move(vx, vy);
		checkForWalls();
		GObject collider = getCollidingObject();
		if (collider instanceof GRect) {
			bounceClip.play();
			if (collider == paddle) {
				checkPaddleCollision();
				vy = -1 * Math.abs(vy); // ball doesn't 'stick' to paddle
			} else {
				brickHit(collider);
			}
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
	
	
	/* Method: checkPaddleCollision() */
	/*
	 * Changes ball's horizontal velocity based on incoming velocity and where
	 * along the paddle the collision occurs. If the ball hits either end of 
	 * the paddle traveling inward, then it is reflected along both the x- and
	 * y- axes. Otherwise, the ball's horizontal velocity is increased
	 * directionally.
	 */
	private void checkPaddleCollision() {
		double firstQuarter = paddle.getX() + PADDLE_WIDTH * 0.25;
		double lastQuarter = paddle.getX() + PADDLE_WIDTH * 0.75;
		if (ball.getX() < firstQuarter && vx > 0) {
				vx = -vx;
			} else if (ball.getX() > lastQuarter && vx < 0) {
				vx = -vx;		
			} else {
				if (vx < 0) {
					vx -= 0.25;
				} else {
					vx += 0.25;
				}
			}
	}
	
	
	/* Method: brickHit() */
	/*
	 * If the object that the ball collides into is a brick, the ball absorbs the
	 * brick's color, and the brick is removed. The value of bricksRemaining
	 * decreases, and the brickCount label updates. The value of points in 
	 * increased depending on the color of the destroyed brick, and the score
	 * label is updated. The vertical velocity of the ball is reversed.
	 */
	private void brickHit(GObject collider) {
		Color brickColor = collider.getColor();
		ball.setColor(brickColor);
		remove(collider);
		bricksRemaining--;
		brickCount.setLabel("bricks remaining: " + bricksRemaining);
		keepScore(collider);
		score.setLabel("score: " + points);
		vy = -vy;
	}
	
	
	/* Method: keepScore() */
	/*
	 * Gives each colored brick a point value. Updates the user's score
	 * as the ball "destroys" various colors of bricks.
	 */
	private void keepScore(GObject collider) {
		if (collider.getColor() == Color.RED) {
			points += 9;
		} else if (collider.getColor() == Color.ORANGE) {
			points += 7;
		} else if (collider.getColor() == Color.YELLOW) {
			points += 5;
		} else if (collider.getColor() == Color.GREEN) {
			points += 3;
		} else if (collider.getColor() == Color.CYAN) {
			points += 1;
		}
	}
	
	
	/* Method: printResultString() */
	/*
	 * Draws a label that displays a game-ending message to the user. 
	 * Accepts a string as a parameter - the string dependent upon the
	 * result of the game. If the user successfully eliminates all of 
	 * the blocks from the window within three turns, then the user 
	 * wins the game ("YOU WIN"). Otherwise, the game is over ("GAME
	 * OVER").
	 */
	private void printResult(String result) {
		GLabel label = new GLabel(result);
		label.setFont("SansSerif-40");
		label.setColor(Color.WHITE);
		double x = (WIDTH / 2.0) - (label.getWidth() / 2.0);
		double y = (HEIGHT / 2.0) - (label.getAscent() / 2.0);
		add(label, x, y);
		
		GLabel finalScore = new GLabel("FINAL SCORE: " + points);
		finalScore.setFont("SansSerif-15");
		finalScore.setColor(Color.WHITE);
		double x2 = (WIDTH / 2.0) - (finalScore.getWidth() / 2.0);
		double y2 = (HEIGHT / 2.0) + (1.5 * finalScore.getAscent());
		add(finalScore, x2, y2);
	}
	
	
	/* Method: promptPlayAgain() */
	/*
	 * Draws a label that prompts the user to click the screen if he/she wants
	 * to replay the game. If the screen is clicked, the run() method is called
	 * and the game starts over from the beginning.
	 */
	private void promptPlayAgain() {
		pause(1000);
		GLabel playAgain = new GLabel("Click to Play Again");
		playAgain.setFont("SansSerif-20");
		playAgain.setColor(Color.WHITE);
		double x = (WIDTH / 2.0) - (playAgain.getWidth() / 2.0);
		double y = (HEIGHT / 2.0) + (3 * playAgain.getAscent());
		add(playAgain, x, y);
		waitForClick();
		removeAll();
		run();
	}
}
