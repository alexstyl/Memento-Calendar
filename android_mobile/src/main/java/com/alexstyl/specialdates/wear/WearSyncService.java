package com.alexstyl.specialdates.wear;

import android.app.IntentService;
import android.content.Intent;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.permissions.MementoPermissions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.alexstyl.specialdates.date.DateExtKt.todaysDate;

public class WearSyncService extends IntentService {

    @Inject NamedayUserSettings namedayUserSettings;
    @Inject PeopleEventsProvider peopleEventsProvider;
    @Inject CrashAndErrorTracker tracker;
    @Inject MementoPermissions permissions;

    public WearSyncService() {
        super(WearSyncService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!permissions.canReadAndWriteContacts()) {
            return;
        }
        Optional<ContactEventsOnADate> eventsOptional = fetchContactEvents();
        if (eventsOptional.isPresent()) {
            ContactEventsOnADate contactEvents = eventsOptional.get();
            PutDataRequest putDataRequest = createDataRequest(contactEvents);
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(Wearable.API)
                    .build();

            if (googleApiClient.blockingConnect().isSuccess()) {
                Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
            }
        }
    }

    private Optional<ContactEventsOnADate> fetchContactEvents() {
        Date closestDate = peopleEventsProvider.findClosestEventDateOnOrAfter(todaysDate());
        if (closestDate != null) {
            return new Optional<>(peopleEventsProvider.fetchEventsOn(closestDate));
        } else {
            return Optional.Companion.absent();
        }
    }

    private PutDataRequest createDataRequest(ContactEventsOnADate contactEvents) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(SharedConstants.NEXT_CONTACT_EVENTS_PATH);
        putDataMapRequest.getDataMap().putStringArrayList(SharedConstants.KEY_CONTACTS_NAMES, getContactsNameListFrom(contactEvents.getContacts()));
        putDataMapRequest.getDataMap().putLong(SharedConstants.KEY_DATE, contactEvents.getDate().toMillis());
        return putDataMapRequest.asPutDataRequest();
    }

    private ArrayList<String> getContactsNameListFrom(List<Contact> contacts) {
        ArrayList<String> namesList = new ArrayList<>(contacts.size());
        for (Contact contact : contacts) {
            namesList.add(contact.getDisplayName().toString());
        }
        return namesList;
    }

}
