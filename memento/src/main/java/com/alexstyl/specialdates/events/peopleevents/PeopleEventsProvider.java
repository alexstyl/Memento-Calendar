package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;

import java.util.List;

interface PeopleEventsProvider {
    ContactEventsOnADate fetchEventsOn(Date date);

    List<ContactEvent> fetchEventsBetween(TimePeriod timeDuration);

    List<ContactEvent> fetchEventsFor(Contact contact);

    Date findClosestStaticEventDateFrom(Date date) throws NoEventsFoundException;
}
