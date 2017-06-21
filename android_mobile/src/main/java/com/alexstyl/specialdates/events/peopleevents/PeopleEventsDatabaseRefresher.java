package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.date.ContactEvent;

import java.util.List;

class PeopleEventsDatabaseRefresher {

    private final AndroidEventsRepository repository;
    private final PeopleEventsPersister persister;
    private final ContactEventsMarshaller marshaller;
    private final PeopleEventsViewRefresher refresher;

    PeopleEventsDatabaseRefresher(AndroidEventsRepository repository,
                                  ContactEventsMarshaller marshaller,
                                  PeopleEventsPersister persister,
                                  PeopleEventsViewRefresher refresher) {
        this.persister = persister;
        this.marshaller = marshaller;
        this.repository = repository;
        this.refresher = refresher;
    }

    void refreshEvents() {
        persister.deleteAllDeviceEvents();
        List<ContactEvent> contacts = repository.fetchPeopleWithEvents();
        storeContactsToProvider(contacts);
        refresher.updateAllViews();
    }

    private void storeContactsToProvider(List<ContactEvent> contacts) {
        ContentValues[] values = marshaller.marshall(contacts);
        persister.insertAnnualEvents(values);
    }

}
