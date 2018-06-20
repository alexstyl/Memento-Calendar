package com.alexstyl.specialdates.support

import android.app.Activity
import android.content.Intent
import android.text.format.DateUtils

class AskForSupport(private val preferences: CallForRatingPreferences) {

    private val isTimeToAskAgain: Boolean
        get() {
            if (preferences.isTriggered) {
                preferences.resetTrigger()
                return true
            }
            val timeSinceLastRate = System.currentTimeMillis() - preferences.lastAskTimeAsked()
            return timeSinceLastRate > RETRY_INTERVAL
        }

    fun shouldAskForRating(): Boolean {
        return !preferences.hasUserRated() && isTimeToAskAgain
    }

    fun onRateEnd() {
        preferences.setHasUserRated(true)
    }

    fun requestForRatingSooner() {
        preferences.triggerNextTime()
    }

    fun askForRatingFromUser(activity: Activity) {
        val intent = Intent(activity, RateDialog::class.java)
        activity.startActivity(intent)
        preferences.setLastAskTimedNow()
    }

    companion object {
        private const val RETRY_INTERVAL = DateUtils.DAY_IN_MILLIS
    }
}
