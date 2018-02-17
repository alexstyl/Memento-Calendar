package com.alexstyl.resources

import android.content.Context
import android.support.v4.content.ContextCompat
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.events.database.EventTypeId
import com.alexstyl.specialdates.events.peopleevents.EventType

class AndroidColors(private val context: Context) : Colors {
    override fun getColorFor(eventType: EventType) = when (eventType.id) {
        EventTypeId.TYPE_BIRTHDAY -> ContextCompat.getColor(context, R.color.birthday_red)
        EventTypeId.TYPE_NAMEDAY -> ContextCompat.getColor(context, R.color.nameday_blue)
        EventTypeId.TYPE_ANNIVERSARY -> ContextCompat.getColor(context, R.color.anniversary_yellow)
        EventTypeId.TYPE_CUSTOM -> ContextCompat.getColor(context, R.color.purple_custom_event)
        EventTypeId.TYPE_OTHER -> ContextCompat.getColor(context, R.color.purple_custom_event)
        else -> {
            throw IllegalStateException("No color matching for $eventType")
        }
    }

    override fun getDateHeaderTextColor(): Int = ContextCompat.getColor(context, R.color.upcoming_header_text_color)

    override fun getTodayHeaderTextColor(): Int = ContextCompat.getColor(context, R.color.upcoming_header_today_text_color)

    override fun getDailyReminderColor(): Int = ContextCompat.getColor(context, R.color.main_red)

    override fun getNamedaysColor(): Int = ContextCompat.getColor(context, R.color.nameday_blue)

    override fun getBankholidaysColor(): Int = ContextCompat.getColor(context, R.color.bankholiday_green)
}
