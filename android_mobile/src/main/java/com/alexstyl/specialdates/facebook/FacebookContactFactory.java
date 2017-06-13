package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.util.DateParser;

import java.util.Map;

class FacebookContactFactory {
    private DateParser parser = DateParser.INSTANCE;

    ContactEvent createContactFrom(Map<String, String> map) throws DateParseException {
        Date date = dateFrom(map);
        DisplayName name = nameFrom(map);
        long uid = idOf(map.get("UID"));
        return new ContactEvent(Optional.<Long>absent(), StandardEventType.BIRTHDAY, date, new FacebokContact(uid, name));
    }

    private Date dateFrom(Map<String, String> map) throws DateParseException {
        String dateString = map.get("DTSTART");
        return parser.parse(dateString);
    }

    private DisplayName nameFrom(Map<String, String> map) {
        String summary = map.get("SUMMARY");
        int endOfName = summary.indexOf("'s birthday");
        return DisplayName.from(summary.substring(0, endOfName));
    }

    private long idOf(String uid) {
        int facebookMail = uid.indexOf("@facebook.com");
        return Long.valueOf(uid.substring(1, facebookMail));
    }
}
