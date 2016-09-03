package com.alexstyl.specialdates.events.namedays;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.ContentValuesMarshaller;
import com.alexstyl.specialdates.events.EventType;
import com.alexstyl.specialdates.events.PeopleEventsPersister;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendarProvider;
import com.alexstyl.specialdates.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamedayDatabaseRefresher {

    private static final Optional<Contact> NO_CONTACT = Optional.absent();

    private final ContentResolver contentResolver;
    private final ContactProvider contactProvider;
    private final NamedayCalendarProvider namedayCalendarProvider;
    private final NamedayPreferences namedayPreferences;
    private final PeopleEventsPersister perister;
    private final ContentValuesMarshaller eventMarshaller;

    public static NamedayDatabaseRefresher newInstance(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        NamedayCalendarProvider namedayProvider = NamedayCalendarProvider.newInstance(context);
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        ContactProvider contactProvider = ContactProvider.get(context);
        ContentValuesMarshaller marshaller = new ContentValuesMarshaller();
        PeopleEventsPersister persister = new PeopleEventsPersister(new EventSQLiteOpenHelper(context));
        return new NamedayDatabaseRefresher(contentResolver, namedayProvider, namedayPreferences, persister, contactProvider, marshaller);
    }

    NamedayDatabaseRefresher(ContentResolver contentResolver,
                             NamedayCalendarProvider namedayCalendarProvider,
                             NamedayPreferences namedayPreferences,
                             PeopleEventsPersister databaseProvider, ContactProvider contactProvider,
                             ContentValuesMarshaller eventMarshaller) {
        this.contentResolver = contentResolver;
        this.namedayCalendarProvider = namedayCalendarProvider;
        this.namedayPreferences = namedayPreferences;
        this.perister = databaseProvider;
        this.contactProvider = contactProvider;
        this.eventMarshaller = eventMarshaller;
    }

    public void refreshNamedaysIfEnabled() {
        perister.deleteAllNamedays();
        if (namedaysEnabled()) {
            initialiseNamedays();
        }
    }

    private void initialiseNamedays() {
        List<ContactEvent> namedays = loadDeviceStaticNamedays();
        storeNamedaysToDisk(namedays);

        List<ContactEvent> specialNamedays = loadSpecialNamedays();
        storeSpecialNamedaysToDisk(specialNamedays);
    }

    private List<ContactEvent> loadDeviceStaticNamedays() {
        List<ContactEvent> namedayEvents = new ArrayList<>();
        Cursor cursor = DeviceContactsQuery.query(contentResolver);
        if (isInvalidCursor(cursor)) {
            return namedayEvents;
        }

        Set<Long> contactIDs = new HashSet<>();

        while (cursor.moveToNext()) {
            long id = DeviceContactsQuery.getID(cursor);

            if (contactIDs.contains(id)) {
                continue;
            }
            contactIDs.add(id);
            Optional<Contact> contact = getDeviceContactWithId(id);
            if (!contact.isPresent()) {
                continue;
            }

            DisplayName displayName = DisplayName.from(getDisplayName(cursor));
            HashSet<Nameday> namedays = new HashSet<>();
            for (String firstName : displayName.getFirstNames()) {
                NameCelebrations nameDays = getNamedaysOf(firstName);
                if (nameDays.containsNoDate()) {
                    continue;
                }
                int namedaysCount = nameDays.size();
                for (int i = 0; i < namedaysCount; i++) {
                    DayDate date = nameDays.getDate(i);
                    Nameday nameday = new Nameday(date);
                    if (namedays.contains(nameday)) {
                        continue;
                    }
                    ContactEvent event = ContactEvent.newInstance(EventType.NAMEDAY, date, contact.get());
                    namedayEvents.add(event);
                    namedays.add(nameday);
                }
            }
        }

        cursor.close();
        return namedayEvents;
    }

    private List<ContactEvent> loadSpecialNamedays() {
        List<ContactEvent> namedayEvents = new ArrayList<>();
        Cursor cursor = DeviceContactsQuery.query(contentResolver);
        if (isInvalidCursor(cursor)) {
            return namedayEvents;
        }

        Set<Long> contactIDs = new HashSet<>();

        while (cursor.moveToNext()) {
            long id = DeviceContactsQuery.getID(cursor);

            if (contactIDs.contains(id)) {
                continue;
            }
            contactIDs.add(id);

            Optional<Contact> contact = getDeviceContactWithId(id);
            if (!contact.isPresent()) {
                continue;
            }

            DisplayName displayName = DisplayName.from(getDisplayName(cursor));

            for (String firstName : displayName.getFirstNames()) {
                NameCelebrations nameDays = getSpecialNamedaysOf(firstName);
                if (nameDays.containsNoDate()) {
                    continue;
                }

                int namedaysCount = nameDays.size();
                for (int i = 0; i < namedaysCount; i++) {
                    DayDate date = nameDays.getDate(i);
                    ContactEvent nameday = ContactEvent.newInstance(EventType.NAMEDAY, date, contact.get());
                    namedayEvents.add(nameday);
                }
            }
        }

        cursor.close();
        return namedayEvents;
    }

    private Optional<Contact> getDeviceContactWithId(long id) {
        try {
            Contact contact = contactProvider.getOrCreateContact(id);
            return new Optional<>(contact);
        } catch (ContactNotFoundException e) {
            return NO_CONTACT;
        }
    }

    private boolean isInvalidCursor(Cursor cursor) {
        return cursor == null;
    }

    private String getDisplayName(Cursor cursor) {
        return cursor.getString(DeviceContactsQuery.DISPLAY_NAME);
    }

    private void storeNamedaysToDisk(List<ContactEvent> namedays) {
        ContentValues[] values = eventMarshaller.marshall(namedays);
        perister.insertAnnualEvents(values);
    }

    private void storeSpecialNamedaysToDisk(List<ContactEvent> specialNamedays) {
        ContentValues[] values = eventMarshaller.marshall(specialNamedays);
        perister.insertDynamicEvents(values);
    }

    private NameCelebrations getNamedaysOf(String given) {
        NamedayCalendar namedayCalendar = getNamedayCalendar();
        return namedayCalendar.getNormalNamedaysFor(given);
    }

    private NameCelebrations getSpecialNamedaysOf(String firstName) {
        NamedayCalendar namedayCalendar = getNamedayCalendar();
        return namedayCalendar.getSpecialNamedaysFor(firstName);
    }

    public NamedayCalendar getNamedayCalendar() {
        NamedayLocale locale = namedayPreferences.getSelectedLanguage();
        int todayYear = DayDate.today().getYear();
        return namedayCalendarProvider.loadNamedayCalendarForLocale(locale, todayYear);
    }

    private boolean namedaysEnabled() {
        return namedayPreferences.isEnabled();
    }

    private static class DeviceContactsQuery {

        public final static Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;

        public static final String WHERE =
                ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.Data.DISPLAY_NAME + " NOT NULL"
                        + " AND " + ContactsContract.Data.IN_VISIBLE_GROUP + "=1";

        public static final String[] WHERE_ARGS = {
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE // given names
        };

        @SuppressLint("InlinedApi")
        public final static String SORT_ORDER =
                Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                        : ContactsContract.Contacts.DISPLAY_NAME;

        @SuppressLint("InlinedApi")
        public static final String[] PROJECTION = {
                ContactsContract.Data.CONTACT_ID,
                Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME,
        };

        public static final int ID = 0;

        public static final int DISPLAY_NAME = 1;

        public static Cursor query(ContentResolver cr) {
            return cr.query(CONTENT_URI, PROJECTION, WHERE, WHERE_ARGS, SORT_ORDER);
        }

        public static long getID(Cursor cursor) {
            return cursor.getLong(DeviceContactsQuery.ID);
        }

    }

    private class Nameday {

        private final Date date;

        public Nameday(Date date) {
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Nameday nameday = (Nameday) o;

            return date != null ? date.equals(nameday.date) : nameday.date == null;

        }

        @Override
        public int hashCode() {
            return date != null ? date.hashCode() : 0;
        }

    }

}
