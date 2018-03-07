package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.UpcomingEventsView

class UpcomingEventsViewRefresher(private val views: MutableSet<UpcomingEventsView>) {

    fun refreshViews() {
        for (view in views) {
            view.reloadUpcomingEventsView()
        }
    }

    fun addEventsView(view: UpcomingEventsView) {
        this.views.add(view)
    }

    fun removeView(view: UpcomingEventsView) {
        this.views.remove(view)
    }
}
