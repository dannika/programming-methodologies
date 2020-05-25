/*
 * File: Pyramid.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * ------------------
 * Draws a pyramid consisting of bricks arranged in horizontal rows, so
 * that the number of bricks in each row decreases by one as one moves
 * up the pyramid. The pyramid is centered at the bottom of the window,
 * and each brick's width and height, alongside the number of bricks in
 * the base of the pyramid, are constants.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

	/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

	/** Height of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

	/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	
	/* Adds every brick to console to build pyramid. */
	public void run() {
		buildPyramid();
	}
	
	
	/*
	 * Iterates 14 times (14 layers), calling buildLayer() each time to
	 * add the appropriate number of bricks to each layer.
	 */
	private void buildPyramid() {
		for(int layer = 0; layer < BRICKS_IN_BASE; layer++ ) {
				buildLayer(layer);
		}
	}
	
	
	/*
	 * Adds appropriate number of bricks corresponding to layer, which
	 * is passed as a parameter as buildPyramid() loops through integers.
	 * The number of bricks per layer decreases by one as each layer
	 * increases by one. Each layer of bricks is centered on the console,
	 * with the base of the pyramid (first layer) along the bottom of
	 * the console.
	 */
	private void buildLayer(int layer) {
		int bricksInLayer = BRICKS_IN_BASE - layer;
		for(int n = 0; n < bricksInLayer; n++) {
			GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
			double x = (getWidth() / 2.0)
					- (BRICK_WIDTH * bricksInLayer / 2.0)
					+ (BRICK_WIDTH * n);
			double y = getHeight() - (BRICK_HEIGHT * (layer + 1));
			add(brick, x, y);
		}
	}
}

