package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.DayDate;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateParserTest {

    @Test
    public void canParseDatesWithDashes() throws DateParseException {
        String dateDashes = "13/Jan/1972";
        DayDate parsed = new DateParser().parse(dateDashes);
        assertThat(parsed).isEqualTo(DayDate.newInstance(13, 1, 1972));
    }

    @Test
    public void canParseLongDates() throws DateParseException {
        String dateDashes = "1949-02-14T00:00:00Z";
        DayDate parsed = new DateParser().parse(dateDashes);
        assertThat(parsed).isEqualTo(DayDate.newInstance(14, 2, 1949));
    }

    @Test
    public void canParseLongDates2() throws DateParseException {
        String dateDashes = "20151026T083936Z";
        DayDate parsed = new DateParser().parse(dateDashes);
        assertThat(parsed).isEqualTo(DayDate.newInstance(26, 10, 2015));
    }

    @Test(expected = DateParseException.class)
    public void throwsExceptionWhenNullIsPassed() throws DateParseException {
        new DateParser().parse(null);
    }
}
