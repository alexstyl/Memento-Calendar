package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.facebook.UserCredentials;

import java.net.MalformedURLException;
import java.net.URL;

class CalendarURLCreator {

    private final CrashAndErrorTracker tracker;

    CalendarURLCreator(CrashAndErrorTracker tracker) {
        this.tracker = tracker;
    }

    URL createFrom(UserCredentials user) {
        try {
            return new URL("https://www.facebook.com/ical/b.php?locale=en_US&uid=" + user.getUid() + "&key=" + user.getKey());
        } catch (MalformedURLException e) {
            tracker.track(e);
            throw new IllegalArgumentException(e);
        }
    }
}
