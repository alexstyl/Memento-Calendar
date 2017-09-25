package com.alexstyl.specialdates.upcoming

interface UpcomingEventsAdRules {
    fun shouldAppendAd(): Boolean
    fun onNewAdAdded()
}
