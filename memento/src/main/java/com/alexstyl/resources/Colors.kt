package com.alexstyl.resources

import com.alexstyl.specialdates.events.peopleevents.EventType

interface Colors {
    fun getColorFor(eventType: EventType): Int
    fun getTodayHeaderTextColor(): Int
    fun getDateHeaderTextColor(): Int
    fun getDailyReminderColor(): Int
    fun getNamedaysColor(): Int
    fun getBankholidaysColor(): Int
}
