package com.alexstyl.specialdates.events.namedays;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.ErrorTracker;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    Log.w("Skipped [%s] because it had no variations", nameday.toString());
                    continue;
                }

                int date = nameday.getInt("toEaster");
                ArrayList<String> celebratingNames = getVariationsFrom(variations);
                EasternNameday easternDate = new EasternNameday(date, celebratingNames);
                easternNamedays.add(easternDate);
            } catch (JSONException e) {
                ErrorTracker.track(e);
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
