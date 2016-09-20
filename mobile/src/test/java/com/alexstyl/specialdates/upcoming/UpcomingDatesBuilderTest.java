package com.alexstyl.specialdates.upcoming;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.EventType;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UpcomingDatesBuilderTest {

    private static final DayDate FEBRUARY_1st = DayDate.newInstance(1, DayDate.FEBRUARY, 1990);
    private static final DayDate FEBRUARY_3rd = DayDate.newInstance(3, DayDate.FEBRUARY, 1990);
    private static final DayDate MARCH_5th = DayDate.newInstance(5, DayDate.MARCH, 1990);

    @Test
    public void givenASingleContactEvent_thenOneCelebrationDateIsCreated() {
        List<ContactEvent> contactEvents = Collections.singletonList(aContactEvent());

        List<CelebrationDate> dates = new UpcomingDatesBuilder()
                .withContactEvents(contactEvents)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenASingleContactEvent_thenTheCelebrationDateContainsTheContactEvent() {
        List<ContactEvent> contactEvents = Collections.singletonList(aContactEvent());

        List<CelebrationDate> dates = new UpcomingDatesBuilder()
                .withContactEvents(contactEvents)
                .build();

        ContactEvent celebrationDateEvent = dates.get(0).getContactEvents().getEvent(0);
        assertThat(celebrationDateEvent).isEqualTo(contactEvents.get(0));
    }

    @Test
    public void givenTwoContactEventsOnSameDate_thenOneCelebrationDateIsCreated() {
        List<ContactEvent> contactEventsList = Arrays.asList(
                aContactEventOn(FEBRUARY_1st),
                aContactEventOn(FEBRUARY_1st)
        );

        List<CelebrationDate> dates = new UpcomingDatesBuilder()
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

        List<CelebrationDate> dates = new UpcomingDatesBuilder()
                .withContactEvents(contactEventsList)
                .build();

        assertThat(dates.size()).isEqualTo(2);
    }

    @Test
    public void givenABankHoliday_thenACelebrationDateIsCreated() {
        List<BankHoliday> bankHolidays = Collections.singletonList(aBankHoliday());
        List<CelebrationDate> dates = new UpcomingDatesBuilder()
                .withBankHolidays(bankHolidays)
                .build();

        assertThat(dates.size()).isEqualTo(1);
    }

    @Test
    public void givenEventsOnDifferentEvents_thenACelebrationDatesForEachOneAreCreated() {
        List<CelebrationDate> dates = new UpcomingDatesBuilder()
                .withContactEvents(Collections.singletonList(aContactEventOn(FEBRUARY_1st)))
                .withBankHolidays(Collections.singletonList(aBankHolidayOn(FEBRUARY_3rd)))
                .withNamedays(Collections.singletonList(new NamesInADate(MARCH_5th, Collections.singletonList("Name"))))
                .build();

        assertThat(dates.size()).isEqualTo(3);
    }

    private BankHoliday aBankHolidayOn(DayDate date) {
        return new BankHoliday("A bank holiday", date);
    }

    @NonNull
    private BankHoliday aBankHoliday() {
        return new BankHoliday("A bank holiday", DayDate.newInstance(1, 1, 1990));
    }

    private static ContactEvent aContactEvent() {
        return aContactEventOn(DayDate.newInstance(1, 1, 1990));
    }

    private static ContactEvent aContactEventOn(DayDate date) {
        TestContact contact = new TestContact(1, DisplayName.NO_NAME);
        return new ContactEvent(EventType.BIRTHDAY, date, contact);
    }

}
