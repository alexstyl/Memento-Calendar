package com.alexstyl.specialdates.addevent.ui;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.search.NameMatcher;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.TimePeriod;

import java.util.ArrayList;
import java.util.List;

final class ContactsSearch {

    private final PeopleEventsProvider peopleEventsProvider;
    private final NameMatcher nameMatcher;

    ContactsSearch(PeopleEventsProvider peopleEventsProvider, NameMatcher nameMatcher) {
        this.peopleEventsProvider = peopleEventsProvider;
        this.nameMatcher = nameMatcher;
    }

    List<Contact> searchForContacts(String searchQuery, int counter) {
        searchQuery = searchQuery.trim();
        List<Contact> matchedContacts = new ArrayList<>();
        Date from = Date.today();
        Date to = from.addDay(364);
        List<ContactEvent> contactEvents = peopleEventsProvider.getCelebrationDateFor(TimePeriod.between(from, to));
        int size = 0;

        for (ContactEvent contactEvent : contactEvents) {
            DisplayName displayName = contactEvent.getContact().getDisplayName();
            if (nameMatcher.match(displayName, searchQuery)) {
                matchedContacts.add(contactEvent.getContact());
                size++;
                if (size == counter) {
                    break;
                }
            }
        }
        return matchedContacts;
    }

}
