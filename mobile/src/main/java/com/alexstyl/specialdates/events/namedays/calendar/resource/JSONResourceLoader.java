package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONException;
import org.json.JSONObject;

interface JSONResourceLoader {
    JSONObject loadJSON(NamedayLocale locale) throws JSONException;
}
