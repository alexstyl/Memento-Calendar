package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NamedayJSONProvider(private val loader: NamedayJSONResourceLoader) {

    @Throws(JSONException::class)
    fun getNamedayJSONFor(locale: NamedayLocale): NamedayJSON {
        val json = loader.loadJSON(locale)
        val data = json.getJSONArray("data")
        val special = getSpecialOf(json)
        return NamedayJSON(locale, data, special)
    }

    @Throws(JSONException::class)
    private fun getSpecialOf(json: JSONObject): JSONArray {
        return if (json.has("special")) {
            json.getJSONArray("special")
        } else EMPTY
    }

    companion object {

        private val EMPTY = JSONArray()
    }

}
