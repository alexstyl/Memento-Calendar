package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.UpcomingEventsView

interface UpcomingEventsRefresher {
    fun refreshViews()
    fun addEventsView(view: UpcomingEventsView)
    fun removeView(view: UpcomingEventsView)
}
