package com.alexstyl.specialdates.wear;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.ContactEvents;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class WearSyncService extends IntentService {

    public WearSyncService() {
        super(WearSyncService.class.getSimpleName());
    }

    public static void startService(Context context) {
        Intent service = new Intent(context, WearSyncService.class);
        context.startService(service);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ContactEvents contactEvents = fetchContactEvents();
        if (contactEvents.size() == 0) {
            return;
        }

        PutDataRequest putDataRequest = createDataRequest(contactEvents);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Wearable.API)
                .build();

        if (googleApiClient.blockingConnect().isSuccess()) {
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
        }
    }

    private ContactEvents fetchContactEvents() {
        PeopleEventsProvider eventsProvider = PeopleEventsProvider.newInstance(this);
        DayDate today = DayDate.today();
        return eventsProvider.getCelebrationsClosestTo(today);
    }

    private PutDataRequest createDataRequest(ContactEvents contactEvents) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(SharedConstants.NEXT_CONTACT_EVENTS_PATH);
        putDataMapRequest.getDataMap().putStringArrayList(SharedConstants.KEY_CONTACTS_NAMES, getContactsNameListFrom(contactEvents.getContacts()));
        putDataMapRequest.getDataMap().putLong(SharedConstants.KEY_DATE, contactEvents.getDate().toMillis());
        return putDataMapRequest.asPutDataRequest();
    }

    private ArrayList<String> getContactsNameListFrom(List<Contact> contacts) {
        ArrayList<String> namesList = new ArrayList<String>(contacts.size());
        for (Contact contact : contacts) {
            namesList.add(contact.getDisplayName().toString());
        }
        return namesList;
    }

}
