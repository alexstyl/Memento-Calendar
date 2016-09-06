package com.alexstyl.specialdates.analytics;

public enum Action {
    ADD_BIRTHDAY("add birthday"),
    DAILY_REMINDER("enable daily reminder"),
    DONATION("donate"),
    INTERACT_CONTACT("interact contact"),
    SELECT_THEME("select theme"),
    GO_TO_TODAY("go to today");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
