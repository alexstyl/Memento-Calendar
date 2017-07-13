package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class PeopleEventsProvider {

    private static final DateComparator dateComparator = DateComparator.INSTANCE;

    private final NamedayPreferences namedayPreferences;
    private final PeopleNamedaysCalculator peopleNamedaysCalculator;
    private final StaticPeopleEventsProvider staticEventsProvider;
    private final ClosestEventsComparator closestEventsComparator = new ClosestEventsComparator();

    public static PeopleEventsProvider newInstance(Context context) {
        ContactsProvider contactsProvider = ContactsProvider.get(context);
        ContentResolver resolver = context.getContentResolver();
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        NamedayCalendarProvider namedayCalendarProvider = NamedayCalendarProvider.newInstance(context.getResources());
        PeopleNamedaysCalculator peopleNamedaysCalculator = new PeopleNamedaysCalculator(
                namedayPreferences,
                namedayCalendarProvider,
                contactsProvider
        );
        CustomEventProvider customEventProvider = new CustomEventProvider(resolver);
        StaticPeopleEventsProvider staticEventsProvider = new StaticPeopleEventsProvider(resolver, contactsProvider, customEventProvider);
        return new PeopleEventsProvider(
                namedayPreferences,
                peopleNamedaysCalculator,
                staticEventsProvider
        );
    }

    PeopleEventsProvider(NamedayPreferences namedayPreferences,
                         PeopleNamedaysCalculator peopleNamedaysCalculator,
                         StaticPeopleEventsProvider staticEventsProvider
    ) {
        this.staticEventsProvider = staticEventsProvider;
        this.namedayPreferences = namedayPreferences;
        this.peopleNamedaysCalculator = peopleNamedaysCalculator;
    }

    public List<ContactEvent> getCelebrationDateOn(Date date) {
        TimePeriod timeDuration = TimePeriod.between(date, date);
        List<ContactEvent> contactEvents = new ArrayList<>();
        contactEvents.addAll(staticEventsProvider.fetchEventsBetween(timeDuration));
        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(timeDuration);
            contactEvents.addAll(namedaysContactEvents);
        }
        return contactEvents;

    }

    public List<ContactEvent> getContactEventsFor(TimePeriod timeDuration) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        contactEvents.addAll(staticEventsProvider.fetchEventsBetween(timeDuration));

        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(timeDuration);
            contactEvents.addAll(namedaysContactEvents);
        }
        return Collections.unmodifiableList(contactEvents);
    }

    public Observable<List<ContactEvent>> getContactEventsFor(final Contact contact) {
        return Observable.fromCallable(new Callable<List<ContactEvent>>() {
            @Override
            public List<ContactEvent> call() throws Exception {
                List<ContactEvent> contactEvents = new ArrayList<>();
                contactEvents.addAll(staticEventsProvider.fetchEventsFor(contact));
                if (namedayPreferences.isEnabled()) {
                    List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysFor(contact);
                    contactEvents.addAll(namedaysContactEvents);
                }
                return Collections.unmodifiableList(contactEvents);
            }
        });
    }

    public Optional<ContactEventsOnADate> getCelebrationsClosestTo(Date date) {
        ensureDateHasYear(date);

        Optional<ContactEventsOnADate> staticEvents = findNextStaticEventsOn(date);
        Optional<ContactEventsOnADate> dynamicEvents;
        if (namedayPreferences.isEnabled()) {
            dynamicEvents = findNextDynamicEventsOn(date);
        } else {
            dynamicEvents = Optional.absent();
        }
        return returnClosestEventsOrMerge(staticEvents, dynamicEvents);

    }

    private Optional<ContactEventsOnADate> findNextStaticEventsOn(Date date) {
        try {
            Date closestStaticDate = staticEventsProvider.findClosestStaticEventDateFrom(date);
            return new Optional<>(staticEventsProvider.fetchEventsOn(closestStaticDate));
        } catch (NoEventsFoundException e) {
            return Optional.absent();
        }
    }

    private Optional<ContactEventsOnADate> findNextDynamicEventsOn(Date date) {
        try {
            Date closestDynamicDate = findNextDynamicEventDateAfter(date);
            ContactEventsOnADate namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysOn(closestDynamicDate);
            return new Optional<>(namedaysContactEvents);
        } catch (NoEventsFoundException e) {
            return Optional.absent();
        }
    }

    private Date findNextDynamicEventDateAfter(final Date date) throws NoEventsFoundException {
        List<ContactEvent> contactEvents = new ArrayList<>(peopleNamedaysCalculator.loadSpecialNamedaysBetween(TimePeriod.between(date, Date.endOfYear(date.getYear()))));
        Collections.sort(contactEvents, new Comparator<ContactEvent>() {
            @Override
            public int compare(ContactEvent o1, ContactEvent o2) {
                return dateComparator.compare(o1.getDate(), o2.getDate());
            }
        });

        for (ContactEvent contactEvent : contactEvents) {
            Date contactEventDate = contactEvent.getDate();
            if (dateComparator.compare(contactEventDate, date) >= 0) {
                return contactEventDate;
            }
        }
        return date;
    }

    private Optional<ContactEventsOnADate> returnClosestEventsOrMerge(Optional<ContactEventsOnADate> staticEvents, Optional<ContactEventsOnADate> dynamicEvents) {
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

    private Optional<ContactEventsOnADate> createOptionalFor(Optional<ContactEventsOnADate> staticEvents, Optional<ContactEventsOnADate> dynamicEvents) {
        List<ContactEvent> combinedEvents = combine(dynamicEvents.get().getEvents(), staticEvents.get().getEvents());
        if (combinedEvents.size() > 0) {
            return new Optional<>(ContactEventsOnADate.createFrom(
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
