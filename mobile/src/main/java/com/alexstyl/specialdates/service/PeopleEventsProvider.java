package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.AndroidContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.datedetails.PeopleEventsQuery;
import com.alexstyl.specialdates.events.database.EventColumns;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract.PeopleEvents;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;
import com.alexstyl.specialdates.SQLArgumentBuilder;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeopleEventsProvider {

    private static final String DATE_FROM = "substr(" + PeopleEvents.DATE + ",-5) >= ?";
    private static final String DATE_TO = "substr(" + PeopleEvents.DATE + ",-5) <= ?";
    private static final String DATE_BETWEEN_IGNORING_YEAR = DATE_FROM + " AND " + DATE_TO;
    private static final String[] PEOPLE_PROJECTION = new String[]{PeopleEvents.DATE};
    private static final String[] PROJECTION = {
            PeopleEvents.CONTACT_ID,
            PeopleEvents.DEVICE_EVENT_ID,
            PeopleEvents.DATE,
            PeopleEvents.EVENT_TYPE,
    };

    private final AndroidContactsProvider contactsProvider;
    private final ContentResolver resolver;
    private final NamedayPreferences namedayPreferences;
    private final PeopleNamedaysCalculator peopleNamedaysCalculator;
    private final CustomEventProvider customEventProvider;

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
        return new PeopleEventsProvider(contactsProvider, resolver, namedayPreferences, peopleNamedaysCalculator, customEventProvider);
    }

    private PeopleEventsProvider(AndroidContactsProvider contactsProvider,
                                 ContentResolver resolver,
                                 NamedayPreferences namedayPreferences,
                                 PeopleNamedaysCalculator peopleNamedaysCalculator, CustomEventProvider customEventProvider) {
        this.contactsProvider = contactsProvider;
        this.resolver = resolver;
        this.namedayPreferences = namedayPreferences;
        this.peopleNamedaysCalculator = peopleNamedaysCalculator;
        this.customEventProvider = customEventProvider;
    }

    public List<ContactEvent> getCelebrationDateOn(Date date) {
        TimePeriod timeDuration = TimePeriod.between(date, date);
        List<ContactEvent> contactEvents = fetchStaticEventsBetween(timeDuration);

        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(timeDuration);
            contactEvents.addAll(namedaysContactEvents);
        }
        return contactEvents;

    }

    public List<ContactEvent> getCelebrationDateFor(TimePeriod timeDuration) {
        List<ContactEvent> contactEvents = fetchStaticEventsBetween(timeDuration);

        if (namedayPreferences.isEnabled()) {
            List<ContactEvent> namedaysContactEvents = peopleNamedaysCalculator.loadSpecialNamedaysBetween(timeDuration);
            contactEvents.addAll(namedaysContactEvents);
        }
        return Collections.unmodifiableList(contactEvents);
    }

    private List<ContactEvent> fetchStaticEventsBetween(TimePeriod timeDuration) {
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

    private static TimePeriod firstHalfOf(TimePeriod timeDuration) {
        return TimePeriod.between(
                timeDuration.getStartingDate(),
                Date.endOfYear(timeDuration.getStartingDate().getYear())
        );
    }

    private static TimePeriod secondHalfOf(TimePeriod timeDuration) {
        return TimePeriod.between(
                Date.startOfTheYear(timeDuration.getEndingDate().getYear()),
                timeDuration.getEndingDate()
        );
    }

    private boolean isWithinTheSameYear(TimePeriod timeDuration) {
        return timeDuration.getStartingDate().getYear() == timeDuration.getEndingDate().getYear();
    }

    private ContactEvent getContactEventFrom(Cursor cursor) throws ContactNotFoundException {
        long contactId = getContactIdFrom(cursor);
        Contact contact = contactsProvider.getOrCreateContact(contactId);
        Date date = getDateFrom(cursor);
        EventType eventType = getEventType(cursor);

        Optional<Long> eventId = getDeviceEventIdFrom(cursor);
        return new ContactEvent(eventId, eventType, date, contact);
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
            long contactId = getContactIdFrom(cursor);
            try {
                Contact contact = contactsProvider.getOrCreateContact(contactId);
                EventType eventType = getEventType(cursor);
                Optional<Long> deviceEventId = getDeviceEventIdFrom(cursor);

                ContactEvent event = new ContactEvent(deviceEventId, eventType, date, contact);
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
            dateFrom = getDateFrom(cursor);
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
                PEOPLE_PROJECTION,
                PeopleEvents.DATE + " >= ?",
                thePassing(date),
                PeopleEvents.DATE + " ASC LIMIT 1"
        );
    }

    private String[] thePassing(Date date) {
        return new String[]{
                date.toShortDate()
        };
    }

    private String[] getSelectArgs(Date date) {
        return new String[]{date.toShortDate()};
    }

    private String getSelection() {
        if (namedaysAreEnabled()) {
            return PeopleEventsQuery.SELECT;
        } else {
            return PeopleEventsQuery.SELECT_ONLY_BIRTHDAYS;
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

    private static Date getDateFrom(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DATE);
        String text = cursor.getString(index);
        return from(text);
    }

    private static long getContactIdFrom(Cursor cursor) {
        int contactIdIndex = cursor.getColumnIndexOrThrow(PeopleEvents.CONTACT_ID);
        return cursor.getLong(contactIdIndex);
    }

    private EventType getEventType(Cursor cursor) {
        int eventTypeIndex = cursor.getColumnIndexOrThrow(PeopleEvents.EVENT_TYPE);
        @EventTypeId int rawEventType = cursor.getInt(eventTypeIndex);
        if (rawEventType == EventColumns.TYPE_CUSTOM) {
            Optional<Long> deviceEventIdFrom = getDeviceEventIdFrom(cursor);
            if (deviceEventIdFrom.isPresent()) {
                return queryCustomEvent(deviceEventIdFrom.get());
            }
            return StandardEventType.OTHER;
        }
        return StandardEventType.fromId(rawEventType);
    }

    private EventType queryCustomEvent(long deviceId) {
        return customEventProvider.getEventWithId(deviceId);
    }

    private static Date from(String text) {
        try {
            return DateParser.INSTANCE.parse(text);
        } catch (DateParseException e) {
            e.printStackTrace();
            throw new DeveloperError("Invalid date stored to database. [" + text + "]");
        }
    }

    private static Optional<Long> getDeviceEventIdFrom(Cursor cursor) {
        int eventId = cursor.getColumnIndexOrThrow(PeopleEvents.DEVICE_EVENT_ID);
        long deviceEventId = cursor.getLong(eventId);
        if (isALegitEventId(deviceEventId)) {
            return Optional.absent();
        }
        return new Optional<>(deviceEventId);
    }

    private static boolean isALegitEventId(long deviceEventId) {
        return deviceEventId == -1;
    }

    public List<ContactEvent> getEventsFor(Contact contact) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        return contactEvents;
    }
}
