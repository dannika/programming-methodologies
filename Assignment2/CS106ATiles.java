/*
 * File: CS106ATiles.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * ----------------------
 * Displays four tiles each containing the text "CS106A," which is
 * centered in each of the four respective tiles. Each of the four tiles
 * themselves are centered around the midpoint of the window, with 20
 * pixels of space separating each one.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class CS106ATiles extends GraphicsProgram {
	
	/** Amount of space (in pixels) between tiles */
	private static final int TILE_SPACE = 20;
	
	/* Width of each tile in pixels. */
	private static final int TILE_WIDTH = 144;
	
	/* Length of each tile in pixels. */
	private static final int TILE_LENGTH = 72;

	
	/*
	 * Draws all four tiles and their corresponding labels to the
	 * console, calling upon methods makeTiles() and makeLabels().
	 */
	public void run() {
		makeTiles();
		makeLabels();
	}
	

	/*
	 * Draws four tiles of constant width and length. The tiles are
	 * organized into two rows of two tiles. The four-tile figure is
	 * centered in the window, and each tile has a constant 20 pixels 
	 * of space between the adjacent.
	 */
	private void makeTiles() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				GRect tile = new GRect(TILE_WIDTH, TILE_LENGTH);
				double x = (getWidth() / 2.0)
						- (TILE_WIDTH + (TILE_SPACE / 2.0))
						+ ((TILE_WIDTH + TILE_SPACE) * j);
				double y = (getHeight() / 2.0)
						+ (TILE_LENGTH * (i - 1))
						- (TILE_SPACE / 2.0) + (i * TILE_SPACE);
				add(tile, x, y);
			}
		}
	}
	
	
	/*
	 * Draws four "CS106A" labels. Each label is centered inside of a
	 * respective tile by referencing the (x, y) coordinate of the top
	 * left tile and continuing left to right, row by row.
	 */
	private void makeLabels() {
		double firstTileY = (getHeight() / 2.0)
				- (TILE_LENGTH) - (TILE_SPACE / 2.0);
		for (int i = 0; i < 2; i++) {
			double firstTileX = (getWidth() / 2.0)
					- (TILE_WIDTH + (TILE_SPACE / 2.0));
			for (int j = 0; j < 2; j++) {
				GLabel label = new GLabel("CS106A");
				double x = firstTileX
						+ (TILE_WIDTH / 2.0)
						- (label.getWidth() / 2.0);
				double y = firstTileY
						+ (TILE_LENGTH / 2.0)
						+ (label.getAscent() / 2.0);
				add(label, x, y);
				
				firstTileX = firstTileX + TILE_SPACE + TILE_WIDTH;
			}
			firstTileY = firstTileY + TILE_SPACE + TILE_LENGTH;
		}
	}
}

