package com.alexstyl.specialdates.events.namedays.calendar.resource;

import org.json.JSONArray;

class NamedayJSON {
    private final JSONArray data;
    private final JSONArray special;

    NamedayJSON(JSONArray data, JSONArray special) {
        this.data = data;
        this.special = special;
    }

    JSONArray getData() {
        return data;
    }

    JSONArray getSpecial() {
        return special;
    }
}
