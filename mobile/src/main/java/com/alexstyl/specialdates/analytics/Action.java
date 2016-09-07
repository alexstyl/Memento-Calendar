package com.alexstyl.specialdates.analytics;

public enum Action {
    ADD_BIRTHDAY("add_birthday"),
    DAILY_REMINDER("daily_reminder"),
    DONATION("donate"),
    INTERACT_CONTACT("interact_contact"),
    SELECT_THEME("select_theme"),
    GO_TO_TODAY("go_to_today");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
