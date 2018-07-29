package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayBundle
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar

import org.json.JSONException

class NamedayCalendarProvider(private val jsonProvider: NamedayJSONProvider, private val factory: SpecialNamedaysHandlerFactory) {

    fun loadNamedayCalendarForLocale(locale: NamedayLocale, year: Int): NamedayCalendar {
        if (hasRequestedSameCalendar(locale, year)) {
            return cachedCalendar!!
        }
        val namedayJSON = loadNamedayJSONFor(locale)
        val specialCaseHandler = getSpecialnamedaysHandler(namedayJSON)
        val namedaysBundle = getNamedayBundle(locale, namedayJSON)
        val namedayCalendar = NamedayCalendar(locale, namedaysBundle, specialCaseHandler, year)

        cachedCalendar = namedayCalendar

        return namedayCalendar
    }

    private fun hasRequestedSameCalendar(locale: NamedayLocale, year: Int): Boolean {
        return cachedCalendar != null
                && cachedCalendar!!.year == year
                && cachedCalendar!!.locale == locale
    }

    private fun loadNamedayJSONFor(locale: NamedayLocale): NamedayJSON {
        try {
            return jsonProvider.getNamedayJSONFor(locale)
        } catch (e: JSONException) {
            throw IllegalStateException("Could not load nameday JSON for $locale")
        }

    }

    private fun getSpecialnamedaysHandler(namedayJSON: NamedayJSON): SpecialNamedays {
        return factory.createStrategyForLocale(namedayJSON.locale, namedayJSON)
    }

    private fun getNamedayBundle(locale: NamedayLocale, namedayJSON: NamedayJSON): NamedayBundle {
        return if (locale.isComparedBySound) {
            NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON)
        } else {
            NamedayJSONParser.getNamedaysFrom(namedayJSON)
        }
    }

    companion object {
        private var cachedCalendar: NamedayCalendar? = null
    }

}
