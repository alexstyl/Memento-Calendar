package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class NamedayJSONResourceProvider {

    private static final JSONArray EMPTY = new JSONArray();

    private final NamedayJSONResourceLoader loader;

    NamedayJSONResourceProvider(NamedayJSONResourceLoader loader) {
        this.loader = loader;
    }

    NamedayJSON getNamedayJSONFor(NamedayLocale locale) throws JSONException {
        JSONObject json = loader.loadJSON(locale);
        JSONArray data = json.getJSONArray("data");
        JSONArray special = getSpecialOf(json);
        return new NamedayJSON(data, special);
    }

    private JSONArray getSpecialOf(JSONObject json) throws JSONException {
        if (json.has("special")) {
            return json.getJSONArray("special");
        }
        return EMPTY;
    }

}
