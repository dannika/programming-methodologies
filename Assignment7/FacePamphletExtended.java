/* 
 * File: FacePamphletExtended.java
 * -----------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * Extension of FacePamphlet. Extensions include:
 * (1) Ability to load and save networks from and to a file.
 * (2) JTextField interactors are cleared after every action event, so as to no confuse
 *     the user as they switch between and update profiles.
 * (3) A wonderfully original banner on each profile page.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphletExtended extends Program 
					implements FacePamphletConstantsExtended {

	/* Method: init() */
	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the controller, action listeners, the
	 * database, and the graphics canvas.
	 * 
	 */
	public void init() {
		createController();
		addActionListeners();
		facePamphletDatabase = new FacePamphletDatabaseExtended();
		canvas = new FacePamphletCanvasExtended();
		add(canvas);
    }
	
	
	/* Method: createController() */
	/**
	 * Creates panels of interactors - in the form of JButtons
	 * and JTextFields - along the North and West edges of the 
	 * application.
	 */
	private void createController() {
		createNorthController();
		createWestController();
	}
	
	
	/* Method: createNorthController() */
	/**
	 * Creates a panel of interactors on the North edge of the
	 * application. These interactors include a JTextField that
	 * accepts a profile name as text and three JButtons that
	 * add, delete, and look-up a profile, respectively.
	 */
	private void createNorthController() {
		nameField = new JTextField(TEXT_FIELD_SIZE);
		addButton = new JButton("Add");
		deleteButton = new JButton("Delete");
		lookupButton = new JButton("Lookup");
		fileField = new JTextField(TEXT_FIELD_SIZE);
		loadButton = new JButton("Load");
		saveButton = new JButton("Save");
		
		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		add(addButton, NORTH);
		add(deleteButton, NORTH);
		add(lookupButton, NORTH);
		add(new JLabel("File"), NORTH);
		add(fileField, NORTH);
		add(loadButton, NORTH);
		add(saveButton, NORTH);
	}
	
	
	/* Method: createWestController() */
	/**
	 * Creates a panel of interactors on the West edge of the
	 * application. These interactors include three text fields
	 * and three buttons that each udpate status, update profile
	 * picture, and add a friend, respectively. Each JTextField
	 * also accepts the ENTER key as an action event.
	 */
	private void createWestController() {
		statusField = new JTextField(TEXT_FIELD_SIZE);
		statusField.addActionListener(this);
		changeStatus = new JButton("Change Status");
		
		pictureField = new JTextField(TEXT_FIELD_SIZE);
		pictureField.addActionListener(this);
		changePicture = new JButton("Change Picture");
		
		addField = new JTextField(TEXT_FIELD_SIZE);
		addField.addActionListener(this);
		addFriend = new JButton("Add Friend");
		
		add(statusField, WEST);
		add(changeStatus, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(pictureField, WEST);
		add(changePicture, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(addField, WEST);
		add(addFriend, WEST);
	}
    
  
	/* Method: actionPerformed(e) */
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used. Various methods are called
     * depending on which interactor is acted upon. These
     * methods are only called if the text field is not empty.
     * 
     * @param ActionEvent e, an action upon an object that occurs
     * while the program is running
     */
    public void actionPerformed(ActionEvent e) {
    	Object source = e.getSource();
    	String text = nameField.getText();
    	
    	if (source == addButton && !text.equals("")) {
    		addNewProfile(text);
    	} else if (source == deleteButton && !text.equals("")) {
    		deleteProfile(text);
    	} else if (source == lookupButton && !text.equals("")) {
    		lookupProfile(text);
    	} else if ((source == statusField || source == changeStatus)
    			&& !(statusField.getText().equals(""))) {
    		updateStatus();
    	} else if ((source == pictureField || source == changePicture) 
    			&& !(pictureField.getText().equals(""))) {
    		updatePicture();
    	} else if ((source == addField || source == addFriend)
    			&& !(addField.getText().equals(""))) {
    		addNewFriend();
    	} else if (source == loadButton && !(fileField.getText().equals(""))) {
    		loadFile();
    	} else if (source == saveButton && !(fileField.getText().equals(""))) {
    		saveFile();
    	}
    	clearTextFields();
	}
    
    
    /* Method: addNewProfile(text) */
    /**
     * Adds a new profile to the database, if one with that name
     * does not already exist. If it does already exist in the
     * database, then the existing profile is displayed. Otherwise,
     * the new profile is created and displayed to the canvas.
     * 
     * @param String text, the name of a profile to be added
     */
    private void addNewProfile(String text) {
    	if (facePamphletDatabase.containsProfile(text)) {
			FacePamphletProfileExtended existing = facePamphletDatabase.getProfile(text);
			currentProfile = existing;
			canvas.displayProfile(currentProfile);
			canvas.showMessage("A profile with the name " + text + " already exists");
		} else {
			FacePamphletProfileExtended newProfile = new FacePamphletProfileExtended(text);
			facePamphletDatabase.addProfile(newProfile);
    		currentProfile = newProfile;
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("New profile created");
		}
    }
    
    
    /* Method: deleteProfile(text) */
    /**
     * Deletes a profile from the database, if it exists. If it does
     * not exist, no a message relays so. Otherwise, the profile
     * is removed. The current profile is set to the value null
     * and the canvas is cleared regardless of whether a profile
     * has been deleted.
     * 
     * @param String text, the name of the profile to be deleted
     */
    private void deleteProfile(String text) {
    	currentProfile = null;
		canvas.clearDisplay();
		if (facePamphletDatabase.containsProfile(text)) {
			facePamphletDatabase.deleteProfile(text);
			canvas.showMessage("Profile of " + text + " deleted");
		} else {
			canvas.showMessage("A profile with the name " + text + " does not exist");
		}
    }
    
    
    /* Method: lookupProfile(text) */
    /**
     * Looks up and displays a profile, if it exists. If it does
     * not, a message relay so. Otherwise, the found profile is
     * displayed on the canvas and set to the current profile.
     * 
     * @param String text, the name of the desired profiled
     */
    private void lookupProfile(String text) {
    	if (facePamphletDatabase.containsProfile(text)) {
			currentProfile = facePamphletDatabase.getProfile(text);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Displaying " + text);
		} else {
			canvas.clearDisplay();
			canvas.showMessage("A profile with the name " + text + " does not exist");
			currentProfile = null;
		}
    }
    
    
    /* Method: updateStatus() */
    /**
     * Updates the current profile's status. If there is no current
     * profile, a message relays so. Otherwise, the profile's status is
     * updated and displayed.
     */
    private void updateStatus() {
    	if (currentProfile != null) {
			String newStatus = statusField.getText();
			currentProfile.setStatus(newStatus);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Status updated to " + newStatus);
		} else {
			canvas.clearDisplay();
			canvas.showMessage("Please select a profile to change status");
		}
    }
    
    
    /* Method: updatePicture() */
    /**
     * Updates the current profile's image. If the input in the
     * text field is a valid image filename, the program will
     * read the file and set the image to the profile's picture.
     * An exception is thrown if there is an error reading
     * the file. If there is no current profile, a message
     * relays so.
     */
    private void updatePicture() {
    	GImage profPic = null;
		String filename = pictureField.getText();
		if (currentProfile != null) {
			try {
				profPic = new GImage(filename);
				currentProfile.setImageFileName(filename);
				currentProfile.setImage(profPic);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Picture updated");
			} catch (ErrorException ex) {
				profPic = null;
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Unable to open image file: " + filename);
			}
		} else {
			canvas.clearDisplay();
			canvas.showMessage("Please select a profile to change picture");
		}
    }
    
    
    /* Method: addNewFriend() */
    /**
     * Adds a friend to the current profile's friend list. If the
     * inputed name is not already on the profile's friend list, it
     * is added. Likewise, the name of the current profile is added
     * to the friend list of the inputed name. This method rejects
     * the current profile adding its own name or adding the name
     * of someone already in on its friend list.
     */
    private void addNewFriend() {
    	String friend = addField.getText();
		if (currentProfile != null) {
			if (currentProfile.getName().equals(friend)) {
				canvas.displayProfile(currentProfile);
				canvas.showMessage("You can't add yourself as a friend");
			} else if (facePamphletDatabase.containsProfile(friend)) {
				if (currentProfile.addFriend(friend)) {
					currentProfile.addFriend(friend);
					FacePamphletProfileExtended addedFriend = 
							facePamphletDatabase.getProfile(friend);
					addedFriend.addFriend(currentProfile.getName());
					canvas.displayProfile(currentProfile);
					canvas.showMessage(friend + " added as a friend");
				} else {
					canvas.displayProfile(currentProfile);
					canvas.showMessage(currentProfile.getName() + " already has " + 
							friend + " as a friend");
				}
			} else {
				canvas.displayProfile(currentProfile);
				canvas.showMessage(friend + " does not exist");
			}
		} else {
			canvas.clearDisplay();
			canvas.showMessage("Please select a profile to add friend");
		}
    }
    
    
    /* Method: loadFile() */
    /**
     * Loads a previously saved network. If the inputed file
     * exists, its data is read and stored in the database, 
     * allowing for the user of the current session to access it.
     * Otherwise, an exception is thrown.
     */
    private void loadFile() {
    	String file = fileField.getText();
    	try {
    		facePamphletDatabase.loadFile(file);
    		canvas.clearDisplay();
    		canvas.showMessage("File loaded");
    	} catch (ErrorException ex) {
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("Unable to open file " + file);
    	}
    }
    
    
    /* Method: saveFile() */
    /**
     * Saves the existing network to a new file. Throws an
     * exception if there is an error in writing the file.
     */
    private void saveFile() {
    	String file = fileField.getText();
    	try {
    		facePamphletDatabase.saveFile(file);
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("File saved");
    	} catch (ErrorException ex) {
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("Unable to save file " + file);
    	}
    }
    
    
    /* Method: clearTextFields */
    /** Clears all text fields after every action event. */
    private void clearTextFields() {
    	nameField.setText("");
    	statusField.setText("");
    	pictureField.setText("");
    	addField.setText("");
    	fileField.setText("");
    }
    
    
    /* Private instance variables */
    private JTextField nameField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton lookupButton;
    private JTextField fileField;
    private JButton loadButton;
    private JButton saveButton;
    
    private JTextField statusField;
    private JButton changeStatus;
    private JTextField pictureField;
    private JButton changePicture;
    private JTextField addField;
    private JButton addFriend;
    
    private FacePamphletDatabaseExtended facePamphletDatabase;
    private FacePamphletProfileExtended currentProfile;
    private FacePamphletCanvasExtended canvas;

}
