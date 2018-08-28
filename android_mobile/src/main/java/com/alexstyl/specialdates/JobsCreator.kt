package com.alexstyl.specialdates

import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderPresenter
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsJob
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsUpdater
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.upcoming.PeopleEventsRefreshJob
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class JobsCreator(private val peopleEventsUpdater: PeopleEventsUpdater,
                  private val presenter: DailyReminderPresenter,
                  private val notifier: DailyReminderNotifier,
                  private val permissions: MementoPermissions,
                  private val facebookFriendsUpdater: FacebookFriendsUpdater,
                  private val tracker: CrashAndErrorTracker
) : JobCreator {

    override fun create(tag: String): Job? =
            when (tag) {
                PeopleEventsRefreshJob.TAG -> PeopleEventsRefreshJob(peopleEventsUpdater, permissions)
                FacebookFriendsJob.TAG -> FacebookFriendsJob(facebookFriendsUpdater, tracker)
                else -> {
                    null
                }
            }
}
