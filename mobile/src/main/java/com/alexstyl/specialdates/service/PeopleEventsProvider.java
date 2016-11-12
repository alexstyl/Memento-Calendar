package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.PeopleEventsQuery;
import com.alexstyl.specialdates.events.database.PeopleEventsContract.PeopleEvents;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.SQLArgumentBuilder;
import com.alexstyl.specialdates.upcoming.TimePeriod;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.List;

public class PeopleEventsProvider {

    private static final String DATE_FROM = "substr(" + PeopleEvents.DATE + ",-5) >= ?";
    private static final String DATE_TO = "substr(" + PeopleEvents.DATE + ",-5) <= ?";
    private static final String DATE_BETWEEN_IGNORING_YEAR = DATE_FROM + " AND " + DATE_TO;
    private static final String[] PEOPLE_PROJECTION = new String[]{PeopleEvents.DATE};
    private static final String[] PROJECTION = {
            PeopleEvents.CONTACT_ID,
            PeopleEvents.SOURCE,
            PeopleEvents.DATE,
            PeopleEvents.EVENT_TYPE,
    };

    // TODO calculate namedays here
    private final ContactProvider contactProvider;
    private final ContentResolver resolver;
    private final NamedayPreferences namedayPreferences;

    public static PeopleEventsProvider newInstance(Context context) {
        ContactProvider provider = ContactProvider.get(context);
        ContentResolver resolver = context.getContentResolver();
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        return new PeopleEventsProvider(provider, resolver, namedayPreferences);
    }

    private PeopleEventsProvider(ContactProvider contactProvider, ContentResolver resolver, NamedayPreferences namedayPreferences) {
        this.contactProvider = contactProvider;
        this.resolver = resolver;
        this.namedayPreferences = namedayPreferences;
    }

    public List<ContactEvent> getCelebrationDateFor(TimePeriod timeDuration) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        Cursor cursor = queryEventsFor(timeDuration);
        throwIfInvalid(cursor);
        while (cursor.moveToNext()) {
            try {
                ContactEvent contactEvent = getContactEventFrom(cursor);
                contactEvents.add(contactEvent);
            } catch (ContactNotFoundException e) {
                Log.w(e);
            }
        }

        cursor.close();
        return contactEvents;
    }

    private Cursor queryEventsFor(TimePeriod timeDuration) {
        if (isWithinTheSameYear(timeDuration)) {
            return queryPeopleEvents(timeDuration, PeopleEvents.DATE + " ASC");
        } else {
            return queryForBothYearsIn(timeDuration);
        }
    }

    private Cursor queryPeopleEvents(TimePeriod timePeriod, String sortOrder) {
        String[] selectArgs = new String[]{
                SQLArgumentBuilder.dateWithoutYear(timePeriod.getStartingDate()),
                SQLArgumentBuilder.dateWithoutYear(timePeriod.getEndingDate()),
        };

        Cursor cursor = resolver.query(
                PeopleEvents.CONTENT_URI,
                PROJECTION,
                DATE_BETWEEN_IGNORING_YEAR,
                selectArgs,
                sortOrder
        );
        if (isInvalid(cursor)) {
            ErrorTracker.track(new IllegalStateException("People Events returned invalid cursor"));
        }
        return cursor;
    }

    private Cursor queryForBothYearsIn(TimePeriod timeDuration) {
        TimePeriod firstHalf = firstHalfOf(timeDuration);
        Cursor[] cursors = new Cursor[2];
        cursors[0] = queryPeopleEvents(firstHalf, PeopleEvents.DATE + " ASC");
        TimePeriod secondHalf = secondHalfOf(timeDuration);
        cursors[1] = queryPeopleEvents(secondHalf, PeopleEvents.DATE + " ASC");
        return new MergeCursor(cursors);
    }

    private TimePeriod firstHalfOf(TimePeriod timeDuration) {
        return TimePeriod.between(
                timeDuration.getStartingDate(),
                Date.endOfYear(timeDuration.getStartingDate().getYear())
        );
    }

    private TimePeriod secondHalfOf(TimePeriod timeDuration) {
        return TimePeriod.between(
                Date.startOfTheYear(timeDuration.getEndingDate().getYear()),
                timeDuration.getEndingDate()
        );
    }

    private boolean isWithinTheSameYear(TimePeriod timeDuration) {
        return timeDuration.getStartingDate().getYear() == timeDuration.getEndingDate().getYear();
    }

    private ContactEvent getContactEventFrom(Cursor cursor) throws ContactNotFoundException {
        long contactId = PeopleEvents.getContactIdFrom(cursor);
        Contact contact = contactProvider.getOrCreateContact(contactId);
        Date date = PeopleEvents.getDateFrom(cursor);
        EventType eventType = PeopleEvents.getEventType(cursor);

        return new ContactEvent(eventType, date, contact);
    }

    public ContactEvents getCelebrationsClosestTo(Date date) {
        Date closestDate = findClosestDateTo(date);
        return getCelebrationDateFor(closestDate);
    }

    ContactEvents getCelebrationDateFor(Date date) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        Cursor cursor = resolver.query(
                PeopleEvents.CONTENT_URI,
                null,
                getSelection(),
                getSelectArgs(date),
                PeopleEvents.CONTACT_ID
        );
        if (isInvalid(cursor)) {
            throw new DeveloperError("Cursor was invalid");
        }

        while (cursor.moveToNext()) {
            long contactId = PeopleEvents.getContactIdFrom(cursor);
            try {
                Contact contact = contactProvider.getOrCreateContact(contactId);
                EventType eventType = PeopleEvents.getEventType(cursor);

                ContactEvent event = new ContactEvent(eventType, date, contact);
                contactEvents.add(event);
            } catch (Exception e) {
                ErrorTracker.track(e);
            }
        }
        cursor.close();
        return ContactEvents.createFrom(date, contactEvents);
    }

    private Date findClosestDateTo(Date date) {
        Cursor cursor = queryDateClosestTo(date);
        if (isInvalid(cursor)) {
            throw new DeveloperError("Cursor was invalid");
        }

        Date dateFrom;
        if (cursor.moveToFirst()) {
            dateFrom = PeopleEvents.getDateFrom(cursor);
        } else {
            dateFrom = date;
        }
        cursor.close();
        return dateFrom;
    }

    private static final Uri PEOPLE_EVENTS = PeopleEvents.CONTENT_URI;

    private Cursor queryDateClosestTo(Date date) {
        return resolver.query(
                PEOPLE_EVENTS,
                PEOPLE_PROJECTION, whereDateIsEqualOrAfter(), thePassing(date), onlyTheFirstMatch()
        );
    }

    private String onlyTheFirstMatch() {
        return PeopleEvents.DATE + " ASC" + " LIMIT 1";
    }

    private String[] thePassing(Date date) {
        return new String[]{
                date.toShortDate()
        };
    }

    private String whereDateIsEqualOrAfter() {
        return PeopleEvents.DATE + " >= ?";
    }

    private String[] getSelectArgs(Date date) {
        return new String[]{date.toShortDate()};
    }

    private String getSelection() {
        if (namedaysAreEnabled()) {
            return PeopleEventsQuery.Date.SELECT;
        } else {
            return PeopleEventsQuery.Date.SELECT_ONLY_BIRTHDAYS;
        }
    }

    private boolean namedaysAreEnabled() {
        return namedayPreferences.isEnabled();
    }

    private static void throwIfInvalid(Cursor cursor) {
        if (isInvalid(cursor)) {
            throw new RuntimeException("Invalid cursor");
        }
    }

    private static boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

}
