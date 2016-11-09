package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.date.ContactEvent;

import java.util.List;

class PeopleEventsDatabaseRefresher {

    private final PeopleEventsRepository repository;
    private final PeopleEventsPersister persister;
    private final Marshaller<ContactEvent> marshaller;

    PeopleEventsDatabaseRefresher(PeopleEventsRepository repository, Marshaller<ContactEvent> marshaller, PeopleEventsPersister persister) {
        this.persister = persister;
        this.marshaller = marshaller;
        this.repository = repository;
    }

    void refreshEvents() {
        persister.deleteEverythingButNamedays();
        List<ContactEvent> contacts = repository.fetchPeopleWithEvents();
        storeContactsToProvider(contacts);
    }

    private void storeContactsToProvider(List<ContactEvent> contacts) {
        ContentValues[] values = marshaller.marshall(contacts);
        persister.insertAnnualEvents(values);
    }

}
