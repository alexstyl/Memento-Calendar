package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.SQLArgumentBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alexstyl.specialdates.events.database.EventTypeId.TYPE_CUSTOM;

class StaticPeopleEventsProvider {

    private static final String DATE_FROM = "substr(" + PeopleEventsContract.PeopleEvents.DATE + ",-5) >= ?";
    private static final String DATE_TO = "substr(" + PeopleEventsContract.PeopleEvents.DATE + ",-5) <= ?";
    private static final String DATE_BETWEEN_IGNORING_YEAR = DATE_FROM + " AND " + DATE_TO;
    private static final String[] PEOPLE_PROJECTION = new String[]{PeopleEventsContract.PeopleEvents.DATE};
    private static final Uri PEOPLE_EVENTS = PeopleEventsContract.PeopleEvents.CONTENT_URI;
    private static final String[] PROJECTION = {
            PeopleEventsContract.PeopleEvents.CONTACT_ID,
            PeopleEventsContract.PeopleEvents.DEVICE_EVENT_ID,
            PeopleEventsContract.PeopleEvents.DATE,
            PeopleEventsContract.PeopleEvents.EVENT_TYPE,
            PeopleEventsContract.PeopleEvents.SOURCE,
    };

    /*
        We use this column in order to be able to do comparisons of dates, without having to worry about the year
        So, instead of a full date 1990-12-19, this will return 12-19. Similarwise for --12-19.

        an example in use: select * from annual_events WHERE substr(date,-5) >= '03-04' ORDER BY substr(date,-5) asc LIMIT 1
     */
    private static final String DATE_COLUMN_WITHOUT_YEAR = "substr(" + PeopleEventsContract.PeopleEvents.DATE + ", -5) ";

    private final ContentResolver resolver;
    private final ContactsProvider contactsProvider;
    private final CustomEventProvider customEventProvider;

    StaticPeopleEventsProvider(ContentResolver resolver, ContactsProvider contactsProvider, CustomEventProvider customEventProvider) {
        this.resolver = resolver;
        this.contactsProvider = contactsProvider;
        this.customEventProvider = customEventProvider;
    }

    ContactEventsOnADate fetchEventsOn(Date date) {
        return ContactEventsOnADate.createFrom(date, fetchEventsBetween(TimePeriod.Companion.between(date, date)));
    }

    List<ContactEvent> fetchEventsBetween(TimePeriod timeDuration) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        Cursor cursor = queryEventsFor(timeDuration);
        while (cursor.moveToNext()) {
            try {
                ContactEvent contactEvent = getContactEventFrom(cursor);
                contactEvents.add(contactEvent);
            } catch (ContactNotFoundException e) {
                Log.w(e);
            }
        }
        cursor.close();
        return Collections.unmodifiableList(contactEvents);
    }

    List<ContactEvent> fetchEventsFor(Contact contact) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        Cursor cursor = queryEventsOf(contact);
        while (cursor.moveToNext()) {
            try {
                ContactEvent contactEvent = getContactEventFrom(cursor);
                contactEvents.add(contactEvent);
            } catch (ContactNotFoundException e) {
                Log.w(e);
            }
        }
        cursor.close();
        return Collections.unmodifiableList(contactEvents);
    }

    private Cursor queryEventsFor(TimePeriod timeDuration) {
        if (isWithinTheSameYear(timeDuration)) {
            return queryPeopleEvents(timeDuration, PeopleEventsContract.PeopleEvents.DATE + " ASC");
        } else {
            return queryAllYearsIn(timeDuration);
        }
    }

    private Cursor queryEventsOf(Contact contact) {
        String[] selectArgs = new String[]{
                String.valueOf(contact.getContactID()),
                String.valueOf(contact.getSource())
        };

        return resolver.query(
                PeopleEventsContract.PeopleEvents.CONTENT_URI,
                PROJECTION,
                PeopleEventsContract.PeopleEvents.CONTACT_ID + " = ? " +
                        "AND " + PeopleEventsContract.PeopleEvents.SOURCE + " = ?",
                selectArgs,
                null
        );
    }

    private Cursor queryPeopleEvents(TimePeriod timePeriod, String sortOrder) {
        String[] selectArgs = new String[]{
                SQLArgumentBuilder.dateWithoutYear(timePeriod.getStartingDate()),
                SQLArgumentBuilder.dateWithoutYear(timePeriod.getEndingDate()),
        };

        return resolver.query(
                PeopleEventsContract.PeopleEvents.CONTENT_URI,
                PROJECTION,
                DATE_BETWEEN_IGNORING_YEAR,
                selectArgs,
                sortOrder
        );
    }

    private Cursor queryAllYearsIn(TimePeriod timeDuration) {
        TimePeriod firstHalf = firstHalfOf(timeDuration);
        Cursor[] cursors = new Cursor[2];
        cursors[0] = queryPeopleEvents(firstHalf, PeopleEventsContract.PeopleEvents.DATE + " ASC");
        TimePeriod secondHalf = secondHalfOf(timeDuration);
        cursors[1] = queryPeopleEvents(secondHalf, PeopleEventsContract.PeopleEvents.DATE + " ASC");
        return new MergeCursor(cursors);
    }

    private static TimePeriod firstHalfOf(TimePeriod timeDuration) {
        return TimePeriod.Companion.between(
                timeDuration.getStartingDate(),
                Date.Companion.endOfYear(timeDuration.getStartingDate().getYear())
        );
    }

    private static TimePeriod secondHalfOf(TimePeriod timeDuration) {
        return TimePeriod.Companion.between(
                Date.Companion.startOfYear(timeDuration.getEndingDate().getYear()),
                timeDuration.getEndingDate()
        );
    }

    private boolean isWithinTheSameYear(TimePeriod timeDuration) {
        return timeDuration.getStartingDate().getYear() == timeDuration.getEndingDate().getYear();
    }

    Date findClosestStaticEventDateFrom(Date date) throws NoEventsFoundException {
        Cursor cursor = queryDateClosestTo(date);
        try {
            if (cursor.moveToFirst()) {
                Date closestDate = getDateFrom(cursor);

                return Date.Companion.on(closestDate.getDayOfMonth(), closestDate.getMonth(),
                                         date.getYear()
                );
            }
            throw new NoEventsFoundException("No static even found after or on " + date);
        } finally {
            cursor.close();
        }
    }

    private Cursor queryDateClosestTo(Date date) {
        return resolver.query(
                PEOPLE_EVENTS,
                PEOPLE_PROJECTION,
                DATE_COLUMN_WITHOUT_YEAR + " >= ?",
                monthAndDayOf(date),
                DATE_COLUMN_WITHOUT_YEAR + " ASC LIMIT 1"
        );
    }

    private String[] monthAndDayOf(Date date) {
        return new String[]{
                DateDisplayStringCreator.INSTANCE.stringOfNoYear(date)
        };
    }

    private static Date getDateFrom(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DATE);
        String rawDate = cursor.getString(index);
        try {
            return DateParser.INSTANCE.parse(rawDate);
        } catch (DateParseException e) {
            e.printStackTrace();
            throw new DeveloperError("Invalid date stored to database. [" + rawDate + "]");
        }
    }

    private EventType getEventType(Cursor cursor) {
        int eventTypeIndex = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.EVENT_TYPE);
        @EventTypeId int rawEventType = cursor.getInt(eventTypeIndex);
        if (rawEventType == TYPE_CUSTOM) {
            Optional<Long> deviceEventIdFrom = getDeviceEventIdFrom(cursor);
            if (deviceEventIdFrom.isPresent()) {
                return queryCustomEvent(deviceEventIdFrom.get());
            }
            return StandardEventType.OTHER;
        }
        return StandardEventType.fromId(rawEventType);
    }

    private ContactEvent getContactEventFrom(Cursor cursor) throws ContactNotFoundException {
        long contactId = getContactIdFrom(cursor);
        int source = getContactSourceFrom(cursor);
        Contact contact = contactsProvider.getContact(contactId, source);
        Date date = getDateFrom(cursor);
        EventType eventType = getEventType(cursor);

        Optional<Long> eventId = getDeviceEventIdFrom(cursor);
        return new ContactEvent(eventId, eventType, date, contact);
    }

    private static long getContactIdFrom(Cursor cursor) {
        int contactIdIndex = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.CONTACT_ID);
        return cursor.getLong(contactIdIndex);
    }

    @ContactSource
    @SuppressWarnings("WrongConstant")
    private int getContactSourceFrom(Cursor cursor) {
        int sourceTypeIdex = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.SOURCE);
        return cursor.getInt(sourceTypeIdex);
    }

    private static Optional<Long> getDeviceEventIdFrom(Cursor cursor) {
        int eventId = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DEVICE_EVENT_ID);
        long deviceEventId = cursor.getLong(eventId);
        if (isALegitEventId(deviceEventId)) {
            return Optional.absent();
        }
        return new Optional<>(deviceEventId);
    }

    private static boolean isALegitEventId(long deviceEventId) {
        return deviceEventId == -1;
    }

    private EventType queryCustomEvent(long deviceId) {
        return customEventProvider.getEventWithId(deviceId);
    }
}
