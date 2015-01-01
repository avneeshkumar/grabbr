package com.grabbr.grabbrapp.daos;


/**
 * Created by arun on 13/12/14.
 */
public class FollowersData {

    String UserID;
    String Name;
    String isFollowed;
    String Email;

    public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String isFollowed() {
        return isFollowed;
    }

    public void setFollowed(String isFollowed) {
        this.isFollowed = isFollowed;
    }
    
}
