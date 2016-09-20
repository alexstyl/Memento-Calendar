package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

class JavaJSONResourceLoader implements NamedayJSONResourceLoader {

    @Override
    public JSONObject loadJSON(NamedayLocale locale) throws JSONException {

        String namedayRaw;
        try {
            BufferedReader br = new BufferedReader(new FileReader(getPathTo(locale)));
            namedayRaw = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new JSONException(e.getMessage());
        }

        return new JSONObject(namedayRaw);
    }

    private String getPathTo(NamedayLocale locale) {
        String prefix = getPrefixOf(locale);
        return String.format("src/main/res/raw/%s_namedays.json", prefix);
    }

    private String getPrefixOf(NamedayLocale locale) {
        switch (locale) {
            case gr:
                return "gr";
            case cs:
                return "cs";
            case lv:
                return "lv";
            case ro:
                return "ro";
            case ru:
                return "ru";
            case sk:
                return "sk";
            default:
                throw new IllegalArgumentException("Cannot match " + locale + " to file");

        }
    }

}
