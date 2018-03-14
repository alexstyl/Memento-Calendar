package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.facebook.FacebookImagePath;
import com.alexstyl.specialdates.date.DateParser;

import java.net.URI;
import java.util.Map;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

class FacebookContactFactory {

    private final DateParser parser;

    FacebookContactFactory(DateParser parser) {
        this.parser = parser;
    }

    ContactEvent createContactFrom(Map<String, String> map) throws InvalidFacebookContactException {
        try {
            Date date = dateFrom(map);
            DisplayName name = nameFrom(map);
            long uid = idOf(map);
            URI imagePath = FacebookImagePath.forUid(uid);
            return new ContactEvent(Optional.<Long>absent(), StandardEventType.BIRTHDAY, date, new Contact(uid, name, imagePath, SOURCE_FACEBOOK));
        } catch (DateParseException | IndexOutOfBoundsException ex) {
            throw new InvalidFacebookContactException(ex);
        }
    }

    private Date dateFrom(Map<String, String> map) throws DateParseException {
        String dateString = getOrThrow(map, "DTSTART");
        return parser.parseWithoutYear(dateString);
    }

    private DisplayName nameFrom(Map<String, String> map) {
        String summary = getOrThrow(map, "SUMMARY");
        int endOfName = summary.indexOf("'s birthday");
        return DisplayName.Companion.from(summary.substring(0, endOfName));
    }

    private long idOf(Map<String, String> map) {
        String uid = getOrThrow(map, "UID");
        int facebookMail = uid.indexOf("@facebook.com");
        return Long.parseLong(uid.substring(1, facebookMail));
    }

    private static String getOrThrow(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        throw new IllegalArgumentException("Map did not contain " + key);
    }
}
