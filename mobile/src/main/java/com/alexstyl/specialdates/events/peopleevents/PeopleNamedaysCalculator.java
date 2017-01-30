package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.AndroidContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.date.TimePeriod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class PeopleNamedaysCalculator {

    private static final Optional<Long> NO_DEVICE_EVENT_ID = Optional.absent();
    private final NamedayPreferences namedayPreferences;
    private final NamedayCalendarProvider namedayCalendarProvider;
    private final AndroidContactsProvider contactsProvider;

    public PeopleNamedaysCalculator(NamedayPreferences namedayPreferences,
                                    NamedayCalendarProvider namedayCalendarProvider,
                                    AndroidContactsProvider contactsProvider) {
        this.namedayPreferences = namedayPreferences;
        this.namedayCalendarProvider = namedayCalendarProvider;
        this.contactsProvider = contactsProvider;
    }

    public List<ContactEvent> loadDeviceStaticNamedays() {
        List<ContactEvent> namedayEvents = new ArrayList<>();
        List<Contact> contacts = contactsProvider.fetchAllDeviceContacts();
        for (Contact contact : contacts) {
            DisplayName displayName = contact.getDisplayName();
            HashSet<Date> namedays = new HashSet<>();
            for (String firstName : displayName.getFirstNames()) {
                NameCelebrations nameDays = getNamedaysOf(firstName);
                if (nameDays.containsNoDate()) {
                    continue;
                }
                int namedaysCount = nameDays.size();
                for (int i = 0; i < namedaysCount; i++) {
                    Date date = nameDays.getDate(i);
                    if (namedays.contains(date)) {
                        continue;
                    }
                    ContactEvent event = new ContactEvent(NO_DEVICE_EVENT_ID, StandardEventType.NAMEDAY, date, contact);
                    namedayEvents.add(event);
                    namedays.add(date);
                }
            }
        }

        return namedayEvents;
    }

    public List<ContactEvent> loadSpecialNamedaysBetween(TimePeriod timeDuration) {
        List<ContactEvent> namedayEvents = new ArrayList<>();
        for (Contact contact : contactsProvider.fetchAllDeviceContacts()) {
            for (String firstName : contact.getDisplayName().getFirstNames()) {
                NameCelebrations nameDays = getSpecialNamedaysOf(firstName);
                if (nameDays.containsNoDate()) {
                    continue;
                }

                int namedaysCount = nameDays.size();
                for (int i = 0; i < namedaysCount; i++) {
                    Date date = nameDays.getDate(i);
                    if (timeDuration.containsDate(date)) {
                        ContactEvent nameday = new ContactEvent(NO_DEVICE_EVENT_ID, StandardEventType.NAMEDAY, date, contact);
                        namedayEvents.add(nameday);
                    }
                }
            }
        }
        return namedayEvents;
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
        return namedayCalendarProvider.loadNamedayCalendarForLocale(locale, Date.CURRENT_YEAR);
    }
}
