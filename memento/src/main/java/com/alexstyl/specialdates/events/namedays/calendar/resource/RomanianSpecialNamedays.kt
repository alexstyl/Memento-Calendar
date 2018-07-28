package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.EasternNameday
import com.alexstyl.specialdates.events.namedays.calendar.EasternNamedaysExtractor


internal class RomanianSpecialNamedays private constructor(private val namedays: RomanianNamedays) : SpecialNamedays {

    override val allNames: List<String>
        get() = namedays.allNames

    override fun getNamedayOn(date: Date): NamesInADate {
        return namedays.getNamedaysFor(date)
    }

    override fun getNamedaysFor(name: String, year: Int): NameCelebrations {
        return namedays.getNamedaysFor(name, year)
    }

    companion object {

        fun from(namedayJSON: NamedayJSON, romanianEasterCalculator: RomanianEasterSpecialCalculator): SpecialNamedays {
            val extractor = EasternNamedaysExtractor(namedayJSON.special)
            val easternNamedays = extractor.parse()
            val names = namesOf(easternNamedays)
            val namedays = RomanianNamedays(romanianEasterCalculator, names)
            return RomanianSpecialNamedays(namedays)
        }

        private fun namesOf(easternNamedays: List<EasternNameday>): ArrayList<String> {
            val names = ArrayList<String>()
            for (easternNameday in easternNamedays) {
                names.addAll(easternNameday.namesCelebrating)
            }
            return names
        }
    }
}
