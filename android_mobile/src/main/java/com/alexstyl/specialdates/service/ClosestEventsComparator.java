package com.alexstyl.specialdates.service;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;

import java.util.Comparator;

final class ClosestEventsComparator implements Comparator<Optional<ContactEventsOnADate>> {

    private final DateComparator dateComparator = DateComparator.INSTANCE;

    @Override
    public int compare(Optional<ContactEventsOnADate> optionalA, Optional<ContactEventsOnADate> optionalB) {
        if (!optionalA.isPresent() && !optionalB.isPresent()) {
            return 0;
        }
        if (optionalA.isPresent() && !optionalB.isPresent()) {
            return -1;
        } else if (!optionalA.isPresent() && optionalB.isPresent()) {
            return 1;
        } else {
            ContactEventsOnADate eventsA = optionalA.get();
            ContactEventsOnADate eventsB = optionalB.get();
            return dateComparator.compare(eventsA.getDate(), eventsB.getDate());
        }
    }
}
