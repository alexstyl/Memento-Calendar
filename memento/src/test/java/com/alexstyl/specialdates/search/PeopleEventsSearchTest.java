package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.TestContactEventsBuilder;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactFixture;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.peopleevents.CompositePeopleEventsProvider;

import java.util.ArrayList;
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

    private static final Contact ALEX = ContactFixture.aContactCalled("Alex Styl");
    private static final Contact MARIA = ContactFixture.aContactCalled("Maria Papadopoulou");
    private static final Contact MIMOZA = ContactFixture.aContactCalled("Mimoza Dereks");
    private static final Date JANUARY_1st = Date.Companion.startOfYear(2016);

    private PeopleEventsSearch search;
    @Mock
    private CompositePeopleEventsProvider mockProvider;

    @Before
    public void setUp() {
        search = new PeopleEventsSearch(mockProvider, NameMatcher.INSTANCE);
        List<ContactEvent> contactEvents = new TestContactEventsBuilder()
                .addBirthdayFor(ALEX, JANUARY_1st)
                .addAnniversaryFor(MARIA, JANUARY_1st)
                .addNamedayFor(MIMOZA, JANUARY_1st)
                .build();

        when(mockProvider.getContactEventsFor(aYearFromNow())).thenReturn(contactEvents);
    }

    private static TimePeriod aYearFromNow() {
        Date today = Date.Companion.today();
        Date aYearFromNow = today.addDay(364);
        return TimePeriod.Companion.between(today, aYearFromNow);
    }

    @Test
    public void searchingByFirstLetter() {
        List<ContactWithEvents> actual = search.searchForContacts("A", 5);
        ContactWithEvents expected = new ContactWithEvents(ALEX, singletonList(new ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)));

        assertThat(actual).containsOnly(expected);
    }

    @Test
    public void searchingByLastLetter() {
        List<ContactWithEvents> actual = search.searchForContacts("S", 5);
        ContactWithEvents expected = new ContactWithEvents(ALEX, singletonList(new ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)));

        assertThat(actual).containsOnly(expected);
    }

    @Test
    public void searchingByFullName() {
        List<ContactWithEvents> actual = search.searchForContacts("Alex Styl", 5);
        ContactWithEvents expected = new ContactWithEvents(ALEX, singletonList(new ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)));

        assertThat(actual).containsOnly(expected);
    }

    @Test
    public void multipleResults() {
        List<ContactWithEvents> actual = search.searchForContacts("M", 5);

        List<ContactWithEvents> expected = new ArrayList<>();
        expected.add(new ContactWithEvents(MIMOZA, new ContactEventTestBuilder(MIMOZA).buildNameday((JANUARY_1st))));
        expected.add(new ContactWithEvents(MARIA, new ContactEventTestBuilder(MARIA).buildAnniversary((JANUARY_1st))));

        assertThat(actual).containsAll(expected);
    }

    @Test
    public void requestOneResultReturnsOnlyOneResult() {
        List<ContactWithEvents> actual = search.searchForContacts("M", 1);

        List<ContactWithEvents> expected = new ArrayList<>();
        expected.add(new ContactWithEvents(MARIA, new ContactEventTestBuilder(MARIA).buildAnniversary((JANUARY_1st))));

        assertThat(actual).containsAll(expected);
    }
}
