/*
 * File: NameSurferExtended.java
 * ---------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * Extension of NameSurferProject.
 * 
 * Extensions include:
 * (1) A delete button, which removes individual name plots from the graph. If the user enters
 *     a name in the text field that has already been graphed and clicks "Delete", that single
 *     line plot will be removed.
 * (2) Plotted points (circles, squares, diamonds, and triangles), which correspond to
 *     each color plot line.
 * (3) Bold and upper case top-ranked name, to distinguish it from other peasant names.
 * (4) Font size adjusts as the application size change.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurferExtended extends Program implements NameSurferConstants {

	/* Method: init() */
	/**
	 * Initializes the interactors at the top of the window. Implements action listeners
	 * to allow for invocation of actionPerformed() method. Reads in the database of names
	 * and rankings. Initializes and adds graph to the window.
	 */
	public void init() {
	    createController();
	    addActionListeners();
	    namesDataBase = new NameSurferDataBase(NAMES_DATA_FILE);
	    graph = new NameSurferGraphExtended();
	    add(graph);
	}
	
	
	/* Method: createController() */
	/**
	 * Creates panel of interactors at the top of the window. The control strip includes a
	 * name label, a text field that accepts a name entry of max length MAX_NAME, a graph 
	 * button which will eventually graph a valid name entry, and a clear button which will
	 * clear all line plots from the graph. The text field detects a pressed ENTER key action,
	 * which will also eventually graph a valid name entry.
	 */
	private void createController() {
		nameField = new JTextField(MAX_NAME);
		nameField.addActionListener(this);
		graphButton = new JButton("Graph");
		deleteButton = new JButton("Delete");
		clearButton = new JButton("Clear");
		
		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		add(graphButton, NORTH);
		add(deleteButton, NORTH);
		add(clearButton, NORTH);
	}

	
	/* Method: actionPerformed(e) */
	/**
	 * Detects when the buttons are clicked of when the ENTER key is pressed in the text
	 * field. If the ENTER key is pressed, or if the "Graph" button is clicked, the name 
	 * entry is graphed on the window (if it exists in the database). If the "Clear" button
	 * is clicked, then all of the plots on the graph are cleared.
	 * 
	 * Extension: If the "Delete" button is clicked and the name in the text field has
	 * already been plotted on the graph, then that name is deleted from the graph.
	 * 
	 * @param ActionEvent e, an event performed while the program is running
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == graphButton || e.getSource() == nameField) {
			String entry = nameField.getText();
			NameSurferEntry nameRankings = namesDataBase.findEntry(entry);
			if (nameRankings != null) {
				graph.addEntry(nameRankings);
				graph.update();
			}
		} else if (e.getSource() == deleteButton) {
			String entry = nameField.getText();
			NameSurferEntry formattedName = namesDataBase.findEntry(entry);
			graph.deleteEntry(formattedName);
			graph.update();
		} else if (e.getSource() == clearButton) {
			graph.clear();
			graph.update();
		}
	}
	
	
	/* Private instance variables */
	private JTextField nameField;
	private JButton graphButton;
	private JButton deleteButton;
	private JButton clearButton;
	private NameSurferDataBase namesDataBase;
	private NameSurferGraphExtended graph;
}