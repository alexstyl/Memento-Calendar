package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.UpcomingEventsView

class UpcomingEventsViewRefresher(private val views: MutableSet<UpcomingEventsView>) : UpcomingEventsRefresher {

    override fun refreshViews() {
        for (view in views) {
            view.reloadUpcomingEventsView()
        }
    }

    override fun addEventsView(view: UpcomingEventsView) {
        this.views.add(view)
    }

    override fun removeView(view: UpcomingEventsView) {
        this.views.remove(view)
    }
}
