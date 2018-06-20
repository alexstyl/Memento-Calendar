package com.alexstyl

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.events.peopleevents.EventType

class TestColors : Colors {
    override fun getColorFor(eventType: EventType): Int = eventType.id
    override fun getTodayHeaderTextColor(): Int = 0

    override fun getDateHeaderTextColor(): Int = 1

    override fun getDailyReminderColor(): Int = 2

    override fun getNamedaysColor(): Int = 3

    override fun getBankholidaysColor(): Int = 4

}
