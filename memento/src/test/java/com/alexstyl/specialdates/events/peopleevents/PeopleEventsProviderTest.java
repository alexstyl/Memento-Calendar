package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.TestContactEventsBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactFixture;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.Date.Companion;
import static com.alexstyl.specialdates.date.Months.JANUARY;
import static com.alexstyl.specialdates.date.Months.MARCH;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleEventsProviderTest {

    private static final Contact PETER = ContactFixture.aContactCalled("Peter");

    @Mock private NamedayUserSettings mockNamedaysPreferences;
    @Mock private PeopleStaticEventsProvider mockStaticEventProvider;
    @Mock private PeopleNamedaysCalculator mockPeopleNamedaysCalculator;

    private PeopleEventsProvider peopleEventsProvider;

    @Before
    public void setUp() {
        peopleEventsProvider = new PeopleEventsProvider(
                mockNamedaysPreferences,
                mockPeopleNamedaysCalculator,
                mockStaticEventProvider
        );
    }

    @Test
    public void staticEventsAreReturnedCorrectly() {
        Date date = Companion.on(1, JANUARY, 2017);
        List<ContactEvent> expectedEvents = new TestContactEventsBuilder().addAnniversaryFor(PETER, date).build();

        when(mockStaticEventProvider.fetchEventsBetween(TimePeriod.Companion.between(date, date))).thenReturn(expectedEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsOnly(expectedEvents.get(0));
    }

    @Test
    public void dynamicEventsAreReturnedCorrectly() {
        Date date = Date.Companion.on(1, JANUARY, 2017);
        List<ContactEvent> expectedEvents = new TestContactEventsBuilder().addNamedayFor(PETER, date).build();
        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);
        TimePeriod timePeriod = TimePeriod.Companion.between(date, date);
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysBetween(timePeriod)).thenReturn(expectedEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsOnly(expectedEvents.get(0));
    }

    @Test
    public void combinedEventsAreReturnedCorrectly() {
        Date date = Companion.on(1, JANUARY, 2017);
        List<ContactEvent> expectedDynamicEvents = new TestContactEventsBuilder().addNamedayFor(PETER, date).build();
        List<ContactEvent> expectedStaticEvents = new TestContactEventsBuilder().addAnniversaryFor(PETER, date).build();

        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysBetween(TimePeriod.Companion.between(date, date))).thenReturn(expectedDynamicEvents);
        when(mockStaticEventProvider.fetchEventsBetween(TimePeriod.Companion.between(date, date))).thenReturn(expectedStaticEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsAll(expectedDynamicEvents);
        assertThat(events).containsAll(expectedStaticEvents);
    }

    @Test
    public void neitherStaticNorDynamicEvents_returnsNoClosestEvent() throws NoEventsFoundException {
        when(mockStaticEventProvider.findClosestStaticEventDateFrom(any(Date.class))).thenThrow(NoEventsFoundException.class);
        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysOn(any(Date.class)))
                .thenReturn(ContactEventsOnADate.createFrom(Companion.on(1, JANUARY, 2017), Collections.<ContactEvent>emptyList()));

        Optional<ContactEventsOnADate> date = peopleEventsProvider.getCelebrationsClosestTo(Companion.on(1, JANUARY, 2017));
        assertThat(date.isPresent()).isFalse();

    }

    @Test
    public void onlyDynamicEvents_returnsTheDynamicEvents() throws NoEventsFoundException {
        when(mockStaticEventProvider.findClosestStaticEventDateFrom(Companion.on(2, MARCH, 2017))).thenThrow(NoEventsFoundException.class);
        when(mockNamedaysPreferences.isEnabled()).thenReturn(true);

        List<ContactEvent> expectedEvents = new TestContactEventsBuilder()
                .addNamedayFor(PETER, Companion.on(2, MARCH, 2017))
                .build();
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysOn(Companion.on(2, MARCH, 2017)))
                .thenReturn(ContactEventsOnADate.createFrom(Companion.on(2, MARCH, 2017), expectedEvents));
        when(mockPeopleNamedaysCalculator.loadSpecialNamedaysBetween(TimePeriod.Companion.between(Companion.on(2, MARCH, 2017), Date.Companion.endOfYear(2017))))
                .thenReturn(expectedEvents);

        Optional<ContactEventsOnADate> actualEvents = peopleEventsProvider.getCelebrationsClosestTo(Companion.on(2, MARCH, 2017));
        assertThat(actualEvents.get()).isEqualTo(ContactEventsOnADate.createFrom(Companion.on(2, MARCH, 2017), expectedEvents));
    }

}
