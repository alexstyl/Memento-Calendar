package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateParseException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

import org.apache.log4j.BasicConfigurator;

class FacebookBirthdaysProvider {

    private final CalendarFetcher fileLoader;
    private final FacebookContactFactory factory;



    FacebookBirthdaysProvider(CalendarFetcher fileLoader, FacebookContactFactory factory) {
        this.fileLoader = fileLoader;
        this.factory = factory;
    }

    List<ContactEvent> providerBirthdays(URL url) throws CalendarFetcherException {
        Calendar calendar = fileLoader.fetchCalendarFrom(url);
        List<ContactEvent> contacts = new ArrayList<>();
        for (CalendarComponent component : calendar.getComponents()) {
            Map<String, String> contactValues = new HashMap<>();
            for (Property property : component.getProperties()) {
                contactValues.put(property.getName(), property.getValue());
            }
            try {
                ContactEvent contactFrom = factory.createContactFrom(contactValues);
                contacts.add(contactFrom);
            } catch (DateParseException e) {
                // do not throw an exception just for one exception
                ErrorTracker.track(e);
            }
        }
        return contacts;
    }

}
