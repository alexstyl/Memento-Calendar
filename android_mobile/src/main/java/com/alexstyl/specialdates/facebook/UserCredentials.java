package com.alexstyl.specialdates.facebook;

class UserCredentials {

    static final UserCredentials ANNONYMOUS = new UserCredentials(-1, "", "");

    private final long userID;
    private final String key;
    private final String name;

    UserCredentials(long userID, String key, String name) {
        this.userID = userID;
        this.key = key;
        this.name = name;
    }

    long getUid() {
        return userID;

    }

    String getKey() {
        return key;

    }

    public String getName() {
        return name;
    }
}
