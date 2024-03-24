package com.amzventures.zeeshopping.Utility;

public class Users {
    String userId ,username,profile;

    public Users(String userId, String name, String profile) {
        this.userId = userId;
        this.username = name;
        this.profile = profile;
    }

    public Users() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
