package com.alexstyl.specialdates.upcoming;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.DeviceContact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.DateConstants.*;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UpcomingRowViewModelsBuilderTest {

    private static final Date FEBRUARY_1st = Date.on(1, FEBRUARY, 1990);
    private static final Date FEBRUARY_3rd = Date.on(3, FEBRUARY, 1990);
    private static final Date MARCH_5th = Date.on(5, MARCH, 1990);
    private static final Optional<Long> NO_DEVICE_EVENT_ID = Optional.absent();

    private UpcomingRowViewModelsFactory upcomingRowViewModelsFactory;

    @Mock
    private ColorResources mockColorResources;
    @Mock
    private StringResources mockStringResources;

    @Before
    public void setUp() {
        Date today = Date.today();
        upcomingRowViewModelsFactory = new UpcomingRowViewModelsFactory(
                today,
                DateDisplayStringCreator.INSTANCE,
                new ContactViewModelFactory(mockColorResources, mockStringResources),
                mockStringResources,
                new BankHolidayViewModelFactory(),
                new NamedaysViewModelFactory(today)
        );

    }

    @Test
    public void celebrationDateIsCreatedCorrectlyForDifferentYear() {
        ContactEvent event = aContactEventOn(Date.on(1, JANUARY, 1990));
        TimePeriod duration = TimePeriod.between(Date.on(1, JANUARY, 2016), Date.on(1, DECEMBER, 2016));

        List<UpcomingRowViewModel> dates = new UpcomingRowViewModelsBuilder(duration, upcomingRowViewModelsFactory)
                .withContactEvents(singletonList(event))
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenASingleContactEvent_thenOneCelebrationDateIsCreated() {
        ContactEvent event = aContactEvent();
        List<ContactEvent> contactEvents = singletonList(event);

        TimePeriod duration = timeOf(event);

        List<UpcomingRowViewModel> dates = new UpcomingRowViewModelsBuilder(duration, upcomingRowViewModelsFactory)
                .withContactEvents(contactEvents)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenTwoContactEventsOnSameDate_thenOneCelebrationDateIsCreated() {
        List<ContactEvent> contactEventsList = Arrays.asList(
                aContactEventOn(FEBRUARY_1st),
                aContactEventOn(FEBRUARY_1st)
        );

        List<UpcomingRowViewModel> dates = new UpcomingRowViewModelsBuilder(TimePeriod.between(FEBRUARY_1st, FEBRUARY_1st), upcomingRowViewModelsFactory)
                .withContactEvents(contactEventsList)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenTwoContactEventsOnDifferentDate_thenTwoCelebrationDateAreCreated() {
        List<ContactEvent> contactEventsList = Arrays.asList(
                aContactEventOn(FEBRUARY_1st),
                aContactEventOn(FEBRUARY_3rd)
        );

        List<UpcomingRowViewModel> dates = new UpcomingRowViewModelsBuilder(TimePeriod.between(FEBRUARY_1st, FEBRUARY_3rd), upcomingRowViewModelsFactory)
                .withContactEvents(contactEventsList)
                .build();

        assertThat(dates.size()).isEqualTo(2);
    }

    @Test
    public void givenABankHoliday_thenACelebrationDateIsCreated() {
        BankHoliday bankHoliday = aBankHoliday();
        List<BankHoliday> bankHolidays = singletonList(bankHoliday);
        List<UpcomingRowViewModel> dates = new UpcomingRowViewModelsBuilder(TimePeriod.between(bankHoliday.getDate(), bankHoliday.getDate()), upcomingRowViewModelsFactory)
                .withBankHolidays(bankHolidays)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenEventsOnDifferentEvents_thenACelebrationDatesForEachOneAreCreated() {
        List<UpcomingRowViewModel> dates = new UpcomingRowViewModelsBuilder(TimePeriod.between(FEBRUARY_1st, MARCH_5th), upcomingRowViewModelsFactory)
                .withContactEvents(singletonList(aContactEventOn(FEBRUARY_1st)))
                .withBankHolidays(singletonList(aBankHolidayOn(FEBRUARY_3rd)))
                .withNamedays(singletonList(new NamesInADate(MARCH_5th, singletonList("Name"))))
                .build();

        assertThat(dates.size()).isEqualTo(3);
    }

    private static BankHoliday aBankHolidayOn(Date date) {
        return new BankHoliday("A bank holiday", date);
    }

    private static BankHoliday aBankHoliday() {
        return new BankHoliday("A bank holiday", Date.on(1, JANUARY, 1990));
    }

    private static ContactEvent aContactEvent() {
        return aContactEventOn(Date.on(1, JANUARY, 1990));
    }

    private static ContactEvent aContactEventOn(Date date) {
        DeviceContact contact = new DeviceContact(1, DisplayName.NO_NAME, "lookup_key");
        return new ContactEvent(NO_DEVICE_EVENT_ID, StandardEventType.BIRTHDAY, date, contact);
    }

    private static TimePeriod timeOf(ContactEvent event) {
        return TimePeriod.between(event.getDate(), event.getDate());
    }

}
