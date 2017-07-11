package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.TestNamedayCalendarBuilder;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.date.TimePeriod;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleNamedaysCalculatorTest {

    private PeopleNamedaysCalculator calculator;

    private static final NamedayLocale LOCALE = NamedayLocale.GREEK;

    private static final int YEAR = 2016;
    @Mock
    private NamedayCalendarProvider namedayCalendarProvider;
    @Mock
    private NamedayPreferences mockPreferences;
    @Mock
    private ContactsProvider mockContactsProvider;
    private final TestContact EASTER_CELEBRATING_CONTACT = new TestContact(1, DisplayName.from("Λάμπρος"), getImagePath());

    @Before
    public void setUp() {
        Log.setShowLogs(false);
        NamedayCalendar namedayCalendar = new TestNamedayCalendarBuilder()
                .forLocale(LOCALE)
                .forYear(YEAR)
                .build();

        when(namedayCalendarProvider.loadNamedayCalendarForLocale(any(NamedayLocale.class), any(Integer.class))).thenReturn(namedayCalendar);
        when(mockPreferences.getSelectedLanguage()).thenReturn(LOCALE);
        calculator = new PeopleNamedaysCalculator(mockPreferences, namedayCalendarProvider, mockContactsProvider);

    }

    @Test
    public void gettingSpecialNamedaysOnSpecificDateOnlyReturnsTheEventsForThatDate() {
        List<Contact> testContacts = createSomeContacts();
        testContacts.add(EASTER_CELEBRATING_CONTACT);
        when(mockContactsProvider.getAllContacts()).thenReturn(testContacts);

        Date easterDate = OrthodoxEasterCalculator.INSTANCE.calculateEasterForYear(YEAR);
        List<ContactEvent> contactEvents = calculator.loadSpecialNamedaysBetween(TimePeriod.between(easterDate, easterDate));
        assertThat(contactEvents).hasSize(1);
        assertThat(contactEvents.get(0).getContact()).isEqualTo(EASTER_CELEBRATING_CONTACT);
    }

    private List<Contact> createSomeContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new TestContact(12, DisplayName.from("Αβδηρος"), getImagePath()));
        contacts.add(new TestContact(13, DisplayName.from("Αγις"), getImagePath()));
        return contacts;
    }

}
