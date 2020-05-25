/*
 * File: NameSurferGraphExtended.java
 * ---------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * Extension of NameSurferGraph.
 * 
 * Extensions include:
 * (1) A deleteEntry() method, which removes individual name plots from the graph. If the 
 *     user enters a name in the text field that has already been graphed and clicks 
 *     "Delete", that single line plot will be removed.
 * (2) Plotted points (circles, squares, diamonds, and triangles), which correspond to
 *     each color plot line.
 * (3) Bold and upper case top-ranked name, to distinguish it from other peasant names.
 * (4) Font size adjusts as the application size change.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraphExtended extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/* Constructor: NameSurferGraph() */
	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraphExtended() {
		addComponentListener(this);
		namesDisplayed = new ArrayList<NameSurferEntry>();
	}
	
	
	/* Method: clear() */
	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		namesDisplayed.clear();
	}
	
	
	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display (if the entry is not
	 * already in the list).
	 * 
	 * @param NameSurferEntry entry, a single entry in the database
	 */
	public void addEntry(NameSurferEntry entry) {
		if (!(namesDisplayed.contains(entry))) {
			namesDisplayed.add(entry);
		}
	}
	
	
	/* Method: deleteEntry(entry) */
	/**
	 * Deletes a NameSurferEntry from the list of entries on the display (if the entry is
	 * in the list).
	 * 
	 * @param String entry, a name entry in the JTextField nameField
	 */
	public void deleteEntry(NameSurferEntry entry) {
		if (namesDisplayed.contains(entry)) {
			namesDisplayed.remove(entry);
		}
	}
	
	
	/* Method: update() */
	/**
	 * Updates the display image by deleting all the graphical objects from the canvas and 
	 * then reassembling the display according to the list of entries. It is also called 
	 * whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		drawGrid();
		if (namesDisplayed.size() > 0) {
			for (int i = 0; i < namesDisplayed.size(); i++) {
				NameSurferEntry entry = namesDisplayed.get(i);
				drawNameEntry(entry, i);
			}
		}
	}
	
	
	/* Method: drawGrid() */
	/**
	 * Assembles the skeleton of the display grid. Invokes drawVerticalLines(),
	 * drawHorizontalLines(), and drawDecades() to draw vertical lines, horizontal lines,
	 * and decade labels, respectively.
	 */
	private void drawGrid() {
		drawVerticalLines();
		drawHorizontalLines();
		drawDecades();
	}
	
	
	/* Method: drawVerticalLines() */
	/**
	 * Draws vertical grid lines to the display. The width of the canvas (which is resizable)
	 * is divided by NDECADES into equally-spaced segments. At each segment, a vertical line
	 * of length getHeight() is added to the canvas, creating a series of vertical lines
	 * that are equally spaced at each decade.
	 */
	private void drawVerticalLines() {
		double spacing = getWidth() / NDECADES;
		for (int i = 0; i < NDECADES; i++) {
			double x0 = i * spacing;
			double y0 = 0;
			double x1 = x0;
			double y1 = getHeight();
			add(new GLine(x0, y0, x1, y1));
		}
	}
	
	
	/* Method: drawHorizontalLines() */
	/**
	 * Draws horizontal grid lines to the display. These two lines serve as the top and
	 * and bottom borders, in which the top border is the Number 1 ranked name and the
	 * bottom border denotes unranked names.
	 */
	private void drawHorizontalLines() {
		GLine top = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		GLine bottom = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, 
				getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(top);
		add(bottom);
	}
	
	
	/* Method: drawDecades() */
	/**
	 * Draws decade labels to the canvas. For each decade in the span of [START_DECADE, 
	 * NDECADES], a GLabel containing the decade year is added. Each label lies along its
	 * corresponding vertical line, which are equally spaced relative to the width of the
	 * resizable canvas.
	 */
	private void drawDecades() {
		for (int i = 0; i < NDECADES; i++) {
			int decadeInt = START_DECADE;
			decadeInt += 10 * i;
			String decadeStr = Integer.toString(decadeInt);
			double x = 2 + i * (getWidth() / NDECADES);
			double y = getHeight() - GRAPH_MARGIN_SIZE / 4;
			GLabel decadeLabel = new GLabel(decadeStr, x, y);
			add(decadeLabel);
		}
	}
	
	
	/* Method: drawNameEntry(entry, entryNum) */
	/**
	 * Draws a name's line plot and labels to the canvas. A line is plotted between each
	 * decade based on the name's rank (relative to the height of the canvas). At each 
	 * decade (represented by a vertical line), a label is drawn, displaying the name
	 * and that name's rank during that specific decade. After all iterations, the final
	 * display is a line graph of the name's popularity at various decades over the time span.
	 * 
	 * Extension: Each rank is plotted along the line with a certain shape. The shapes
	 * vary alongside color and are also cycled through.
	 * 
	 * @param NameSurferEntry entry
	 * @param int entryNum
	 */
	private void drawNameEntry(NameSurferEntry entry, int entryNum) {
		entryNum %= 4;
		double spacing = getWidth() / NDECADES;
		double actualHeight = getHeight() - (GRAPH_MARGIN_SIZE * 2);
		
		for (int i = 0; i < NDECADES - 1; i++) {
			int rank1 = entry.getRank(i);
			int rank2 = entry.getRank(i + 1);
			double x0 = spacing * i;
			double y0 = 0;
			double x1 = spacing * (i + 1);
			double y1 = 0;
			
			y0 = setY(actualHeight, rank1);
			y1 = setY(actualHeight, rank2);
			
			GLine line = new GLine(x0, y0, x1, y1);
						
			GLabel label1 = createLabel(entry, rank1);
			GLabel label2 = createLabel(entry, rank2);

			GObject icon = setIcon(entryNum);
			
			colorPlotLines(line, label1, label2, icon, entryNum);
			
			add(line);
			add(label1, x0 + 2, y0 - 2);
			add(label2, x1 + 2, y1 - 2);
			add(icon, x0 - 2.5, y0 - 2.5);
			add(icon, x1 - 2.5, y1 - 2.5);
		}
	}
	
	
	/* Method: setY(actualHeight, rank) */
	/**
	 * Sets the y-value of a line/label endpoint.
	 * 
	 * @param double actualHeight, the height of the canvas minus the graph margin
	 * @param int rank, a name's rank at a particular decade
	 * @return double y, a y coordinate
	 */
	private double setY(double actualHeight, int rank) {
		double y = 0.0;
		if (rank != 0) {
			y = ((actualHeight / MAX_RANK) * rank) + GRAPH_MARGIN_SIZE;
		} else if (rank == 0) {
			y = getHeight() - GRAPH_MARGIN_SIZE;
		}
		return y;
	}
	
	
	/* Method: createLabel(entry, rank) */
	/**
	 * Creates a label for each entry. If the entry name's rank != 0, the label is created
	 * with the name and appropriate rank for that decade. Otherwise, if the rank == 0,
	 * the label is created with the name and an asterisk to denote an unranked name.
	 * 
	 * Extension: Makes top-ranked name label boldface.
	 * 
	 * @param NameSurferEntry entry, a single entry in the database
	 * @param int rank, an entry's rank at a certain decade
	 * @return GLabel label, the name label for a specific decade
	 */
	private GLabel createLabel(NameSurferEntry entry, int rank) {
		GLabel label = new GLabel("");
		label.setFont(new Font("Arial", Font.PLAIN, getWidth() / 60));
		if (rank == 1) {
			label.setLabel(entry.getName().toUpperCase() + " " + rank);
			label.setFont("Arial-bold");
		} else if (rank == 0) {
			label.setLabel(entry.getName() + " *");
		} else {
			label.setLabel(entry.getName() + " " + rank);
		}
		return label;
	}
	
	
	/* Method: setIcon(entryNum) */
	/**
	 * Coordinates the icon plotted at each decade. If entryNum % 4 == 0, the icon is a
	 * circle. If entryNum % 4 == 1, the icon is a square. This pattern continues (remainder
	 * of 2 corresponds with triangles and 3, with diamonds) such that the icons cycle in
	 * the same sequence. 
	 * 
	 * @param int entryNum, the entry's number, which determines icon shape
	 * @return GObject, a filled GObject that will plot a rank at each decade
	 */
	private GObject setIcon(int entryNum) {
		double iconWidth = 5.0;
		switch(entryNum) {
			case 0:
				GOval dot = new GOval(iconWidth, iconWidth);
				dot.setFilled(true);
				return dot;
			case 1:
				GRect square = new GRect(iconWidth, iconWidth);
				square.setFilled(true);
				return square;
			case 2:
				GPolygon triangle = new GPolygon();
				triangle.addVertex(0, -iconWidth / 2);
				triangle.addVertex(iconWidth / 2, iconWidth / 2);
				triangle.addVertex(-iconWidth / 2, iconWidth / 2);
				triangle.setFilled(true);
				return triangle;
			case 3:
				GPolygon diamond = new GPolygon();
				diamond.addVertex(-iconWidth / 2, 0);
				diamond.addVertex(0, iconWidth);
				diamond.addVertex(iconWidth / 2,  0);
				diamond.addVertex(0,  -iconWidth);
				diamond.setFilled(true);
				return diamond;
			default:
				return null;
		}
	}
	
	
	/* Method: colorPlotLines(line, label1, label2, entryNum) */
	/**
	 * Coordinates the color of each plot line. If entryNum % 4 == 0, the plot line and
	 * labels will be black. If entryNum % 4 == 1, the plot lines and labels will be red.
	 * This pattern continues (remainder of 2 corresponds with blue and 3, with magenta)
	 * such that the colors cycle in the same sequence.
	 * 
	 * @param GLine line, the line to be drawn between two decades
	 * @param GLabel label1, the first label
	 * @param GLabel label2, the second label
	 * @param int entryNum, the entry's number, which determines color
	 */
	private void colorPlotLines(GLine line, GLabel label1, GLabel label2, 
			GObject icon, int entryNum) {
		switch (entryNum) {
		case 0:
			line.setColor(Color.BLACK);
			label1.setColor(Color.BLACK);
			label2.setColor(Color.BLACK);
			icon.setColor(Color.BLACK);
			break;
		case 1:
			line.setColor(Color.RED);
			label1.setColor(Color.RED);
			label2.setColor(Color.RED);
			icon.setColor(Color.RED);
			break;
		case 2:
			line.setColor(Color.BLUE);
			label1.setColor(Color.BLUE);
			label2.setColor(Color.BLUE);
			icon.setColor(Color.BLUE);
			break;
		case 3:
			line.setColor(Color.MAGENTA);
			label1.setColor(Color.MAGENTA);
			label2.setColor(Color.MAGENTA);
			icon.setColor(Color.MAGENTA);
			break;
		}
	}
	

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	/* Private instance variables */
	private ArrayList<NameSurferEntry> namesDisplayed;
}