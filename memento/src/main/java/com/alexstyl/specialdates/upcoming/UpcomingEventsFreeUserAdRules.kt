package com.alexstyl.specialdates.upcoming

class UpcomingEventsFreeUserAdRules : UpcomingEventsAdRules {

    private var adAdded: Boolean = false

    override fun shouldAppendAd(): Boolean {
        return !this.adAdded
    }

    override fun onNewAdAdded() {
        this.adAdded = true
    }
}
