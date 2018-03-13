package com.alexstyl.specialdates.events.peopleevents;

import android.database.Cursor;
import android.database.MergeCursor;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.SQLArgumentBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.Contacts;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alexstyl.specialdates.events.database.EventTypeId.TYPE_CUSTOM;

class AndroidPeopleEventsProvider implements PeopleEventsProvider {

    private static final String DATE_FROM = "substr(" + AnnualEventsContract.DATE + ",-5) >= ?";
    private static final String DATE_TO = "substr(" + AnnualEventsContract.DATE + ",-5) <= ?";
    private static final String DATE_BETWEEN_IGNORING_YEAR = DATE_FROM + " AND " + DATE_TO + " AND " + AnnualEventsContract.VISIBLE + " == 1";
    private static final String[] PEOPLE_PROJECTION = {AnnualEventsContract.DATE};
    //    private static final Uri PEOPLE_EVENTS = PeopleEventsContract.PeopleEvents.CONTENT_URI;
    private static final String[] PROJECTION = {
            AnnualEventsContract.CONTACT_ID,
            AnnualEventsContract.DEVICE_EVENT_ID,
            AnnualEventsContract.DATE,
            AnnualEventsContract.EVENT_TYPE,
            AnnualEventsContract.SOURCE,
    };

    /*
        We use this column in order to be able to do comparisons of dates, without having to worry about the year
        So, instead of a full date 1990-12-19, this will return 12-19. Similarwise for --12-19.

        an example in use: select * from annual_events WHERE substr(date,-5) >= '03-04' ORDER BY substr(date,-5) asc LIMIT 1
     */
    private static final String DATE_COLUMN_WITHOUT_YEAR = "substr(" + AnnualEventsContract.DATE + ", -5) ";

    private final EventSQLiteOpenHelper eventSQLHelper;
    private final ContactsProvider contactsProvider;
    private final CustomEventProvider customEventProvider;
    private final CrashAndErrorTracker tracker;

    AndroidPeopleEventsProvider(EventSQLiteOpenHelper sqLiteOpenHelper,
                                ContactsProvider contactsProvider,
                                CustomEventProvider customEventProvider,
                                CrashAndErrorTracker tracker) {
        this.eventSQLHelper = sqLiteOpenHelper;
        this.contactsProvider = contactsProvider;
        this.customEventProvider = customEventProvider;
        this.tracker = tracker;
    }

    @Override
    public ContactEventsOnADate fetchEventsOn(Date date) {
        return ContactEventsOnADate.Companion.createFrom(date, fetchEventsBetween(TimePeriod.Companion.between(date, date)));
    }

    @Override
    public List<ContactEvent> fetchEventsBetween(TimePeriod timePeriod) {
        Cursor cursor = queryEventsFor(timePeriod);
        List<ContactEvent> contactEvents = new ArrayList<>(cursor.getCount());

        List<Long> deviceIds = new ArrayList<>(cursor.getCount());
        List<Long> facebookIds = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
            long contactId = getContactIdFrom(cursor);
            int source = getContactSourceFrom(cursor);

            switch (source) {
                case ContactSource.SOURCE_DEVICE:
                    deviceIds.add(contactId);
                    break;
                case ContactSource.SOURCE_FACEBOOK:
                    facebookIds.add(contactId);
                    break;
                default:
                    throw new UnsupportedOperationException("Source " + source + " not managed");
            }
        }

        Contacts deviceContacts = contactsProvider.getContacts(deviceIds, ContactSource.SOURCE_DEVICE);
        Contacts facebookContacts = contactsProvider.getContacts(facebookIds, ContactSource.SOURCE_FACEBOOK);
        Map<Integer, Contacts> contacts = new HashMap<>();
        contacts.put(ContactSource.SOURCE_DEVICE, deviceContacts);
        contacts.put(ContactSource.SOURCE_FACEBOOK, facebookContacts);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            try {
                Contacts contactsOfSource = contacts.get(getContactSourceFrom(cursor));
                Contact contact = contactsOfSource.getContact(getContactIdFrom(cursor));
                if (contact != null) {
                    ContactEvent contactEvent = getContactEventFrom(cursor, contact);
                    contactEvents.add(contactEvent);
                }
            } catch (ContactNotFoundException e) {
                tracker.track(e);
            }
        }
        cursor.close();
        return Collections.unmodifiableList(contactEvents);
    }

    @Override
    public List<ContactEvent> fetchEventsFor(Contact contact) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        Cursor cursor = queryEventsOf(contact);
        while (cursor.moveToNext()) {
            try {
                ContactEvent contactEvent = getContactEventFrom(cursor, contact);
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
            return queryPeopleEvents(timeDuration, AnnualEventsContract.DATE + " ASC");
        } else {
            return queryAllYearsIn(timeDuration);
        }
    }

    private Cursor queryEventsOf(Contact contact) {
        String[] selectArgs = {
                String.valueOf(contact.getContactID()),
                String.valueOf(contact.getSource())
        };

        // query database
        return eventSQLHelper.getReadableDatabase().query(
                AnnualEventsContract.TABLE_NAME,
                PROJECTION,
                AnnualEventsContract.CONTACT_ID + " = ? "
                        + "AND " + AnnualEventsContract.SOURCE + " = ?",
                selectArgs,
                null, null, null
        );
    }

    private Cursor queryPeopleEvents(TimePeriod timePeriod, String sortOrder) {
        String[] selectArgs = {
                SQLArgumentBuilder.dateWithoutYear(timePeriod.getStartingDate()),
                SQLArgumentBuilder.dateWithoutYear(timePeriod.getEndingDate()),
        };

        return eventSQLHelper.getReadableDatabase().query(
                AnnualEventsContract.TABLE_NAME,
                PROJECTION,
                DATE_BETWEEN_IGNORING_YEAR,
                selectArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor queryAllYearsIn(TimePeriod timeDuration) {
        TimePeriod firstHalf = firstHalfOf(timeDuration);
        Cursor[] cursors = new Cursor[2];
        cursors[0] = queryPeopleEvents(firstHalf, AnnualEventsContract.DATE + " ASC");
        TimePeriod secondHalf = secondHalfOf(timeDuration);
        cursors[1] = queryPeopleEvents(secondHalf, AnnualEventsContract.DATE + " ASC");
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

    @NonNull
    @Override
    public Date findClosestEventDateOnOrAfter(@NonNull Date date) throws NoEventsFoundException {
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
        return eventSQLHelper.getReadableDatabase().query(
                AnnualEventsContract.TABLE_NAME,
                PEOPLE_PROJECTION,
                DATE_COLUMN_WITHOUT_YEAR + " >= ?",
                monthAndDayOf(date),
                null,
                null,
                DATE_COLUMN_WITHOUT_YEAR + " ASC LIMIT 1"
        );
    }

    private String[] monthAndDayOf(Date date) {
        return new String[]{
                ShortDateLabelCreator.INSTANCE.createLabelWithNoYearFor(date)
        };
    }

    private static Date getDateFrom(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(AnnualEventsContract.DATE);
        String rawDate = cursor.getString(index);
        try {
            return DateParser.INSTANCE.parse(rawDate);
        } catch (DateParseException e) {
            throw new DeveloperError("Invalid date stored to database. [" + rawDate + "]");
        }
    }

    private EventType getEventType(Cursor cursor) {
        int eventTypeIndex = cursor.getColumnIndexOrThrow(AnnualEventsContract.EVENT_TYPE);
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

    private ContactEvent getContactEventFrom(Cursor cursor, Contact contact) throws ContactNotFoundException {
        Date date = getDateFrom(cursor);
        EventType eventType = getEventType(cursor);

        Optional<Long> eventId = getDeviceEventIdFrom(cursor);
        return new ContactEvent(eventId, eventType, date, contact);
    }

    private static long getContactIdFrom(Cursor cursor) {
        int contactIdIndex = cursor.getColumnIndexOrThrow(AnnualEventsContract.CONTACT_ID);
        return cursor.getLong(contactIdIndex);
    }

    @ContactSource
    @SuppressWarnings("WrongConstant")
    private int getContactSourceFrom(Cursor cursor) {
        int sourceTypeIdex = cursor.getColumnIndexOrThrow(AnnualEventsContract.SOURCE);
        return cursor.getInt(sourceTypeIdex);
    }

    private static Optional<Long> getDeviceEventIdFrom(Cursor cursor) {
        int eventId = cursor.getColumnIndexOrThrow(AnnualEventsContract.DEVICE_EVENT_ID);
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
