/*
 * File: Target.java
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * -----------------
 * Draws a 3-layered target of alternating red and white colors.
 * The outer layer is red and has a radius of 72 pixels. The middle
 * layer is white and 0.65 times the size of the outer layer. The inner
 * layer is red and 0.3 times the size of the outer layer.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	
	/*
	 * Declares a constant RADIUS of 72 pixels (1 inch) from which the
	 * sizing and scale of each layer is based.
	 */
	private static final int RADIUS = 72;
	
	
	/* Draws the target. */
	public void run() {
		drawTarget();
	}
	
	
	/*
	 * Draws each layer of the target. The outer layer is red and has a
	 * radius of 72 pixels. The middle layer is white and is 0.65 times
	 * the size of the outer layer. The inner layer is red and is 0.3
	 * times the size of the outer layer.
	 */
	private void drawTarget() {
		drawCircle(1, Color.RED);
		drawCircle(0.65, Color.WHITE);
		drawCircle(0.3, Color.RED);
	}
	
	
	/*
	 * Draws a circular object based on parameters of scale and color.
	 * Each circle's dimensions are based upon the constant RADIUS of 72
	 * and the scale of each layer. Color is also determined upon 
	 * calling method. The circle is centered in the window using
	 * getWidth() and getHeight() methods and dividing by 2.0.
	 */
	private void drawCircle(double scale, Color color) {
		GOval circle = new GOval(RADIUS * 2 * scale, RADIUS * 2 * scale);
		circle.setColor(color);
		circle.setFilled(true);
		
		double x = getWidth() / 2.0 - circle.getWidth() / 2.0;
		double y = getHeight() / 2.0 - circle.getHeight() / 2.0;
		
		add(circle, x, y);
	}
}
