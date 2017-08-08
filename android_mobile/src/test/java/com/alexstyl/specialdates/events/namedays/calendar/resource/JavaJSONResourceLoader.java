package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.novoda.notils.exception.DeveloperError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

class JavaJSONResourceLoader implements NamedayJSONResourceLoader {

    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    @Override
    public JSONObject loadJSON(NamedayLocale locale) throws JSONException {
        String namedayRaw;

        try {
            InputStream stream = CLASS_LOADER.getResourceAsStream("namedays/" + fileNameOf(locale));
            if (stream == null) {
                throw new DeveloperError("Couldn't find " + locale);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            namedayRaw = bufferedReader.readLine();
        } catch (IOException e) {
            throw new JSONException(e.getMessage());
        }

        return new JSONObject(namedayRaw);
    }

    private static String fileNameOf(NamedayLocale locale) {
        return String.format(Locale.US, "%s_namedays.json", locale.getCountryCode());
    }

}
