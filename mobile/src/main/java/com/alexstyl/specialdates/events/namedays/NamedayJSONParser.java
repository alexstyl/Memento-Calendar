package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.DayDate;
import com.novoda.notils.exception.DeveloperError;

import org.joda.time.IllegalFieldValueException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NamedayJSONParser {

    private static final String DATE = "date";
    private static final String NAMES = "names";
    private final NamedayLocale locale;

    public NamedayJSONParser(NamedayLocale locale) {
        this.locale = locale;
    }

    NamedayBundle parseAsNamedays(JSONArray data) {
        Node namesToDate = getNode(locale);
        NamedaysList dateToNames = new NamedaysList();

        int size = data.length();
        for (int i = 0; i < size; i++) {
            try {
                JSONObject nameday;

                nameday = (JSONObject) data.get(i);
                String dateString = nameday.getString(DATE);
                DayDate theDate;
                try {
                    theDate = parse(dateString);
                } catch (IllegalFieldValueException ex) {
                    ex.printStackTrace();
                    continue;
                }

                JSONArray variations = nameday.getJSONArray(NAMES);
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

    private Node getNode(NamedayLocale locale) {
        if (locale.isComparedBySounds()) {
            return new SoundNode();
        }
        return new CharacterNode();
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
