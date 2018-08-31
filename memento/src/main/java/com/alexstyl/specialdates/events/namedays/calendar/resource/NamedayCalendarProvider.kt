package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.Namedays
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator
import org.json.JSONException

// TODO marked open so that we can compile older Java code.
open class NamedayCalendarProvider(private val jsonProvider: NamedayJSONProvider,
                                   private val orthodoxEasterCalculator: OrthodoxEasterCalculator,
                                   private val romanianEasterCalculator: RomanianEasterSpecialCalculator) {

    open fun loadNamedayCalendarForLocale(locale: NamedayLocale, year: Int): NamedayCalendar {
        val key = Pair(year, locale)

        if (!calendars.containsKey(key)) {
            val namedayJSON = loadNamedayJSONFor(locale)
            val namedays = createNamedaysFor(locale, namedayJSON)
            val specialNamedays = createStrategyForLocale(namedayJSON.locale, namedayJSON)

            calendars[key] = NamedayCalendar(locale, namedays, specialNamedays, year)
        }

        return calendars[key]!!
    }

    private fun createStrategyForLocale(locale: NamedayLocale, namedayJSON: NamedayJSON) =
            when (locale) {
                NamedayLocale.GREEK -> GreekSpecialNamedays.from(namedayJSON, orthodoxEasterCalculator)
                NamedayLocale.ROMANIAN -> RomanianSpecialNamedays.from(namedayJSON, romanianEasterCalculator)
                else -> NoSpecialNamedays
            }

    private fun loadNamedayJSONFor(locale: NamedayLocale): NamedayJSON {
        try {
            return jsonProvider.getNamedayJSONFor(locale)
        } catch (e: JSONException) {
            throw IllegalStateException("Could not load nameday JSON for $locale")
        }
    }

    private fun createNamedaysFor(locale: NamedayLocale, namedayJSON: NamedayJSON): Namedays {
        return if (locale == NamedayLocale.GREEK) {
            NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON)
        } else {
            NamedayJSONParser.getNamedaysFrom(namedayJSON)
        }
    }

    companion object {
        private val calendars = mutableMapOf<Pair<Int, NamedayLocale>, NamedayCalendar>()
    }

}
