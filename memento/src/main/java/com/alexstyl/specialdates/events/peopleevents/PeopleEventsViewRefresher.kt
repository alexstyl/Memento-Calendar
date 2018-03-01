package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.PeopleEventsView

class PeopleEventsViewRefresher(private val views: MutableSet<PeopleEventsView>) {

    fun refreshViews() {
        for (view in views) {
            view.refreshEventsView()
        }
    }

    fun addEventsView(view: PeopleEventsView) {
        this.views.add(view)
    }

    fun removeView(view: PeopleEventsView) {
        this.views.remove(view)
    }
}
