package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.evernote.android.job.Job

class PeopleEventsRefreshJob(private val peopleEventsUpdater: PeopleEventsUpdater,
                             private val permissions: MementoPermissions) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        return if (permissions.canReadContacts()) {
            peopleEventsUpdater
                    .updateEvents()
                    .subscribe()
            Job.Result.SUCCESS
        } else {
            Result.FAILURE
        }
    }

    companion object {
        const val TAG = "People_Job"
    }
}
