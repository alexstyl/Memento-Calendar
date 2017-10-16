package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.date.ContactEvent;

import java.util.List;

public class DeviceEventsDatabaseRefresher {

    private final AndroidEventsRepository repository;
    private final PeopleEventsPersister persister;
    private final ContactEventsMarshaller marshaller;

    public DeviceEventsDatabaseRefresher(
            AndroidEventsRepository repository,
            ContactEventsMarshaller marshaller,
            PeopleEventsPersister persister) {
        this.persister = persister;
        this.marshaller = marshaller;
        this.repository = repository;
    }

    public void rebuildEvents() {
        persister.deleteAllDeviceEvents();
        List<ContactEvent> contacts = repository.fetchPeopleWithEvents();
        storeContactsToProvider(contacts);
    }

    private void storeContactsToProvider(List<ContactEvent> contacts) {
        ContentValues[] values = marshaller.marshall(contacts);
        persister.insertAnnualEvents(values);
    }

}
