/*
 * File: FacePamphletDatabaseExtended.java
 * -------------------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 * 
 * Extensions include:
 * (1) Ability to read and load files of existing networks into the database.
 * (2) Ability to write and save files of established networks to a file to be
 *     accessed later.
 */

import java.util.*;
import java.io.*;
import acm.util.*;
import acm.graphics.*;

public class FacePamphletDatabaseExtended implements FacePamphletConstantsExtended {
	
	/* Constructor: FacePamphletDatabase() */
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the database.
	 */
	public FacePamphletDatabaseExtended() {
		facePamphletDatabase = new HashMap<String, FacePamphletProfileExtended>();
	}
	
	
	/* Method: addProfile(profile) */
	/** 
	 * This method adds the given profile to the database.  If the 
	 * name associated with the profile is the same as an existing 
	 * name in the database, the existing profile is replaced by 
	 * the new profile passed in.
	 * 
	 * @param FacePamphletProfileExtended profile, a profile to be added to the database
	 */
	public void addProfile(FacePamphletProfileExtended profile) {
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
	 * @return FacePamphletProfileExtended, the profile associated with a given name
	 */
	public FacePamphletProfileExtended getProfile(String name) {
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
	
	
	/* Method: loadFile(file) */
	/**
	 * Loads a saved file to the current program. Reads each
	 * line of the file and extracts the components of each
	 * saved profile of the existing network and adds them to
	 * the database.
	 * 
	 * Throws an exception for any IO errors.
	 * 
	 * @param String file, the name of the file to be loaded
	 */
	public void loadFile(String file) {
		try {
			facePamphletDatabase.clear();
			BufferedReader rd = new BufferedReader(new FileReader(file));
			int nProfile = Integer.parseInt(rd.readLine());
			
			for (int i = 0; i < nProfile; i++) {	
				String name = rd.readLine();
				FacePamphletProfileExtended profile = new FacePamphletProfileExtended(name);

				String image = rd.readLine();
				profile.setImageFileName(image);
				if (image.length() != 0) {
					profile.setImage(new GImage(image));
				}
				
				String status = rd.readLine();
				profile.setStatus(status);

				while (true) {
					String friend = rd.readLine();
					if (friend.length() == 0) break;
					profile.addFriend(friend);
				} 
				facePamphletDatabase.put(name, profile);
			}
			rd.close();
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	
	/* Method: saveFile(fileName) */
	/**
	 * Writes a new files that contains the data from the
	 * session of networking. Writes each profile in the network
	 * to the file, spacing each profile's components along the 
	 * following guidelines:
	 * Profile name
	 * Name of image file for profile (blank if no image)
	 * Status (blank if no status)
	 * Names of friends, listed one per line, if any
	 * [blank line denotes end of profile]
	 * 
	 * Throws an exception for any IO errors.
	 * 
	 * @param String fileName, the name of the file to be written
	 */
	public void saveFile(String fileName) {
		try {
			File file = new File(fileName);
			
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter wt = new BufferedWriter(fileWriter);
			
			String num = Integer.toString(facePamphletDatabase.size());			
			wt.write(num);
			wt.newLine();
			
			Iterator<String> it = facePamphletDatabase.keySet().iterator();
			while (it.hasNext()) {
				String name = it.next();
				FacePamphletProfileExtended profile = facePamphletDatabase.get(name);
				
				wt.write(name);
				wt.newLine();
				
				String imageFile = profile.getImageFileName();
				if (imageFile != null) {
					wt.write(profile.getImageFileName());
				}
				wt.newLine();
				System.out.print(profile.getImageFileName());
				
				wt.write(profile.getStatus());
				wt.newLine();
				
				Iterator<String> friends = profile.getFriends();
				while (friends.hasNext()) {
					wt.write(friends.next());
					wt.newLine();
				}
				wt.newLine();	
			}
			wt.close();
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}	
	}
	
	
	/* Private instance variables */
	private HashMap<String, FacePamphletProfileExtended> facePamphletDatabase;

}
