package com.alexstyl.specialdates.events.namedays.calendar.resource;

import android.content.res.Resources;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidJSONResourceLoader implements JSONResourceLoader {

    private final Resources resources;

    public AndroidJSONResourceLoader(Resources resources) {
        this.resources = resources;
    }

    @Override
    public JSONObject loadJSON(NamedayLocale locale) throws JSONException {
        InputStream inputStream = resources.openRawResource(locale.getRawResId());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                outputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
            return new JSONObject(outputStream.toString());
        } catch (IOException | JSONException e) {
            throw new JSONException(e.getMessage());
        }
    }

}
