package com.alexstyl.specialdates

import com.alexstyl.specialdates.dailyreminder.DailyReminderJob
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderPresenter
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.upcoming.PeopleEventsRefreshJob
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class JobsCreator(private val peopleEventsUpdater: PeopleEventsUpdater,
                  private val presenter: DailyReminderPresenter,
                  private val notifier: DailyReminderNotifier) : JobCreator {

    override fun create(tag: String): Job? =
            when (tag) {
                PeopleEventsRefreshJob.TAG -> PeopleEventsRefreshJob(peopleEventsUpdater)
                DailyReminderJob.TAG -> DailyReminderJob(presenter, notifier)
                else -> {
                    null
                }
            }
}
