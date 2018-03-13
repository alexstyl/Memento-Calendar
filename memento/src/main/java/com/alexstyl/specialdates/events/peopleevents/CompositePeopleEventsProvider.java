package com.alexstyl.specialdates.events.peopleevents;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositePeopleEventsProvider implements PeopleEventsProvider {

    private final NamedayUserSettings namedayPreferences;
    private final PeopleNamedaysCalculator peopleNamedaysCalculator;
    private final PeopleEventsProvider deviceEvents;
    private final ClosestEventsComparator closestEventsComparator = new ClosestEventsComparator();

    CompositePeopleEventsProvider(NamedayUserSettings namedayPreferences,
                                  PeopleNamedaysCalculator peopleNamedaysCalculator,
                                  PeopleEventsProvider deviceEvents) {
        this.deviceEvents = deviceEvents;
        this.namedayPreferences = namedayPreferences;
        this.peopleNamedaysCalculator = peopleNamedaysCalculator;
    }

    @NonNull
    @Override
    public ContactEventsOnADate fetchEventsOn(@NonNull Date date) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        ContactEventsOnADate contactEventsOnADate = deviceEvents.fetchEventsOn(date);
        contactEvents.addAll(contactEventsOnADate.getEvents());
        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.fetchEventsOn(date).getEvents();
            contactEvents.addAll(namedaysContactEvents);
        }

        return ContactEventsOnADate.Companion.createFrom(date, contactEvents);
    }

    @NonNull
    @Override
    public List<ContactEvent> fetchEventsBetween(@NonNull TimePeriod timePeriod) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        contactEvents.addAll(deviceEvents.fetchEventsBetween(timePeriod));

        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.fetchEventsBetween(timePeriod);
            contactEvents.addAll(namedaysContactEvents);
        }
        return Collections.unmodifiableList(contactEvents);
    }

    @Override
    public List<ContactEvent> fetchEventsFor(Contact contact) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        contactEvents.addAll(deviceEvents.fetchEventsFor(contact));
        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.fetchEventsFor(contact);
            contactEvents.addAll(namedaysContactEvents);
        }
        return Collections.unmodifiableList(contactEvents);
    }

    @NonNull
    @Override
    public Date findClosestEventDateOnOrAfter(@NonNull Date date) throws NoEventsFoundException {
        ensureDateHasYear(date);

        Optional<ContactEventsOnADate> staticEvents = findNextStaticEventsFor(date);
        Optional<ContactEventsOnADate> dynamicEvents = findNextDynamicEventFor(date);
        Optional<ContactEventsOnADate> contactEventsOnADateOptional = returnClosestEventsOrMerge(staticEvents, dynamicEvents);
        if (contactEventsOnADateOptional.isPresent()) {
            return contactEventsOnADateOptional.get().getDate();
        }
        throw new NoEventsFoundException("There are no events after " + date);
    }

    private Optional<ContactEventsOnADate> findNextStaticEventsFor(Date date) {
        try {
            Date closestStaticDate = deviceEvents.findClosestEventDateOnOrAfter(date);
            return new Optional<>(deviceEvents.fetchEventsOn(closestStaticDate));
        } catch (NoEventsFoundException e) {
            return Optional.absent();
        }
    }

    private Optional<ContactEventsOnADate> findNextDynamicEventFor(Date date) {
        if (!namedayPreferences.isEnabled()) {
            return Optional.absent();
        }
        try {
            Date closestEventDateOnOrAfter = peopleNamedaysCalculator.findClosestEventDateOnOrAfter(date);
            return new Optional<>(peopleNamedaysCalculator.fetchEventsOn(closestEventDateOnOrAfter));
        } catch (NoEventsFoundException e) {
            e.printStackTrace();
        }
        return Optional.absent();
    }

    private Optional<ContactEventsOnADate> returnClosestEventsOrMerge(Optional<ContactEventsOnADate> staticEvents,
                                                                      Optional<ContactEventsOnADate> dynamicEvents) {
        if (!staticEvents.isPresent() && !dynamicEvents.isPresent()) {
            return Optional.absent();
        }

        int result = closestEventsComparator.compare(staticEvents, dynamicEvents);
        if (result < 0) {
            return absentIfContainsNoEvent(staticEvents);
        } else if (result > 0) {
            return absentIfContainsNoEvent(dynamicEvents);
        } else {
            return absentIfContainsNoEvent(createOptionalFor(staticEvents, dynamicEvents));
        }
    }

    private Optional<ContactEventsOnADate> createOptionalFor(Optional<ContactEventsOnADate> staticEvents,
                                                             Optional<ContactEventsOnADate> dynamicEvents) {
        List<ContactEvent> combinedEvents = combine(dynamicEvents.get().getEvents(), staticEvents.get().getEvents());
        if (combinedEvents.size() > 0) {
            return new Optional<>(ContactEventsOnADate.Companion.createFrom(
                    staticEvents.get().getDate(),
                    combinedEvents
            ));
        } else {
            return Optional.absent();
        }
    }

    private Optional<ContactEventsOnADate> absentIfContainsNoEvent(Optional<ContactEventsOnADate> optional) {
        if (optional.isPresent()) {
            List<ContactEvent> events = optional.get().getEvents();
            if (events.size() > 0) {
                return optional;
            }
        }
        return Optional.absent();
    }

    private static <T> List<T> combine(List<T> listA, List<T> listB) {
        List<T> contactEvents = new ArrayList<>();
        contactEvents.addAll(listA);
        contactEvents.addAll(listB);
        return Collections.unmodifiableList(contactEvents);
    }

    private static void ensureDateHasYear(Date date) {
        if (!date.hasYear()) {
            throw new IllegalArgumentException("Date must contain year");
        }
    }
}
