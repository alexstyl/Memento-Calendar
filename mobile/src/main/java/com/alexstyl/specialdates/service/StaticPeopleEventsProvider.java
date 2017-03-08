package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.SQLArgumentBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.database.EventColumns;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.novoda.notils.caster.Classes.from;

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
    };

    private final ContentResolver resolver;
    private final ContactsProvider contactsProvider;
    private final CustomEventProvider customEventProvider;

    StaticPeopleEventsProvider(ContentResolver resolver, ContactsProvider contactsProvider, CustomEventProvider customEventProvider) {
        this.resolver = resolver;
        this.contactsProvider = contactsProvider;
        this.customEventProvider = customEventProvider;
    }

    ContactEventsOnADate fetchEventsOn(Date date) {
        return ContactEventsOnADate.createFrom(date, fetchEventsBetween(TimePeriod.between(date, date)));
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

    private Cursor queryEventsFor(TimePeriod timeDuration) {
        if (isWithinTheSameYear(timeDuration)) {
            return queryPeopleEvents(timeDuration, PeopleEventsContract.PeopleEvents.DATE + " ASC");
        } else {
            return queryForBothYearsIn(timeDuration);
        }
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

    private Cursor queryForBothYearsIn(TimePeriod timeDuration) {
        TimePeriod firstHalf = firstHalfOf(timeDuration);
        Cursor[] cursors = new Cursor[2];
        cursors[0] = queryPeopleEvents(firstHalf, PeopleEventsContract.PeopleEvents.DATE + " ASC");
        TimePeriod secondHalf = secondHalfOf(timeDuration);
        cursors[1] = queryPeopleEvents(secondHalf, PeopleEventsContract.PeopleEvents.DATE + " ASC");
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

    Optional<Date> findClosestStaticEventDateFrom(Date date) {
        Cursor cursor = queryDateClosestTo(date);
        try {
            if (cursor.moveToFirst()) {
                Date closestDate = getDateFrom(cursor);
                return new Optional<>(closestDate);
            }
            return Optional.absent();
        } finally {
            cursor.close();
        }
    }

    private Cursor queryDateClosestTo(Date date) {
        // select * from annual_events WHERE substr(date,3) >= '03-04' ORDER BY substr(date,3) asc LIMIT 1
        return resolver.query(
                PEOPLE_EVENTS,
                PEOPLE_PROJECTION,
                substr(PeopleEventsContract.PeopleEvents.DATE) + " >= ?",
                monthAndDayOf(date),
                substr(PeopleEventsContract.PeopleEvents.DATE) + " ASC LIMIT 1"
        );
    }

    private String substr(String datetete) {
        return "substr(" + datetete + ",3) ";
    }

    private String[] monthAndDayOf(Date date) {
        return new String[]{
                DateDisplayStringCreator.INSTANCE.stringOfNoYear(date)
        };
    }

    private static Date getDateFrom(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DATE);
        String text = cursor.getString(index);
        return from(text);
    }

    private static long getContactIdFrom(Cursor cursor) {
        int contactIdIndex = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.CONTACT_ID);
        return cursor.getLong(contactIdIndex);
    }

    private EventType getEventType(Cursor cursor) {
        int eventTypeIndex = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.EVENT_TYPE);
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

    private ContactEvent getContactEventFrom(Cursor cursor) throws ContactNotFoundException {
        long contactId = getContactIdFrom(cursor);
        Contact contact = contactsProvider.getOrCreateContact(contactId);
        Date date = getDateFrom(cursor);
        EventType eventType = getEventType(cursor);

        Optional<Long> eventId = getDeviceEventIdFrom(cursor);
        return new ContactEvent(eventId, eventType, date, contact);
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
