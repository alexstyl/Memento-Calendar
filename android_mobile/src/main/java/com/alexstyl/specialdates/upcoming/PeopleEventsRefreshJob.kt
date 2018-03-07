package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.evernote.android.job.Job

class PeopleEventsRefreshJob(private val peopleEventsUpdater: PeopleEventsUpdater) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        peopleEventsUpdater
                .updateEvents()
                .subscribe()
        return Job.Result.SUCCESS
    }
}
