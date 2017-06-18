package com.alexstyl.specialdates.facebook;

public class UserCredentials {

    public static final UserCredentials ANNONYMOUS = new UserCredentials(-1, "", "");

    private final long userID;
    private final String key;
    private final String name;

    public UserCredentials(long userID, String key, String name) {
        this.userID = userID;
        this.key = key;
        this.name = name;
    }

    public long getUid() {
        return userID;

    }

    public String getKey() {
        return key;

    }

    public String getName() {
        return name;
    }
}
