package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import com.evernote.android.job.Job

class PeopleEventsRefreshJob(private val peopleEventsUpdater: PeopleEventsUpdater,
                             private val uiRefresher: UpcomingEventsViewRefresher) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        peopleEventsUpdater.updateEvents()
        uiRefresher.refreshViews()
        return Job.Result.SUCCESS
    }
}
