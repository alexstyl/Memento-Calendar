package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NamedaysListTest {

    private static final String RECURRING_NAMEDAY = "recurring_nameday";
    private static final String FIXED_YEAR_NAMEDAY = "fixed_date_nameday";
    private static final int FIXED_YEAR = 2015;
    private static final int FIXED_MONTH = 1;

    private static final NamedaysList namedays = new NamedaysList();

    @BeforeClass
    public static void setUp() {
        populateNamedays();
    }

    private static void populateNamedays() {
        namedays.addNameday(Date.on(1, FIXED_MONTH, FIXED_YEAR), FIXED_YEAR_NAMEDAY);
        namedays.addNameday(Date.on(2, FIXED_MONTH, FIXED_YEAR), FIXED_YEAR_NAMEDAY);
        namedays.addNameday(Date.on(3, FIXED_MONTH, FIXED_YEAR), FIXED_YEAR_NAMEDAY);

        namedays.addNameday(Date.on(4, FIXED_MONTH), RECURRING_NAMEDAY);
        namedays.addNameday(Date.on(5, FIXED_MONTH), RECURRING_NAMEDAY);

    }

    @Test
    public void whenNoYearSpecified_thenRecurringEventIsReturned() {
        Date dateWithNoYear = Date.on(4, FIXED_MONTH);

        NamesInADate results = namedays.getNamedaysFor(dateWithNoYear);

        assertThat(results.getNames().get(0)).isEqualTo(RECURRING_NAMEDAY);
    }

    @Test
    public void whenNoYearSpecified_thenFixedYearEventIsNotReturned() {
        Date dateWithNoYear = Date.on(1, FIXED_MONTH);

        NamesInADate results = namedays.getNamedaysFor(dateWithNoYear);

        assertThat(results.getNames()).isEmpty();
    }

    @Test
    public void whenYearSpecified_thenFixedYearEventIsReturned() {
        Date dateWithNoYear = Date.on(1, FIXED_MONTH, FIXED_YEAR);

        NamesInADate results = namedays.getNamedaysFor(dateWithNoYear);

        assertThat(results.getNames().get(0)).isEqualTo(FIXED_YEAR_NAMEDAY);
    }

    @Test
    public void whenYearSpecified_thenRecurringEventIsReturned() {
        Date date = Date.on(4, FIXED_MONTH, FIXED_YEAR);

        NamesInADate results = namedays.getNamedaysFor(date);

        assertThat(results.getNames().get(0)).isEqualTo(RECURRING_NAMEDAY);
    }

}
