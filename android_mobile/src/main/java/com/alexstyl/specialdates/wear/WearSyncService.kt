package com.alexstyl.specialdates.wear

import android.app.IntentService
import android.content.Intent

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable

import javax.inject.Inject

import com.alexstyl.specialdates.date.todaysDate
import kotlin.collections.ArrayList

class WearSyncService : IntentService(WearSyncService::class.java.simpleName) {

    @Inject lateinit var namedayUserSettings: NamedayUserSettings
    @Inject lateinit var peopleEventsProvider: PeopleEventsProvider
    @Inject lateinit var tracker: CrashAndErrorTracker
    @Inject lateinit var permissions: MementoPermissions

    override fun onCreate() {
        super.onCreate()
        val applicationModule = (application as MementoApplication).applicationModule
        applicationModule.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        // TODO RecentPeopleEventsView MVP here
        if (!permissions.canReadAndWriteContacts()) {
            return
        }
        val closestDate = peopleEventsProvider.findClosestEventDateOnOrAfter(todaysDate())


        if (closestDate != null) {
            val contactEvents = peopleEventsProvider.fetchEventsOn(closestDate)
            val putDataRequest = createDataRequest(contactEvents, closestDate)
            val googleApiClient = GoogleApiClient.Builder(applicationContext)
                    .addApi(Wearable.API)
                    .build()

            if (googleApiClient.blockingConnect().isSuccess) {
                Wearable.DataApi.putDataItem(googleApiClient, putDataRequest)
            }
        }
    }

    private fun createDataRequest(contactEvents: List<ContactEvent>, date: Date): PutDataRequest {
        val putDataMapRequest = PutDataMapRequest.create(SharedConstants.NEXT_CONTACT_EVENTS_PATH)
        putDataMapRequest.dataMap.putStringArrayList(SharedConstants.KEY_CONTACTS_NAMES, listOfContactNamesFrom(contactEvents))
        putDataMapRequest.dataMap.putLong(SharedConstants.KEY_DATE, date.toMillis())
        return putDataMapRequest.asPutDataRequest()
    }

    private fun listOfContactNamesFrom(contacts: List<ContactEvent>) = ArrayList(contacts.map { it.contact.displayName.toString() })

}
