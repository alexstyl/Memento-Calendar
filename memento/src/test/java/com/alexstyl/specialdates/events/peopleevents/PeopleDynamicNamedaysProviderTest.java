package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactFixture;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.TestNamedayCalendarBuilder;

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
public class PeopleDynamicNamedaysProviderTest {

    private PeopleDynamicNamedaysProvider calculator;

    private static final NamedayLocale LOCALE = NamedayLocale.GREEK;
    private static final OrthodoxEasterCalculator calculator1 = new OrthodoxEasterCalculator();
    private final Contact EASTER_CELEBRATING_CONTACT = ContactFixture.aContactCalled("Λάμπρος");
    private static final int YEAR = 2016;
    @Mock
    private NamedayCalendarProvider namedayCalendarProvider;
    @Mock
    private NamedayUserSettings mockSettings;
    @Mock
    private ContactsProvider mockContactsProvider;

    @Before
    public void setUp() {
        NamedayCalendar namedayCalendar = new TestNamedayCalendarBuilder()
                .forLocale(LOCALE)
                .forYear(YEAR)
                .build();

        when(namedayCalendarProvider.loadNamedayCalendarForLocale(any(NamedayLocale.class), any(Integer.class))).thenReturn(namedayCalendar);
        when(mockSettings.getSelectedLanguage()).thenReturn(LOCALE);
        when(mockSettings.isEnabled()).thenReturn(true);
        calculator = new PeopleDynamicNamedaysProvider(mockSettings, namedayCalendarProvider, mockContactsProvider);

    }

    @Test
    public void gettingSpecialNamedaysOnSpecificDateOnlyReturnsTheEventsForThatDate() {
        List<Contact> testContacts = createSomeContacts();
        testContacts.add(EASTER_CELEBRATING_CONTACT);
        when(mockContactsProvider.getAllContacts()).thenReturn(testContacts);

        Date easterDate = calculator1.calculateEasterForYear(YEAR);
        List<ContactEvent> contactEvents = calculator.fetchEventsBetween(TimePeriod.Companion.between(easterDate, easterDate));
        assertThat(contactEvents).hasSize(1);
        assertThat(contactEvents.get(0).getContact()).isEqualTo(EASTER_CELEBRATING_CONTACT);
    }

    private List<Contact> createSomeContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(ContactFixture.aContactCalled("Αβδηρος"));
        contacts.add(ContactFixture.aContactCalled(("Αγις")));
        return contacts;
    }

}
