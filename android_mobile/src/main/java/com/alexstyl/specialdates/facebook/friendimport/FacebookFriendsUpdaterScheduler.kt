package com.alexstyl.specialdates.facebook.friendimport

import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

class FacebookFriendsUpdaterScheduler {

    fun startImmediate() {
        JobRequest.Builder(FacebookFriendsJob.TAG)
                .startNow()
                .build()
                .schedule()
    }

    fun scheduleNext() {
        DailyJob.schedule(
                JobRequest.Builder(FacebookFriendsJob.TAG)
                        .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                        .setRequirementsEnforced(true),
                5.oClock(), 6.oClock()
        )
    }

    private fun Int.oClock(): Long = TimeUnit.HOURS.toMillis(this.toLong())
}
