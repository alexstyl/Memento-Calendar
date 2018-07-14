package com.alexstyl.specialdates.facebook.friendimport

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.evernote.android.job.Job

class FacebookFriendsJob(
        private val facebookFriendsUpdater: FacebookFriendsUpdater,
        private val tracker: CrashAndErrorTracker
) : Job() {


    override fun onRunJob(params: Params): Result {
        return try {
            facebookFriendsUpdater
                    .fetchFriends()
                    .subscribe()
            Result.SUCCESS
        } catch (e: FacebookUserLoggedOutException) {
            tracker.track(e)
            Result.FAILURE
        }
    }

    companion object {
        const val TAG = "Friends_Job"
    }
}
