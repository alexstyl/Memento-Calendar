package com.alexstyl.specialdates.facebook;

import java.io.IOException;
import java.net.URL;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

class FacebookCalendarLoader implements CalendarLoader {
    private static final String CALENDAR_URL = "https://www.facebook.com/ical/b.php?uid=1358181263&key=AQCg1OoTtQTSzywU";

    @Override
    public Calendar loadCalendar() {
        try {
            URL url = new URL(CALENDAR_URL);
            return new CalendarBuilder().build(url.openStream());
        } catch (IOException | ParserException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Unhandled case");
    }
}
