package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale

import org.json.JSONException
import org.json.JSONObject

internal interface NamedayJSONResourceLoader {
    @Throws(JSONException::class)
    fun loadJSON(locale: NamedayLocale): JSONObject
}
