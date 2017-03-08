package com.alexstyl.specialdates.service;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.TestContactEventsBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateConstants;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;
import com.novoda.notils.logger.simple.Log;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleEventsProviderTest {

    private static final Contact PETER = new TestContact(1, DisplayName.from("Peter"));

    @Mock
    private ContactsProvider mockContactsProvider;
    @Mock
    private NamedayPreferences mockNamedaysPreferences;
    @Mock
    private CustomEventProvider customEventProvider;
    @Mock
    private StaticPeopleEventsProvider mockStaticEventProvider;

    private PeopleEventsProvider peopleEventsProvider;

    @Before
    public void setUp() {
        Log.setShowLogs(false);
        peopleEventsProvider = new PeopleEventsProvider(
                mockContactsProvider,
                mockNamedaysPreferences,
                new PeopleNamedaysCalculator(
                        mockNamedaysPreferences,
                        mock(NamedayCalendarProvider.class),
                        mockContactsProvider
                ),
                mockStaticEventProvider
        );
    }

    @Test
    public void staticEventsAreReturnedCorrectly() {
        Date date = Date.on(1, DateConstants.JANUARY, 2017);
        List<ContactEvent> staticEvents = new TestContactEventsBuilder().addAnniversaryFor(PETER, date).build();

        when(mockStaticEventProvider.fetchEventsBetween(TimePeriod.between(date, date))).thenReturn(staticEvents);

        List<ContactEvent> events = peopleEventsProvider.getCelebrationDateOn(date);
        assertThat(events).containsOnly(staticEvents.get(0));
    }
}
