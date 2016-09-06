package com.alexstyl.specialdates.analytics;

public enum Action {
    ADD_BIRTHDAY("add birthday"),
    DAILY_REMINDER("enable daily reminder"),
    DONATION("donate"),
    INTERACT_CONTACT("interact contact");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
