package com.alexstyl.specialdates.facebook.friendimport;

import android.content.ContentValues;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;

import java.util.List;

import static com.alexstyl.specialdates.events.database.EventColumns.SOURCE_FACEBOOK;

final public class FacebookFriendsPersister {

    private final PeopleEventsPersister persister;
    private final ContactEventsMarshaller marshaller;

    public FacebookFriendsPersister(PeopleEventsPersister persister, ContactEventsMarshaller marshaller) {
        this.persister = persister;
        this.marshaller = marshaller;
    }

    void keepOnly(List<ContactEvent> friends) {
        ContentValues[] contentValues = marshaller.marshall(friends);
        persister.deleteAllEventsOfSource(SOURCE_FACEBOOK);
        persister.insertAnnualEvents(contentValues);
    }

    public void removeAllFriends() {
        persister.deleteAllEventsOfSource(SOURCE_FACEBOOK);
    }
}
