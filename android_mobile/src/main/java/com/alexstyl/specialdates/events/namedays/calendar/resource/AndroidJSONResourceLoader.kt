package com.alexstyl.specialdates.events.namedays.calendar.resource

import android.content.res.Resources
import com.alexstyl.specialdates.R

import com.alexstyl.specialdates.events.namedays.NamedayLocale

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

import org.json.JSONException
import org.json.JSONObject

class AndroidJSONResourceLoader(private val resources: Resources) : NamedayJSONResourceLoader {

    @Throws(JSONException::class)
    override fun loadJSON(locale: NamedayLocale): JSONObject {
        val inputStream = resources.openRawResource(locale.rawResId())
        val outputStream = ByteArrayOutputStream()

        var ctr: Int
        try {
            ctr = inputStream.read()
            while (ctr != -1) {
                outputStream.write(ctr)
                ctr = inputStream.read()
            }
            inputStream.close()
            return JSONObject(outputStream.toString("UTF-8"))
        } catch (e: IOException) {
            throw JSONException(e.message)
        } catch (e: JSONException) {
            throw JSONException(e.message)
        }

    }

}

private fun NamedayLocale.rawResId(): Int = when(this){
    NamedayLocale.GREEK -> R.raw.gr_namedays
    NamedayLocale.ROMANIAN -> R.raw.ro_namedays
    NamedayLocale.RUSSIAN -> R.raw.ru_namedays
    NamedayLocale.LATVIAN -> R.raw.lv_namedays
    NamedayLocale.LATVIAN_EXTENDED -> R.raw.lv_ext_namedays
    NamedayLocale.SLOVAK -> R.raw.sk_namedays
    NamedayLocale.ITALIAN -> R.raw.it_namedays
    NamedayLocale.CZECH -> R.raw.cs_namedays
    NamedayLocale.HUNGARIAN -> R.raw.hu_namedays
}
