package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.date.ContactEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FacebookBirthdaysProviderTest {

    private static final String CALENDAR_URL = "https://www.facebook.com/ical/b.php?uid=1358181263&key=AQCg1OoTtQTSzywU";

    @Test
    public void parseMockCalendar() throws CalendarFetcherException, MalformedURLException {
        FacebookContactFactory factory = new FacebookContactFactory();
        FacebookBirthdaysProvider importer = new FacebookBirthdaysProvider(new FacebookCalendarFetcher(factory), factory);
        URL url = new URL(CALENDAR_URL);

        List<ContactEvent> contacts = importer.providerBirthdays(url);
        for (ContactEvent contactEvent : contacts) {
            System.out.println(contactEvent.getContact() + " on " + contactEvent.getDate());
        }
        assertThat(contacts).isNotEmpty();
    }

    @Test(expected = CalendarFetcherException.class)
    public void parsingRandomURLThrowsException() throws CalendarFetcherException, MalformedURLException {
        FacebookContactFactory factory = new FacebookContactFactory();
        FacebookBirthdaysProvider importer = new FacebookBirthdaysProvider(new FacebookCalendarFetcher(factory), factory);
        URL url = new URL("https://www.google.com");

        importer.providerBirthdays(url);

    }
}
