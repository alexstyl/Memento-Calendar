package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import java.io.BufferedReader;
import java.io.File;
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
            throw new JSONException(e.getMessage());
        }

        return new JSONObject(namedayRaw);
    }

    private static String getPathTo(NamedayLocale locale) {
        String prefix = locale.getCountryCode();
        return String.format("android_mobile" + File.separator +
                                     "src" + File.separator +
                                     "main" + File.separator +
                                     "res" + File.separator +
                                     "raw" + File.separator +
                                     "%s_namedays.json", prefix);
    }

}
