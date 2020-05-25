/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/* Constructor: FacePamphletCanvas() */
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		//nothing to initialize
	}

	
	/* Method: showMessage(msg) */
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 * 
	 * @param String msg, the message to be dispalyed as a label
	 */
	public void showMessage(String msg) {
		GLabel message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		double x = (getWidth() - message.getWidth()) / 2;
		double y = (getHeight() - BOTTOM_MESSAGE_MARGIN);
		message.setLocation(x, y);
		add(message);
	}
	
	
	/* Method: displayProfile(profile) */
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 * 
	 * @param FacePamphletProfile profile, the profile to be displayed
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		
		String profileName = profile.getName();
		GImage profPic = profile.getImage();
		String status = profile.getStatus();
		Iterator<String> friends = profile.getFriends();
		
		displayName(profileName);
		displayPicture(profPic);
		displayStatus(profileName, status);
		displayFriends(friends);
	}
	
	
	/* Method: displayName(profileName) */
	/**
	 * Displays the name of the profile.
	 * 
	 * @param String profileName, the profile's unique identifier
	 */
	private void displayName(String profileName) {
		GLabel nameLabel = new GLabel(profileName);
		nameLabelHeight = nameLabel.getHeight();
		nameLabel.setFont(PROFILE_NAME_FONT);
		nameLabel.setColor(Color.BLUE);
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + nameLabel.getAscent();
		add(nameLabel, x, y);
	}
	
	
	/* Method: displayPicture(profPic) */
	/**
	 * Displays the current profile's picture, if it exists.
	 * Otherwise, it displays an empty square with the label
	 * "No Image."
	 * 
	 * @param profPic, the current profile's picture
	 */
	private void displayPicture(GImage profPic) {
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + nameLabelHeight + IMAGE_MARGIN;
		
		if (profPic == null) {
			GRect rect = new GRect(x, y, IMAGE_WIDTH, IMAGE_WIDTH);
			GLabel noImage = new GLabel("No Image");
			noImage.setFont(PROFILE_IMAGE_FONT);
			double labelX = x + (IMAGE_WIDTH / 2) - (noImage.getWidth() / 2);
			double labelY = y + (IMAGE_HEIGHT / 2) + (noImage.getAscent() / 2);
			add(rect);
			add(noImage, labelX, labelY);
		} else {
			GImage profilePicture = profPic;
			profilePicture.setBounds(x, y, IMAGE_WIDTH, IMAGE_WIDTH);
			add(profilePicture);
		}
	}
	
	
	/* Method: displayStatus(name, status) */
	/**
	 * Displays the status of the current profile, if it
	 * exists. Otherwise, it displays the label "No current
	 * status."
	 * 
	 * @param String name, the name of the current profile
	 * @param String status, the profile's current status
	 */
	private void displayStatus(String name, String status) {
		GLabel userStatus;
		if (status.equals("")) {
			userStatus = new GLabel("No current status");
		} else {
			userStatus = new GLabel(name + " is " + status);
		}
		userStatus.setFont(PROFILE_STATUS_FONT);
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + nameLabelHeight + IMAGE_MARGIN + IMAGE_HEIGHT +
				STATUS_MARGIN + userStatus.getAscent();
		add(userStatus, x, y);
	}
	
	
	/* Method: displayFriends(friends) */
	/**
	 * Displays the current profile's list of friends, if any exist.
	 * If so, the program iterates through the list, displaying
	 * each friend on a new line underneath the header. Otherwise,
	 * it simply displays the header "Friends:".
	 * 
	 * @param Iterator<String> friends, an iteration through
	 * the current profile's list of friends
	 */
	private void displayFriends(Iterator<String> friends) {
		GLabel friendsLabel = new GLabel("Friends:");
		friendsLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		double x = getWidth() / 2;
		double y = TOP_MARGIN + nameLabelHeight + IMAGE_MARGIN;
		add(friendsLabel, x, y);
		
		Iterator<String> it = friends;
		for(int i = 1; it.hasNext(); i++) {
			GLabel friendName = new GLabel(it.next());
			friendName.setFont(PROFILE_FRIEND_FONT);
			double height = y + friendsLabel.getHeight() * i;
			add(friendName, x, height);
		}
	}
	
	
	/* Method: clearDisplay() */
	/** Removes all objects from the canvas. */
	public void clearDisplay() {
		removeAll();
	}
	
	
	/* Private instance variables */
	private double nameLabelHeight = 0;
	
}
