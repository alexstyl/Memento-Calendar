package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.AndroidContactsProvider;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeopleEventsProvider {

    private final ContactsProvider contactsProvider;
    private final NamedayPreferences namedayPreferences;
    private final PeopleNamedaysCalculator peopleNamedaysCalculator;
    private final StaticPeopleEventsProvider staticEventsProvider;

    public static PeopleEventsProvider newInstance(Context context) {
        AndroidContactsProvider contactsProvider = AndroidContactsProvider.get(context);
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
                contactsProvider,
                namedayPreferences,
                peopleNamedaysCalculator,
                staticEventsProvider
        );
    }

    PeopleEventsProvider(ContactsProvider contactsProvider,
                         NamedayPreferences namedayPreferences,
                         PeopleNamedaysCalculator peopleNamedaysCalculator,
                         StaticPeopleEventsProvider staticEventsProvider
    ) {
        this.contactsProvider = contactsProvider;
        this.staticEventsProvider = staticEventsProvider;
        this.namedayPreferences = namedayPreferences;
        this.peopleNamedaysCalculator = peopleNamedaysCalculator;
    }

    public List<ContactEvent> getCelebrationDateOn(Date date) {
        TimePeriod timeDuration = TimePeriod.between(date, date);
        List<ContactEvent> contactEvents = staticEventsProvider.fetchEventsBetween(timeDuration);

        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(timeDuration);
            contactEvents.addAll(namedaysContactEvents);
        }
        return contactEvents;

    }

    public List<ContactEvent> getContactEventsFor(TimePeriod timeDuration) {
        List<ContactEvent> contactEvents = staticEventsProvider.fetchEventsBetween(timeDuration);

        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(timeDuration);
            contactEvents.addAll(namedaysContactEvents);
        }
        return Collections.unmodifiableList(contactEvents);
    }

    public ContactEventsOnADate getCelebrationsClosestTo(Date date) {
        Optional<Date> closestStaticDate = staticEventsProvider.findClosestStaticEventDateFrom(date);
        ContactEventsOnADate staticEvents = staticEventsProvider.fetchEventsOn(date);
        ContactEventsOnADate dynamicEvents = getDynamicEvents(date, closestStaticDate);

        if (DateComparator.INSTANCE.compare(closestStaticDate.get(), dynamicEvents.getDate()) == 0) {
            return ContactEventsOnADate.createFrom(dynamicEvents.getDate(), combine(dynamicEvents.getEvents(), staticEvents.getEvents()));
        } else if (DateComparator.INSTANCE.compare(closestStaticDate.get(), dynamicEvents.getDate()) > 0) {
            return dynamicEvents;
        } else {
            return staticEvents;
        }
    }

    private ContactEventsOnADate getDynamicEvents(Date date, Optional<Date> closestStaticDate) {
        if (namedayPreferences.isEnabled()) {
            Optional<Date> closestDynamicDate = findClosestDynamicEventDateTo(date);
            if (closestDynamicDate.isPresent()) {
                List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysOn(closestDynamicDate.get());
                return ContactEventsOnADate.createFrom(closestDynamicDate.get(), namedaysContactEvents);
            }
        }
        return ContactEventsOnADate.createFrom(closestStaticDate.get(), Collections.<ContactEvent>emptyList());
    }

    private Optional<Date> findClosestDynamicEventDateTo(Date date) {
        List<ContactEvent> contactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(TimePeriod.between(date, date.addWeek(4)));
        if (contactEvents.size() > 0) {
            return new Optional<>(contactEvents.get(0).getDate());
        }
        return Optional.absent();
    }

    private static Date from(String text) {
        try {
            return DateParser.INSTANCE.parse(text);
        } catch (DateParseException e) {
            e.printStackTrace();
            throw new DeveloperError("Invalid date stored to database. [" + text + "]");
        }
    }

    private static <T> List<T> combine(List<T> listA, List<T> listB) {
        List<T> contactEvents = new ArrayList<>();
        contactEvents.addAll(listA);
        contactEvents.addAll(listB);
        return contactEvents;
    }

}
