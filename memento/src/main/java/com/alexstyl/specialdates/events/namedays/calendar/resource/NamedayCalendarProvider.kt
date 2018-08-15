package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.StaticNamedays
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator
import org.json.JSONException

// TODO marked open so that we can compile older Java code.
open class NamedayCalendarProvider(private val jsonProvider: NamedayJSONProvider,
                                   private val orthodoxEasterCalculator: OrthodoxEasterCalculator,
                                   private val romanianEasterCalculator: RomanianEasterSpecialCalculator) {

    open fun loadNamedayCalendarForLocale(locale: NamedayLocale, year: Int): NamedayCalendar {
        if (hasRequestedSameCalendar(locale, year)) {
            return cachedCalendar!!
        }

        val namedayJSON = loadNamedayJSONFor(locale)
        val namedays = createNamedaysFor(locale, namedayJSON)
        val specialNamedays = createStrategyForLocale(namedayJSON.locale, namedayJSON)

        val namedayCalendar = NamedayCalendar(locale, namedays, specialNamedays, year)

        cachedCalendar = namedayCalendar

        return namedayCalendar
    }


    private fun createStrategyForLocale(locale: NamedayLocale, namedayJSON: NamedayJSON) =
            when (locale) {
                NamedayLocale.GREEK -> GreekSpecialNamedays.from(namedayJSON, orthodoxEasterCalculator)
                NamedayLocale.ROMANIAN -> RomanianSpecialNamedays.from(namedayJSON, romanianEasterCalculator)
                else -> NoSpecialNamedays
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

    private fun createNamedaysFor(locale: NamedayLocale, namedayJSON: NamedayJSON): StaticNamedays {
        return if (locale == NamedayLocale.GREEK) {
            NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON)
        } else {
            NamedayJSONParser.getNamedaysFrom(namedayJSON)
        }
    }

    companion object {
        private var cachedCalendar: NamedayCalendar? = null
    }

}
