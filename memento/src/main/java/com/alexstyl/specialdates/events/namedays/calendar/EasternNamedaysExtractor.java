package com.alexstyl.specialdates.events.namedays.calendar;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EasternNamedaysExtractor {

    private final JSONArray specialJSON;

    public EasternNamedaysExtractor(JSONArray specialJSON) {
        this.specialJSON = specialJSON;
    }

    public List<EasternNameday> parse() {
        return extractNamedaysAsDaysFromEaster(specialJSON);
    }

    private List<EasternNameday> extractNamedaysAsDaysFromEaster(JSONArray specialJSON) {
        List<EasternNameday> easternNamedays = new ArrayList<>();
        int size = specialJSON.length();
        for (int i = 0; i < size; i++) {
            try {
                JSONObject nameday = (JSONObject) specialJSON.get(i);
                JSONArray variations = nameday.getJSONArray("variations");
                if (containsNoVariations(variations)) {
                    continue;
                }

                int date = nameday.getInt("toEaster");
                List<String> celebratingNames = getVariationsFrom(variations);
                EasternNameday easternDate = new EasternNameday(date, celebratingNames);
                easternNamedays.add(easternDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return easternNamedays;
    }

    @NonNull
    private ArrayList<String> getVariationsFrom(JSONArray variations) throws JSONException {
        ArrayList<String> celebratingNames = new ArrayList<>();
        for (int j = 0; j < variations.length(); j++) {
            String variation = variations.getString(j);
            celebratingNames.add(variation);
        }
        return celebratingNames;
    }

    private boolean containsNoVariations(JSONArray variations) {
        return variations.length() == 0;
    }

}
