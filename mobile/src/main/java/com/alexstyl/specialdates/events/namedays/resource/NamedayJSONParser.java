package com.alexstyl.specialdates.events.namedays.resource;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedaysList;
import com.alexstyl.specialdates.events.namedays.calendar.CharacterNode;
import com.alexstyl.specialdates.events.namedays.calendar.Node;
import com.alexstyl.specialdates.events.namedays.calendar.SoundNode;
import com.novoda.notils.exception.DeveloperError;

import org.joda.time.IllegalFieldValueException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NamedayJSONParser {

    private NamedayJSONParser() {
        // hide this
    }

    public static NamedayBundle createAsSounds(NamedayJSON json) {
        return createBundleWith(json, new SoundNode());
    }

    public static NamedayBundle create(NamedayJSON json) {
        return createBundleWith(json, new CharacterNode());
    }

    private static NamedayBundle createBundleWith(NamedayJSON locale, Node namesToDate) {
        NamedaysList dateToNames = new NamedaysList();

        JSONArray data = locale.getData();
        int size = data.length();
        for (int i = 0; i < size; i++) {
            try {
                JSONObject nameday;

                nameday = (JSONObject) data.get(i);
                String dateString = nameday.getString("date");
                DayDate theDate;
                try {
                    theDate = parse(dateString);
                } catch (IllegalFieldValueException ex) {
                    ex.printStackTrace();
                    continue;
                }

                JSONArray variations = nameday.getJSONArray("names");
                int numberOfVariations = variations.length();
                for (int varCount = 0; varCount < numberOfVariations; varCount++) {
                    String variation = variations.getString(varCount);

                    namesToDate.addDate(variation, theDate);
                    dateToNames.addNameday(theDate, variation);

                }
            } catch (JSONException e) {
                ErrorTracker.track(e);
            }
        }

        return new NamedayBundle(namesToDate, dateToNames);
    }

    private static DayDate parse(String date) {
        int slashIndex = date.indexOf("/");
        if (slashIndex == -1) {
            throw new DeveloperError("Unable to parse " + date);
        }
        int dayOfMonth = Integer.valueOf(date.substring(0, slashIndex));
        int month = Integer.valueOf(date.substring(slashIndex + 1));

        return DayDate.newInstance(dayOfMonth, month);
    }
}
