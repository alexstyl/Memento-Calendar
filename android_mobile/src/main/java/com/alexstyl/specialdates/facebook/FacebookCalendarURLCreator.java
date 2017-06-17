package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.ErrorTracker;

import java.net.MalformedURLException;
import java.net.URL;

public class FacebookCalendarURLCreator {

    public URL createFrom(UserCredentials user) {
        try {
            return new URL("www.facebook.com/ical/b.php?uid=" + user.getUid() + "&key=" + user.getKey());
        } catch (MalformedURLException e) {
            ErrorTracker.track(e);
            throw new IllegalArgumentException(e);
        }
    }
}
