package com.alexstyl.specialdates.facebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import static com.alexstyl.specialdates.facebook.StreamUtil.normaliseDate;
import static com.alexstyl.specialdates.facebook.StreamUtil.streamFrom;

class MockCalendarFetcher implements CalendarFetcher {

    @Override
    public Calendar fetchCalendarFrom(URL url) {
        try {
            String normalised = readNormalisedCalendar();
            InputStream is = streamFrom(normalised);

            CalendarBuilder builder = new CalendarBuilder();
            Calendar build = builder.build(is);

            is.close();
            return build;
        } catch (ParserException | IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable to create mock calendar");
    }

    private String readNormalisedCalendar() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getPathToCalendar()));
        String line = reader.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = reader.readLine();
        }
        reader.close();
        String rawCalendar = sb.toString();
        return normaliseDate(rawCalendar);
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
