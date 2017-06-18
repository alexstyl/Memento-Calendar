package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.facebook.FacebookImagePathCreator;
import com.alexstyl.specialdates.util.DateParser;

import java.util.Map;

class FacebookContactFactory {
    private DateParser parser = DateParser.INSTANCE;
    private FacebookImagePathCreator imagePathCreator = FacebookImagePathCreator.INSTANCE;

    ContactEvent createContactFrom(Map<String, String> map) throws DateParseException {
        Date date = dateFrom(map);
        DisplayName name = nameFrom(map);
        long uid = idOf(map);
        return new ContactEvent(Optional.<Long>absent(), StandardEventType.BIRTHDAY, date, new FacebokContact(uid, name, imagePathCreator.forUid(uid)));
    }

    private Date dateFrom(Map<String, String> map) throws DateParseException {
        String dateString = getOrThrow(map, "DTSTART");
        return parser.parseWithoutYear(dateString);
    }

    private DisplayName nameFrom(Map<String, String> map) {
        String summary = getOrThrow(map, "SUMMARY");
        int endOfName = summary.indexOf("'s birthday");
        return DisplayName.from(summary.substring(0, endOfName));
    }

    private long idOf(Map<String, String> map) {
        String uid = getOrThrow(map, "UID");
        int facebookMail = uid.indexOf("@facebook.com");
        return Long.valueOf(uid.substring(1, facebookMail));
    }

    private static String getOrThrow(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        throw new IllegalArgumentException("Map did not contain " + key);
    }
}
