package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.Dates;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PeopleNamedaysCalculator {

    private final NamedayPreferences namedayPreferences;
    private final NamedayCalendarProvider namedayCalendarProvider;
    private final ContactsProvider contactsProvider;

    public PeopleNamedaysCalculator(
            NamedayPreferences namedayPreferences,
            NamedayCalendarProvider namedayCalendarProvider,
            ContactsProvider contactsProvider) {
        this.namedayPreferences = namedayPreferences;
        this.namedayCalendarProvider = namedayCalendarProvider;
        this.contactsProvider = contactsProvider;
    }

    public List<ContactEvent> loadDeviceStaticNamedays() {
        List<ContactEvent> namedayEvents = new ArrayList<>();
        List<Contact> contacts = contactsProvider.getAllContacts();
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
                    ContactEvent event = new ContactEvent(new Optional<>(contact.getContactID()), StandardEventType.NAMEDAY, date, contact);
                    namedayEvents.add(event);
                    namedays.add(date);
                }
            }
        }

        return namedayEvents;
    }

    public ContactEventsOnADate loadSpecialNamedaysOn(Date date) {
        List<ContactEvent> contactEvents = loadSpecialNamedaysBetween(TimePeriod.Companion.between(date, date));
        return ContactEventsOnADate.createFrom(date, contactEvents);
    }

    public List<ContactEvent> loadSpecialNamedaysBetween(TimePeriod timePeriod) {
        List<ContactEvent> namedayEvents = new ArrayList<>();
        for (Contact contact : contactsProvider.getAllContacts()) {
            for (String firstName : contact.getDisplayName().getFirstNames()) {
                NameCelebrations nameDays = getSpecialNamedaysOf(firstName);
                if (nameDays.containsNoDate()) {
                    continue;
                }

                int namedaysCount = nameDays.size();
                for (int i = 0; i < namedaysCount; i++) {
                    Date date = nameDays.getDate(i);
                    if (timePeriod.containsDate(date)) {
                        ContactEvent nameday = new ContactEvent(new Optional<>(contact.getContactID()), StandardEventType.NAMEDAY, date, contact);
                        namedayEvents.add(nameday);
                    }
                }
            }
        }
        return Collections.unmodifiableList(namedayEvents);
    }

    public List<ContactEvent> loadSpecialNamedaysFor(Contact contact) {
        List<ContactEvent> namedays = new ArrayList<>();
        for (String name : contact.getDisplayName().getAllNames()) {
            NameCelebrations specialCelebration = getSpecialNamedaysOf(name);
            Dates specialDates = specialCelebration.getDates();
            for (int i = 0; i < specialDates.size(); i++) {
                Date date = specialDates.getDate(i);
                Optional<Long> deviceEventId = new Optional<>(contact.getContactID());
                ContactEvent contactEvent = new ContactEvent(deviceEventId, StandardEventType.NAMEDAY, date, contact);
                namedays.add(contactEvent);
            }
        }
        return namedays;
    }

    private NameCelebrations getNamedaysOf(String given) {
        NamedayCalendar namedayCalendar = getNamedayCalendar();
        return namedayCalendar.getNormalNamedaysFor(given);
    }

    NameCelebrations getSpecialNamedaysOf(String firstName) {
        NamedayCalendar namedayCalendar = getNamedayCalendar();
        return namedayCalendar.getSpecialNamedaysFor(firstName);
    }

    private NamedayCalendar getNamedayCalendar() {
        NamedayLocale locale = namedayPreferences.getSelectedLanguage();
        return namedayCalendarProvider.loadNamedayCalendarForLocale(locale, Date.Companion.getCURRENT_YEAR());
    }
}
