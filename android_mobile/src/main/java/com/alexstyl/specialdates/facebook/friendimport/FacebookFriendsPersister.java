package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;

import java.util.List;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

public final class FacebookFriendsPersister {

    private final PeopleEventsPersister persister;

    public FacebookFriendsPersister(PeopleEventsPersister persister) {
        this.persister = persister;
    }

    void keepOnly(List<ContactEvent> friends) {
        persister.deleteAllEventsOfSource(SOURCE_FACEBOOK);
        persister.insertAnnualEvents(friends);
    }

    public void removeAllFriends() {
        persister.deleteAllEventsOfSource(SOURCE_FACEBOOK);
    }
}
