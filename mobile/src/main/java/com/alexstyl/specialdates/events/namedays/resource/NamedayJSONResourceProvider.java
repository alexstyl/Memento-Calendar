package com.alexstyl.specialdates.events.namedays.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NamedayJSONResourceProvider {

    private final JSONResourceLoader loader;

    public NamedayJSONResourceProvider(JSONResourceLoader loader) {
        this.loader = loader;
    }

    public NamedayJSON getNamedayJSONFor(NamedayLocale locale) throws JSONException {
        try {
            JSONObject json = loader.getJSON(locale.getRawResId());
            JSONArray data = json.getJSONArray("data");
            JSONArray special = json.getJSONArray("special");
            return new NamedayJSON(data, special);
        } catch (JSONException ex) {
            throw ex;
        }
    }

}
