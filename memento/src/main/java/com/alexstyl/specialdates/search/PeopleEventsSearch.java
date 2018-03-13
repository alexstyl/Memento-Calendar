package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.util.HashMapList;

import java.util.ArrayList;
import java.util.List;

final class PeopleEventsSearch {

    private final NameMatcher nameMatcher;
    private final PeopleEventsProvider peopleEventsProvider;

    PeopleEventsSearch(PeopleEventsProvider peopleEventsProvider, NameMatcher nameMatcher) {
        this.peopleEventsProvider = peopleEventsProvider;
        this.nameMatcher = nameMatcher;
    }

    List<ContactWithEvents> searchForContacts(String searchQuery, int counter) {
        if (counter <= 0) {
            return new ArrayList<>();
        }

        searchQuery = searchQuery.trim();
        HashMapList<Contact, ContactEvent> events = new HashMapList<>();
        TimePeriod between = TimePeriod.Companion.aYearFromNow();
        List<ContactEvent> contactEventsOnDate = peopleEventsProvider.fetchEventsBetween(between);
        int size = 0;
        for (ContactEvent contactEvent : contactEventsOnDate) {
            Contact contact = contactEvent.getContact();
            if (nameMatcher.match(contact.getDisplayName(), searchQuery)) {
                events.addValue(contact, contactEvent);
                size++;
            }
            if (size >= counter) {
                break;
            }
        }

        List<ContactWithEvents> contactWithEvents = new ArrayList<>();
        for (Contact contact : events.keys()) {
            List<ContactEvent> list = events.get(contact);
            contactWithEvents.add(new ContactWithEvents(contact, list));
        }
        return contactWithEvents;
    }
}
