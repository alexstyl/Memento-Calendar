package com.alexstyl.specialdates.analytics;

public enum Screen {
    HOME("home"),
    ADD_BIRTHDAY("add_birthday"),
    SEARCH("search"),
    SETTINGS("settings"),
    DATE_DETAILS("date_details"),
    DONATE("donate"),
    ABOUT("about"),
    CONTACT_PERMISSION_REQUESTED("contact_permission");

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
