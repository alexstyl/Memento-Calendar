package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.facebook.friendimport.CalendarFetcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.alexstyl.specialdates.facebook.friendimport.StreamUtil.normaliseDate;

class MockCalendarFetcher implements CalendarFetcher {

    @Override
    public List<ContactEvent> fetchCalendarFrom(URL url) {
//        try {
//            String normalised = readNormalisedCalendar();
//            InputStream is = streamFrom(normalised);
//
//            CalendarBuilder builder = new CalendarBuilder();
//            Calendar build = builder.build(is);
//
//            is.close();
//            return build;
//        } catch (ParserException | IOException e) {
//            e.printStackTrace();
//        }
//        throw new RuntimeException("Unable to create mock calendar");
        return new ArrayList<>();
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
