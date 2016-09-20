package com.alexstyl.specialdates.events.namedays.resource;

import org.json.JSONArray;

public class NamedayJSON {
    private final JSONArray data;
    private final JSONArray special;

    public NamedayJSON(JSONArray data, JSONArray special) {
        this.data = data;
        this.special = special;
    }

    public JSONArray getData() {
        return data;
    }

    public JSONArray getSpecial() {
        return special;
    }
}
