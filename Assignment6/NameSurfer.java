/*
 * File: NameSurfer.java
 * ---------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * Interactive applet that graphs the 1000 most popular boy and girl names for children at 10
 * year intervals over the past 100 years (as supplied by the Social Security Administration).
 * The program contains a control strip of interactors, including a JTextField and a series
 * of JButtons. The user enters a name in the text field, and if the entry is in the database
 * of popular names and their rankings, then it is graphed on the window. The user can
 * continue to add plots to the graph (which are differentiated by color every fourth entry)
 * using the ENTER key or clicking the "Graph" button, and they can clear the graph by 
 * clicking the "Clear" button. The window is also resizable.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

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
	    graph = new NameSurferGraph();
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
		clearButton = new JButton("Clear");
		
		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		add(graphButton, NORTH);
		add(clearButton, NORTH);
	}

	
	/* Method: actionPerformed(e) */
	/**
	 * Detects when the buttons are clicked of when the ENTER key is pressed in the text
	 * field. If the ENTER key is pressed, or if the "Graph" button is clicked, the name 
	 * entry is graphed on the window (if it exists in the database). If the "Clear" button
	 * is clicked, then all of the plots on the graph are cleared.
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
		} else if (e.getSource() == clearButton) {
			graph.clear();
			graph.update();
		}
	}
	
	
	/* Private instance variables */
	private JTextField nameField;
	private JButton graphButton;
	private JButton clearButton;
	private NameSurferDataBase namesDataBase;
	private NameSurferGraph graph;
}
