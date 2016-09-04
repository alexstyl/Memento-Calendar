package com.alexstyl.specialdates.analytics;

import android.os.Bundle;

public enum Screen {
    HOME("home"),
    ADD_BIRTHDAY("add birthday"),
    SEARCH("search"),
    SETTINGS("settings"),
    DATE_DETAILS("date details"),
    DONATE("donate");

    private final Bundle data;

    Screen(String screenName) {
        data = new Bundle(1);
        data.putString("screen", screenName);
    }

    public Bundle getData() {
        return data;
    }
}
