package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.TimeOfDay
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest


class AndroidDailyReminderScheduler : DailyReminderScheduler {

    override fun scheduleReminderFor(timeOfDay: TimeOfDay) {
        DailyJob.schedule(JobRequest.Builder(DailyReminderJob.TAG),
                timeOfDay.toMillis(),
                timeOfDay.toMillis()
        )
    }

    override fun cancelReminder() {
        JobManager.instance().cancelAllForTag(DailyReminderJob.TAG)
    }
}
