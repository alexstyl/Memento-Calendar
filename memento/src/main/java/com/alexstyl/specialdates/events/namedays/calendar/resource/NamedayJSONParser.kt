package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.MonthInt
import com.alexstyl.specialdates.events.namedays.MapNamedaysList
import com.alexstyl.specialdates.events.namedays.Namedays
import org.json.JSONObject

object NamedayJSONParser {

    fun getNamedaysFromJSONasSounds(json: NamedayJSON): Namedays {
        return createNamedaysFor(json, SoundNode())
    }

    fun getNamedaysFrom(json: NamedayJSON): Namedays {
        return createNamedaysFor(json, CharacterNode())
    }

    private fun createNamedaysFor(json: NamedayJSON, namesToDate: Node): Namedays {
        val dateToNames = MapNamedaysList()
        for (i in 1 until json.namedaysJSON.length()) {
            val nameday = json.namedaysJSON[i] as JSONObject

            val date = nameday.getString("date").toDate()
            val names = nameday.getJSONArray("names")

            for (j in 0 until names.length()) {
                val name = names.getString(j)
                namesToDate.addDate(name, date)
                dateToNames.addRecurringNameday(date, name)
            }
        }
        return Namedays(namesToDate, dateToNames) // TODO make MapNamedaysList Immutable
    }

    private fun String.toDate(): Date {
        val slashIndex = indexOf("/")
        val dayOfMonth = substring(0, slashIndex).toInt()
        @MonthInt val month = substring(slashIndex + 1).toInt()
        return Date.on(dayOfMonth, month)
    }
}
