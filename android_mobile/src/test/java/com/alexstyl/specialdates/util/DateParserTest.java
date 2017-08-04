package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;

import org.junit.Test;

import static com.alexstyl.specialdates.date.DateConstants.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class DateParserTest {

    private DateParser dateParser = DateParser.INSTANCE;

    @Test
    public void dateWithSlashes() throws DateParseException {
        String dateDashes = "13/Jan/1972";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.Companion.on(13, JANUARY, 1972));
    }

    @Test
    public void longDate() throws DateParseException {
        String dateDashes = "1949-02-14T00:00:00Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.Companion.on(14, FEBRUARY, 1949));
    }

    @Test
    public void longDate2() throws DateParseException {
        String dateDashes = "20151026T083936Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.Companion.on(26, OCTOBER, 2015));
    }

    @Test
    public void datesWithDashes() throws DateParseException {
        String dateDashes = "2016-03-29";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.Companion.on(29, MARCH, 2016));
    }

    @Test
    public void datesWithDashesWithoutYear() throws DateParseException {
        String dateDashes = "--03-29";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.Companion.on(29, MARCH));
    }

    @Test
    public void noYear_on_29th_Of_February() throws DateParseException {
        String dateDashes = "--02-29";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.Companion.on(29, FEBRUARY));
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionWhenNullIsPassed() throws DateParseException {
        dateParser.parse(null);
    }
}
