package com.alexstyl.specialdates.upcoming;

import android.support.annotation.NonNull;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.DeviceContact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.search.DumbTestResources;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.DateConstants.*;
import static com.alexstyl.specialdates.date.TimePeriod.between;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UpcomingRowViewModelsBuilderTest {

    private static final Date FEBRUARY_1st = Date.on(1, FEBRUARY, 1990);
    private static final Date FEBRUARY_3rd = Date.on(3, FEBRUARY, 1990);
    private static final Date MARCH_5th = Date.on(5, MARCH, 1990);
    private static final Optional<Long> NO_DEVICE_EVENT_ID = Optional.absent();

    private UpcomingEventRowViewModelFactory upcomingEventRowViewModelFactory;

    @Mock
    private ColorResources mockColorResources;
    @Mock
    private StringResources mockStringResources;
    private UpcomingEventsAdRules adRules = new UpcomingEventsNoAdRules();

    @Before
    public void setUp() {
        Date today = Date.today();
        upcomingEventRowViewModelFactory = new UpcomingEventRowViewModelFactory(
                today,
                new UpcomingDateStringCreator(new DumbTestResources(), today),
                new ContactViewModelFactory(mockColorResources, mockStringResources),
                mockStringResources,
                new BankHolidayViewModelFactory(),
                new NamedaysViewModelFactory(today),
                MonthLabels.forLocale(Locale.getDefault())
        );

    }

    @Test
    public void celebrationDateIsCreatedCorrectlyForDifferentYear() {
        ContactEvent event = aContactEventOn(Date.on(1, JANUARY, 1990));
        TimePeriod duration = between(Date.on(1, JANUARY, 2016), Date.on(1, DECEMBER, 2016));

        List<UpcomingRowViewModel> dates = builder(duration)
                .withContactEvents(singletonList(event))
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenASingleContactEvent_thenOneCelebrationDateIsCreated() {
        ContactEvent event = aContactEvent();
        List<ContactEvent> contactEvents = singletonList(event);

        TimePeriod duration = timeOf(event);

        List<UpcomingRowViewModel> dates = builder(duration)
                .withContactEvents(contactEvents)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenTwoContactEventsOnSameDate_thenOneCelebrationDateIsCreated() {

        List<UpcomingRowViewModel> dates = builder(between(FEBRUARY_1st, FEBRUARY_1st))
                .withContactEvents(asList(
                        aContactEventOn(FEBRUARY_1st),
                        aContactEventOn(FEBRUARY_1st)
                ))
                .build();

        int datesCount = 1;
        int monthCount = 1;
        assertThat(dates.size()).isEqualTo(datesCount + monthCount);
    }

    @Test
    public void givenTwoContactEventsOnDifferentDate_thenTwoCelebrationDateAreCreated() {

        List<UpcomingRowViewModel> dates = builder(between(FEBRUARY_1st, FEBRUARY_3rd))
                .withContactEvents(asList(
                        aContactEventOn(FEBRUARY_1st),
                        aContactEventOn(FEBRUARY_3rd)
                ))
                .build();

        int monthCount = 1;
        int datesCount = 2;
        assertThat(dates.size()).isEqualTo(datesCount + monthCount);
    }

    @Test
    public void givenABankHoliday_thenACelebrationDateIsCreated() {
        BankHoliday bankHoliday = aBankHoliday();
        List<BankHoliday> bankHolidays = singletonList(bankHoliday);
        List<UpcomingRowViewModel> dates = builder(between(bankHoliday.getDate(), bankHoliday.getDate()))
                .withBankHolidays(bankHolidays)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenEventsOnDifferentMonths_thenACelebrationDatesForEachOneAreCreated() {
        List<UpcomingRowViewModel> dates = builder(between(FEBRUARY_1st, MARCH_5th))
                .withContactEvents(asList(aContactEventOn(FEBRUARY_1st)))
                .withBankHolidays(asList(aBankHolidayOn(FEBRUARY_3rd)))
                .withNamedays(asList(new NamesInADate(MARCH_5th, singletonList("Name"))))
                .build();

        int datesCount = 3;
        int monthsCounts = 2;
        assertThat(dates.size()).isEqualTo(datesCount + monthsCounts);
    }

    @NonNull
    private UpcomingRowViewModelsBuilder builder(TimePeriod timePeriod) {
        return new UpcomingRowViewModelsBuilder(timePeriod, upcomingEventRowViewModelFactory, adRules);
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
        return between(event.getDate(), event.getDate());
    }

}
