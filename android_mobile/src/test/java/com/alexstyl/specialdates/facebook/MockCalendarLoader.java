package com.alexstyl.specialdates.facebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;

class MockCalendarLoader implements CalendarLoader {

    @Override
    public Calendar loadCalendar() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getPathToCalendar()));
            CalendarBuilder builder = new CalendarBuilder();
            // TODO close the reader
            return builder.build(new UnfoldingReader(reader));
        } catch (ParserException | IOException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Unhandled case");
    }

    private static String getPathToCalendar() {
        return "android_mobile" + File.separator +
                "src" + File.separator +
                "debug" + File.separator +
                "assets" + File.separator +
                "mock" + File.separator +
                "facebook-calendar.ics";
    }
}
