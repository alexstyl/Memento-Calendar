package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedaysList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class NamedayJSONParser {

    private NamedayJSONParser() {
        // hide this
    }

    static NamedayBundle getNamedaysFromJSONasSounds(NamedayJSON json) {
        return createBundleWith(json, new SoundNode());
    }

    static NamedayBundle getNamedaysFrom(NamedayJSON json) {
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
                Date theDate = getNamedaysFrom(dateString);

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

    private static Date getNamedaysFrom(String date) {
        int slashIndex = date.indexOf("/");
        if (slashIndex == -1) {
            throw new IllegalArgumentException("Unable to get Namedays From " + date);
        }
        int dayOfMonth = Integer.parseInt(date.substring(0, slashIndex));
        @MonthInt int month = Integer.parseInt(date.substring(slashIndex + 1));
        return Date.Companion.on(dayOfMonth, month);
    }
}
