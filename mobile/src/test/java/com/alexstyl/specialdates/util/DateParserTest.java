package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateParserTest {

    private DateParser dateParser = new DateParser();

    @Test
    public void canParseDatesWithDashes() throws DateParseException {
        String dateDashes = "13/Jan/1972";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.on(13, 1, 1972));
    }

    @Test
    public void canParseLongDates() throws DateParseException {
        String dateDashes = "1949-02-14T00:00:00Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.on(14, 2, 1949));
    }

    @Test
    public void canParseLongDates2() throws DateParseException {
        String dateDashes = "20151026T083936Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.on(26, 10, 2015));
    }

    @Test
    public void canParseLongDates3() throws DateParseException {
        String dateDashes = "--02-29";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(Date.on(29, 2));
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionWhenNullIsPassed() throws DateParseException {
        dateParser.parse(null);
    }
}
