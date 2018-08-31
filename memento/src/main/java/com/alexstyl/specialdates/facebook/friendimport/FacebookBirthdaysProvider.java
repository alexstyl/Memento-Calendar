package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.date.ContactEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FacebookBirthdaysProvider {

    private final CalendarLoader calendarLoader;
    private final ContactEventSerialiser serialiser;

    public FacebookBirthdaysProvider(CalendarLoader calendarLoader, ContactEventSerialiser serialiser) {
        this.calendarLoader = calendarLoader;
        this.serialiser = serialiser;
    }

    List<ContactEvent> fetchCalendarFrom(URL url) throws CalendarFetcherException {
        InputStream inputStream = null;
        try {
            inputStream = calendarLoader.loadFrom(url);
            return serialiser.createEventsFrom(inputStream);
        } catch (IOException e) {
            throw new CalendarFetcherException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
