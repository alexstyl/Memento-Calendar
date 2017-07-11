package com.alexstyl.specialdates.service;

import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.TestContactEventsBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;
import com.novoda.notils.logger.simple.Log;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.Date.on;
import static com.alexstyl.specialdates.date.DateConstants.JANUARY;
import static com.alexstyl.specialdates.date.DateConstants.MARCH;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleEventsProviderTest {

    private static final Contact PETER = new TestContact(1, DisplayName.from("Peter"), getImagePath());

    @Mock
    private ContactsProvider mockContactsProvider;
    @Mock
    private NamedayPreferences mockNamedaysPreferences;
    @Mock
    private CustomEventProvider customEventProvider;
    @Mock
    private StaticPeopleEventsProvider mockStaticEventProvider;
    @Mock
    private NamedayCalendarProvider mockNamedayCalendarProvider;
    @Mock
    private PeopleNamedaysCalculator mockPeopleNamedaysCalculator;

    private PeopleEventsProvider peopleEventsProvider;

    @Before
    public void setUp() {
        Log.setShowLogs(false);
        peopleEventsProvider = new PeopleEventsProvider(
                mockNamedaysPreferences,
                mockPeopleNamedaysCalculator,
                mockStaticEventProvider
        );
    }

    @Test
    public void staticEventsAreReturnedCorrectly() {
        Date date = on(1, JANUARY, 2017);
        List<ContactEvent> expectedEvents = new TestContactEventsBuilder().addAnniversaryFor(PETER, date).build();

        when(mockStaticEventProvider.fetchEventsBetween(TimePeriod.between(date, date))).thenReturn(expectedEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsOnly(expectedEvents.get(0));
    }

    @Test
    public void dynamicEventsAreReturnedCorrectly() {
        Date date = on(1, JANUARY, 2017);
        List<ContactEvent> expectedEvents = new TestContactEventsBuilder().addNamedayFor(PETER, date).build();
        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysBetween(TimePeriod.between(date, date))).thenReturn(expectedEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsOnly(expectedEvents.get(0));
    }

    @Test
    public void combinedEventsAreReturnedCorrectly() {
        Date date = on(1, JANUARY, 2017);
        List<ContactEvent> expectedDynamicEvents = new TestContactEventsBuilder().addNamedayFor(PETER, date).build();
        List<ContactEvent> expectedStaticEvents = new TestContactEventsBuilder().addAnniversaryFor(PETER, date).build();

        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysBetween(TimePeriod.between(date, date))).thenReturn(expectedDynamicEvents);
        when(mockStaticEventProvider.fetchEventsBetween(TimePeriod.between(date, date))).thenReturn(expectedStaticEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsAll(expectedDynamicEvents);
        assertThat(events).containsAll(expectedStaticEvents);
    }

    @Test
    public void neitherStaticNorDynamicEvents_returnsNoClosestEvent() throws NoEventsFoundException {
        when(mockStaticEventProvider.findClosestStaticEventDateFrom(any(Date.class))).thenThrow(NoEventsFoundException.class);
        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysOn(any(Date.class)))
                .thenReturn(ContactEventsOnADate.createFrom(on(1, JANUARY, 2017), Collections.<ContactEvent>emptyList()));

        Optional<ContactEventsOnADate> date = peopleEventsProvider.getCelebrationsClosestTo(on(1, JANUARY, 2017));
        assertThat(date.isPresent()).isFalse();

    }

    @Test
    public void onlyDynamicEvents_returnsTheDynamicEvents() throws NoEventsFoundException {
        when(mockStaticEventProvider.findClosestStaticEventDateFrom(any(Date.class))).thenThrow(NoEventsFoundException.class);
        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);

        List<ContactEvent> expectedEvents = new TestContactEventsBuilder()
                .addNamedayFor(PETER, on(2, MARCH, 2017))
                .build();
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysOn(on(2, MARCH, 2017)))
                .thenReturn(ContactEventsOnADate.createFrom(on(2, MARCH, 2017), expectedEvents));

        Optional<ContactEventsOnADate> actualEvents = peopleEventsProvider.getCelebrationsClosestTo(on(2, MARCH, 2017));
        assertThat(actualEvents.get()).isEqualTo(ContactEventsOnADate.createFrom(on(2, MARCH, 2017), expectedEvents));
    }

}
