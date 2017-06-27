package com.alexstyl.specialdates.analytics;

public enum Screen {
    HOME("upcoming"),
    ADD_EVENT("add event"),
    SEARCH("search"),
    SETTINGS("settings"),
    DATE_DETAILS("date details"),
    DONATE("donate"),
    CONTACT_PERMISSION_REQUESTED("contact permission"),
    WEAR_CONTACT_EVENTS("wear: contacts events"),
    PLAY_STORE("playstore"),
    GOOGLE_PLUS_COMMUNITY("google plus community"),
    EMAIL_SUPPORT("email support"),
    FACEBOOK_PROFILE("facebook_profile");

    private final String screenName;

    Screen(String screenName) {
        this.screenName = screenName;
    }

    public String screenName() {
        return screenName;
    }

    @Override
    public String toString() {
        return screenName;
    }
}
