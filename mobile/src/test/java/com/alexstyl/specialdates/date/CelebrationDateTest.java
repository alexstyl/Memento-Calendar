package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CelebrationDateTest {

    private static final Date DATE = Date.on(6, 7, 2016);

    @Test
    public void givenACelebrationDateWithASetDate_thenReturningDayOfTheMonthIsCorrect() throws Exception {
        CelebrationDate celebrationDate = aCelebrateDateWithDate(DATE);
        assertThat(celebrationDate.getDayofMonth()).isEqualTo(DATE.getDayOfMonth());
    }

    @Test
    public void givenACelebrationDateWithASetDate_thenReturningMonthIsCorrect() throws Exception {
        CelebrationDate celebrationDate = aCelebrateDateWithDate(DATE);
        assertThat(celebrationDate.getMonth()).isEqualTo(DATE.getMonth());
    }

    @Test
    public void givenACelebrationDateWithASetDate_thenReturningYearIsCorrect() throws Exception {
        CelebrationDate celebrationDate = aCelebrateDateWithDate(DATE);
        assertThat(celebrationDate.getYear()).isEqualTo(DATE.getYear());
    }

    private static CelebrationDate aCelebrateDateWithDate(Date date) {
        ContactEvents contactEvent = ContactEvents.createFrom(date, new ArrayList<ContactEvent>());
        return new CelebrationDate(date, contactEvent, Optional.<NamesInADate>absent(), Optional.<BankHoliday>absent());
    }
}
