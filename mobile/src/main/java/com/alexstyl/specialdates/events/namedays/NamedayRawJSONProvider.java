package com.alexstyl.specialdates.events.namedays;

import android.content.Context;

import com.alexstyl.specialdates.util.Utils;
import com.novoda.notils.exception.DeveloperError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NamedayRawJSONProvider {

    private static final String DATA = "data";
    private static final String SPECIAL = "special";

    private final JSONObject rawNameday;

    public static NamedayRawJSONProvider newInstance(Context context, NamedayLocale locale) {
        JSONObject rawNameday = getRawNameday(context, locale);
        return new NamedayRawJSONProvider(rawNameday);
    }

    NamedayRawJSONProvider(JSONObject rawNameday) {
        this.rawNameday = rawNameday;
    }

    public JSONArray getNamedays() {
        try {
            return rawNameday.getJSONArray(DATA);
        } catch (JSONException e) {
            throw new DeveloperError("Unable to find " + DATA + " in JSON");
        }
    }

    public JSONArray getSpecialNamedays() {
        try {
            return rawNameday.getJSONArray(SPECIAL);
        } catch (JSONException ex) {
            throw new DeveloperError(String.format("Errow while getting [%s] from json :", SPECIAL), ex);
        }
    }

    private static JSONObject getRawNameday(Context context, NamedayLocale locale) {
        int jsonID = locale.getRawResId();
        return Utils.getJSON(context, jsonID);
    }

}
