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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserCredentials that = (UserCredentials) o;

        if (userID != that.userID) {
            return false;
        }
        if (!key.equals(that.key)) {
            return false;
        }
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (userID ^ (userID >>> 32));
        result = 31 * result + key.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
