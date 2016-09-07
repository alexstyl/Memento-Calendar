package com.alexstyl.specialdates.analytics;

public enum Action {
    ADD_BIRTHDAY("add_bday"),
    DAILY_REMINDER("reminder"),
    DONATION("donate"),
    INTERACT_CONTACT("contact"),
    SELECT_THEME("theme"),
    GO_TO_TODAY("gotoday");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
