package com.alexstyl.specialdates.facebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ParameterFactoryRegistry;
import net.fortuna.ical4j.model.PropertyFactoryRegistry;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.component.VTimeZone;

class MockCalendarLoader implements CalendarLoader {

    @Override
    public Calendar loadCalendar() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getPathToCalendar()));
            CalendarBuilder builder = new CalendarBuilder(
                    new FacebookCalendarParser(),
                    new PropertyFactoryRegistry(),
                    new ParameterFactoryRegistry(),
                    new TimeZoneRegistry() {
                        @Override
                        public void register(TimeZone timezone) {
                            "".toCharArray();
                        }

                        @Override
                        public void register(TimeZone timezone, boolean update) {
                            "".toCharArray();
                        }

                        @Override
                        public void clear() {
                            "".toCharArray();
                        }

                        @Override
                        public TimeZone getTimeZone(String id) {
                            return new TimeZone(new VTimeZone());
                        }
                    }
            );
            // TODO close the reader
            return builder.build(reader);
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
