package com.alexstyl.specialdates.upcoming.widget.list

import com.alexstyl.specialdates.upcoming.UpcomingEventsAdRules

class NoAds : UpcomingEventsAdRules {

    override fun shouldAppendAd(): Boolean {
        return false
    }

    override fun onNewAdAdded() {
        // do nothing
    }
}
