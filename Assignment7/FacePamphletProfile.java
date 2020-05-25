/*
 * File: FacePamphletProfile.java
 * ------------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This class keeps track of all the information for one profile
 * in the FacePamphlet social network.  Each profile contains a
 * name, an image (which may not always be set), a status (what 
 * the person is currently doing, which may not always be set),
 * and a list of friends.
 */

import acm.graphics.*;
import java.util.*;

public class FacePamphletProfile implements FacePamphletConstants {
	
	/* Constructor: FacePamphletProfile(name) */
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for
	 * the profile.
	 * 
	 * @param String name, a unique identifier for the profile
	 */
	public FacePamphletProfile(String name) {
		profileName = name;
		currentStatus = "";
		friendsList = new ArrayList<String>();
		profilePicture = null;
	}

	
	/* Method: getName() */
	/** 
	 * This method returns the name associated with the profile. 
	 * 
	 * @return String profileName, the name of the profile
	 */ 
	public String getName() {
		return profileName;
	}

	
	/* Method: getImage() */
	/** 
	 * This method returns the image associated with the profile.  
	 * If there is no image associated with the profile, the method
	 * returns null. 
	 * 
	 * @return GImage profilePicture, the image associated with the profile
	 */ 
	public GImage getImage() {
		return profilePicture;
	}

	
	/* Method: setImage(image) */
	/** 
	 * This method sets the image associated with the profile. 
	 * 
	 * @param GImage image, the image the user wishes to associated with the profile
	 */ 
	public void setImage(GImage image) {
		profilePicture = image;
	}
	
	
	/* Method: getStatus() */
	/** 
	 * This method returns the status associated with the profile.
	 * If there is no status associated with the profile, the method
	 * returns the empty string ("").
	 * 
	 * @return String currentStatus, the current status of the profile
	 */ 
	public String getStatus() {
		return currentStatus;
	}
	
	
	/* Method: setStatus(status) */
	/** 
	 * This method sets the status associated with the profile. 
	 * 
	 * @param String status, the status the user wishes to set for the profile
	 */ 
	public void setStatus(String status) {
		currentStatus = status;
	}

	
	/* Method: addFriend(friend) */
	/** 
	 * This method adds the named friend to this profile's list of 
	 * friends.  It returns true if the friend's name was not already
	 * in the list of friends for this profile (and the name is added 
	 * to the list).  The method returns false if the given friend name
	 * was already in the list of friends for this profile (in which 
	 * case, the given friend name is not added to the list of friends 
	 * a second time.)
	 * 
	 * @param String friend, the friend to be added to one's network
	 * @return boolean, whether the friend was already in the profile's list of friends
	 */
	public boolean addFriend(String friend) {
		if (!friendsList.contains(friend)) {
			friendsList.add(friend);
			return true;
		}
		return false;
	}

	
	/* Method: removeFriend(friend) */
	/** 
	 * This method removes the named friend from this profile's list
	 * of friends.  It returns true if the friend's name was in the 
	 * list of friends for this profile (and the name was removed from
	 * the list).  The method returns false if the given friend name 
	 * was not in the list of friends for this profile (in which case,
	 * the given friend name could not be removed.)
	 * @param, String friend, the friend to be removed from the profile's network
	 * @return boolean, whether the friend was in the profile's list of friends
	 */
	public boolean removeFriend(String friend) {
		if (friendsList.contains(friend)) {
			friendsList.remove(friend);
			return true;
		}
		return false;
	}
	
	
	/* Iterator: getFriends() */
	/** 
	 * This method returns an iterator over the list of friends 
	 * associated with the profile.
	 * 
	 * @return Iterator<String>, an String iterator over the profile's list of friends
	 */ 
	public Iterator<String> getFriends() {
		return friendsList.iterator();
	}
	
	
	/* Method: toString() */
	/** 
	 * This method returns a string representation of the profile.  
	 * This string is of the form: "name (status): list of friends", 
	 * where name and status are set accordingly and the list of 
	 * friends is a comma separated list of the names of all of the 
	 * friends in this profile.
	 * 
	 * For example, in a profile with name "Alice" whose status is 
	 * "coding" and who has friends Don, Chelsea, and Bob, this method 
	 * would return the string: "Alice (coding): Don, Chelsea, Bob"
	 * 
	 * @return String result, a String representation of the profile
	 */ 
	public String toString() {
		String result = profileName + " (" + currentStatus + ") :";
		Iterator<String> it = getFriends();
		while (it.hasNext()) {
			result += " " + it.next() + ",";
		}
		return result.substring(0, result.length() - 2);
	}
	
	
	/* Private instance variables */
	private String profileName;
	private String currentStatus;
	private ArrayList<String> friendsList;
	private GImage profilePicture;
	
}
