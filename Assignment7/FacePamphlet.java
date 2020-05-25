/* 
 * File: FacePamphlet.java
 * -----------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This program will implement a basic social network management system. This application
 * allows for user profiles to be added to, deleted from, of looked-up in the social
 * network. Moreover, for each profile, the user can keep track the profile name, an 
 * optional image that they may want to display in the profile, an optional current status,
 * and each person's list of friends.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

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
		facePamphletDatabase = new FacePamphletDatabase();
		canvas = new FacePamphletCanvas();
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
		
		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		add(addButton, NORTH);
		add(deleteButton, NORTH);
		add(lookupButton, NORTH);
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
    	}
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
			FacePamphletProfile existing = facePamphletDatabase.getProfile(text);
			currentProfile = existing;
			canvas.displayProfile(currentProfile);
			canvas.showMessage("A profile with the name " + text + " already exists");
		} else {
			FacePamphletProfile newProfile = new FacePamphletProfile(text);
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
				currentProfile.setImage(profPic);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Picture updated");
			} catch(ErrorException ex) {
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
					FacePamphletProfile addedFriend = facePamphletDatabase.getProfile(friend);
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
    
    
    /* Private instance variables */
    private JTextField nameField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton lookupButton;
    
    private JTextField statusField;
    private JButton changeStatus;
    private JTextField pictureField;
    private JButton changePicture;
    private JTextField addField;
    private JButton addFriend;
    
    private FacePamphletDatabase facePamphletDatabase;
    private FacePamphletProfile currentProfile;
    private FacePamphletCanvas canvas;

}
