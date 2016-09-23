package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.ParsedDate;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateParserTest {

    private ContactEventDateParser dateParser = new ContactEventDateParser();

    @Test
    public void canParseDatesWithDashes() throws DateParseException {
        String dateDashes = "13/Jan/1972";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(new ParsedDate(13, 1, 1972));
    }

    @Test
    public void canParseLongDates() throws DateParseException {
        String dateDashes = "1949-02-14T00:00:00Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(new ParsedDate(14, 2, 1949));
    }

    @Test
    public void canParseLongDates2() throws DateParseException {
        String dateDashes = "20151026T083936Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(new ParsedDate(26, 10, 2015));
    }

    @Test(expected = DateParseException.class)
    public void throwsExceptionWhenNullIsPassed() throws DateParseException {
        dateParser.parse(null);
    }
}
