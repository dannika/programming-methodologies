/*
 * File: FacePamphletDatabase.java
 * -------------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */

import java.util.*;

public class FacePamphletDatabase implements FacePamphletConstants {
	
	/* Constructor: FacePamphletDatabase() */
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the database.
	 */
	public FacePamphletDatabase() {
		facePamphletDatabase = new HashMap<String, FacePamphletProfile>();
	}
	
	
	/* Method: addProfile(profile) */
	/** 
	 * This method adds the given profile to the database.  If the 
	 * name associated with the profile is the same as an existing 
	 * name in the database, the existing profile is replaced by 
	 * the new profile passed in.
	 * 
	 * @param FacePamphletProfile profile, a profile to be added to the database
	 */
	public void addProfile(FacePamphletProfile profile) {
		if (facePamphletDatabase.containsKey(profile.getName())) {
			facePamphletDatabase.remove(profile.getName());
			facePamphletDatabase.put(profile.getName(), profile);
		} else {
			facePamphletDatabase.put(profile.getName(), profile);
		}
	}

	
	/* Method: getProfile(name) */
	/** 
	 * This method returns the profile associated with the given name 
	 * in the database.  If there is no profile in the database with 
	 * the given name, the method returns null.
	 * 
	 * @param String name, the name that identifies a profile
	 * @return FacePamphletProfile, the profile associated with a given name
	 */
	public FacePamphletProfile getProfile(String name) {
		if (containsProfile(name)) {
			return facePamphletDatabase.get(name);
		}
		return null;
	}
	
	
	/* Method: deleteProfile(name) */
	/** 
	 * This method removes the profile associated with the given name
	 * from the database.  It also updates the list of friends of all
	 * other profiles in the database to make sure that this name is
	 * removed from the list of friends of any other profile.
	 * 
	 * If there is no profile in the database with the given name, then
	 * the database is unchanged after calling this method.
	 * 
	 * @param String name, the unique identifier of a profile to be deleted
	 */
	public void deleteProfile(String name) {
		if (containsProfile(name)) {
			facePamphletDatabase.remove(name);
			Iterator<String> it = facePamphletDatabase.keySet().iterator();
			while(it.hasNext()){
				facePamphletDatabase.get(it.next()).removeFriend(name);
			}
		}
	}

	
	/* Method: containsProfile(name) */
	/** 
	 * This method returns true if there is a profile in the database 
	 * that has the given name.  It returns false otherwise.
	 * 
	 * @param String name, the unique identifier of a profile in the database
	 * @return boolean, whether a profile with a given name exists in the database
	 */
	public boolean containsProfile(String name) { //where to use this
		if (facePamphletDatabase.containsKey(name)) {
			return true;
		}
		return false;
	}
	
	
	/* Private instance variables */
	private HashMap<String, FacePamphletProfile> facePamphletDatabase;

}
