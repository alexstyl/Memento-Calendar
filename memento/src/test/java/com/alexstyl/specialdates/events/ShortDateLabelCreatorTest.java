package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator;

import org.junit.Test;

import static com.alexstyl.specialdates.date.Months.MAY;
import static org.fest.assertions.api.Assertions.assertThat;

public class ShortDateLabelCreatorTest {

    private static ShortDateLabelCreator CREATOR = new ShortDateLabelCreator();

    @Test
    public void givenDateWithYear_whenAskingYear_thenYearIsReturned() {
        Date date = Date.Companion.on(5, MAY, 1995);
        String dateToString = CREATOR.createLabelWithYearPreferredFor(date);
        assertThat(dateToString).isEqualTo("1995-05-05");
    }

    @Test
    public void givenDateWithNoYear_whenAskingForYear_NoYearIsReturned() {
        Date date = Date.Companion.on(5, MAY);
        String dateToString = CREATOR.createLabelWithYearPreferredFor(date);
        assertThat(dateToString).isEqualTo("--05-05");
    }

    @Test
    public void givenDateWithNoYear_whenAskingNoYear_thenNoYearIsReturned() {
        Date date = Date.Companion.on(5, MAY);
        String dateToString = CREATOR.createLabelWithNoYearFor(date);
        assertThat(dateToString).isEqualTo("05-05");
    }

    @Test
    public void givenDateWithYear_whenAskingForNoYear_NoYearIsReturned() {
        Date date = Date.Companion.on(5, MAY, 1990);
        String dateToString = CREATOR.createLabelWithNoYearFor(date);
        assertThat(dateToString).isEqualTo("05-05");
    }
}
