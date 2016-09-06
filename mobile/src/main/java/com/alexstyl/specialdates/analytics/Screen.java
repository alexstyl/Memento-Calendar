package com.alexstyl.specialdates.analytics;

public enum Screen {
    HOME("home"),
    ADD_BIRTHDAY("add birthday"),
    SEARCH("search"),
    SETTINGS("settings"),
    DATE_DETAILS("date details"),
    DONATE("donate"),
    ABOUT("about");

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
