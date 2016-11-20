package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.TestContactEventBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.TimePeriod;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleEventsSearchTest {

    private static final Contact ALEX = new TestContact(1, DisplayName.from("Alex Styl"));
    private static final Contact MARIA = new TestContact(1, DisplayName.from("Maria Papadopoulou"));
    private static final Contact MIMOZA = new TestContact(1, DisplayName.from("Mimoza Dereks"));
    private static final Date JANUARY_1st = Date.startOfTheYear(2016);

    private PeopleEventsSearch search;
    @Mock
    private PeopleEventsProvider mockProvider;

    @Before
    public void setUp() {
        search = new PeopleEventsSearch(mockProvider, NameMatcher.INSTANCE);
        List<ContactEvent> contactEvents = new TestContactEventBuilder()
                .addBirthdayFor(ALEX, JANUARY_1st)
                .addAnniversaryFor(MARIA, JANUARY_1st)
                .addNamedayFor(MIMOZA, JANUARY_1st)
                .build();

        when(mockProvider.getCelebrationDateFor(aYearFromNow())).thenReturn(contactEvents);
    }

    private TimePeriod aYearFromNow() {
        Date today = Date.today();
        Date aYearFromNow = today.addDay(364);
        return TimePeriod.between(today, aYearFromNow);
    }

    @Test
    public void searchResultsIncludeOnlyTheSearchedContact() {
        List<ContactWithEvents> actual = search.searchForContacts("Alex", 5);
        ContactWithEvents expected = new ContactWithEvents(ALEX, singletonList(new ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)));

        assertThat(actual).containsOnly(expected);
    }
}
