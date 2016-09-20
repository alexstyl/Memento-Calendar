package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayJSONParser;

import org.junit.Test;

public class NamedayJSONParserTest {

    @Test
    public void name() throws Exception {
        new NamedayJSONParser().parseAsNamedays(NamedayLocale.gr);

    }
}
