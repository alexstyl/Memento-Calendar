package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator

class SpecialNamedaysHandlerFactory(private val orthodoxEasterCalculator: OrthodoxEasterCalculator,
                                    private val romanianEasterCalculator: RomanianEasterSpecialCalculator) {

    internal fun createStrategyForLocale(locale: NamedayLocale, namedayJSON: NamedayJSON): SpecialNamedays {
        if (isGreekLocale(locale)) {
            return GreekSpecialNamedays.from(namedayJSON, orthodoxEasterCalculator)
        } else if (isRomanian(locale)) {
            return RomanianSpecialNamedays.from(namedayJSON, romanianEasterCalculator)
        }
        return NoSpecialNamedays()
    }


    private fun isRomanian(locale: NamedayLocale): Boolean {
        return locale == NamedayLocale.ROMANIAN
    }

    private fun isGreekLocale(locale: NamedayLocale): Boolean {
        return locale == NamedayLocale.GREEK
    }

}
