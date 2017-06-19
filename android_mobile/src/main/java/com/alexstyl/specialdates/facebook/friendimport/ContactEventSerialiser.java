package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ContactEventSerialiser {

    private static final String EVENT_END = "END:VEVENT";
    private static final String EVENT_START = "BEGIN:VEVENT";
    private static final String EVENT_DATE = "DTSTART";
    private static final String EVENT_SUMMARY = "SUMMARY";
    private static final String EVENT_UID = "UID";

    private final FacebookContactFactory factory;

    ContactEventSerialiser(FacebookContactFactory factory) {
        this.factory = factory;
    }

    List<ContactEvent> serialiseEventsFrom(InputStream inputStream) throws IOException, DateParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<ContactEvent> contactEvents = new ArrayList<>();
        Map<String, String> map = null;
        while ((line = reader.readLine()) != null) {
            if (EVENT_START.equals(line)) {
                map = new HashMap<>();
            } else if (EVENT_END.equals(line)) {
                ContactEvent contactEvent = factory.createContactFrom(map);
                contactEvents.add(contactEvent);
            } else if (line.startsWith(EVENT_DATE)) {
                parse(line, map, EVENT_DATE);
            } else if (line.startsWith(EVENT_SUMMARY)) {
                parse(line, map, EVENT_SUMMARY);
            } else if (line.startsWith(EVENT_UID)) {
                parse(line, map, EVENT_UID);
            }
        }
        return contactEvents;
    }

    private void parse(String line, Map<String, String> map, String value) {
        String substr = line.substring(value.length() + 1, line.length());
        map.put(value, substr);
    }
}
