package com.alexstyl.specialdates.analytics;

public enum Action {
    ADD_BIRTHDAY("add_bday"),
    DAILY_REMINDER("reminder"),
    DONATION("donate"),
    INTERACT_CONTACT("contact"),
    SELECT_THEME("theme"),
    SELECT_DATE("select_date"),
    COMPLICATION("complication: contacts events");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
