package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TestJSONResourceLoader : NamedayJSONResourceLoader {

    @Throws(JSONException::class)
    override fun loadJSON(locale: NamedayLocale): JSONObject {
        var stream: InputStream? = null
        try {
            stream = CLASS_LOADER.getResourceAsStream("gr_namedays.json")

            val bufferedReader = BufferedReader(InputStreamReader(stream!!))
            return JSONObject(bufferedReader.readLine())
        } catch (e: IOException) {
            throw JSONException(e.message)
        } finally {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private val CLASS_LOADER = Thread.currentThread().contextClassLoader

}
