package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.TimeOfDay
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit


class AndroidDailyReminderScheduler : DailyReminderScheduler {

    @JvmField
    val ONE_HOUR = TimeUnit.HOURS.toMillis(1)

    override fun scheduleReminderFor(timeOfDay: TimeOfDay) {
        DailyJob.schedule(JobRequest.Builder(DailyReminderJob.TAG),
                timeOfDay.toMillis(),
                timeOfDay.toMillis() + ONE_HOUR
        )
    }

    override fun cancelReminder() {
        JobManager.instance().cancelAllForTag(DailyReminderJob.TAG)
    }
}

