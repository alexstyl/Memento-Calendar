package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.DayDate;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateParserTest {

    private ContactEventDateParser dateParser = new ContactEventDateParser();

    @Test
    public void canParseDatesWithDashes() throws DateParseException {
        String dateDashes = "13/Jan/1972";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(DayDate.newInstance(13, 1, 1972));
    }

    @Test
    public void canParseLongDates() throws DateParseException {
        String dateDashes = "1949-02-14T00:00:00Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(DayDate.newInstance(14, 2, 1949));
    }

    @Test
    public void canParseLongDates2() throws DateParseException {
        String dateDashes = "20151026T083936Z";
        Date parsed = dateParser.parse(dateDashes);
        assertThat(parsed).isEqualTo(DayDate.newInstance(26, 10, 2015));
    }

    @Test(expected = DateParseException.class)
    public void throwsExceptionWhenNullIsPassed() throws DateParseException {
        dateParser.parse(null);
    }

//    "yyyy-MM-dd","--MM-dd",
//            "MMM dd, yyyy","MMM dd yyyy","MMM dd",
//
//            "dd MMM yyyy","dd MMM",// 19 Aug 1990
//            "yyyyMMdd", // 20110505
//            "dd MMM yyyy",
//            "d MMM yyyy", // >>> 6 Δεκ 1980
//            "dd/MM/yyyy", //22/04/23
//            "yyyy-MM-dd HH:mm:ss.SSSZ", //ISO 8601
//            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
//            "dd-MM-yyyy", //25-4-1950
//            "dd/MMMM/yyyy", // 13/Gen/1972
//            "yyyy-MM-dd'T'HH:mm:ssZ", // 1949-02-14T00:00:00Z
//            "yyyyMMdd'T'HHmmssZ", // 20151026T083936Z

    @Test
    public void name() {

    }
}
