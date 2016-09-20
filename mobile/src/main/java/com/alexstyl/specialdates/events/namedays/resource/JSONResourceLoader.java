package com.alexstyl.specialdates.events.namedays.resource;

import android.support.annotation.RawRes;

import org.json.JSONException;
import org.json.JSONObject;

interface JSONResourceLoader {
    JSONObject getJSON(@RawRes int jsonRawId) throws JSONException;
}
