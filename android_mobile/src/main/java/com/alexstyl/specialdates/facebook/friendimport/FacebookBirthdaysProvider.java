package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.date.ContactEvent;

import java.net.URL;
import java.util.List;

class FacebookBirthdaysProvider {

    private final CalendarFetcher fileLoader;
    private final FacebookContactFactory factory;

    FacebookBirthdaysProvider(CalendarFetcher fileLoader, FacebookContactFactory factory) {
        this.fileLoader = fileLoader;
        this.factory = factory;
    }

    List<ContactEvent> providerBirthdays(URL url) throws CalendarFetcherException {
        List<ContactEvent> calendar = fileLoader.fetchCalendarFrom(url);
        return calendar;
//        List<ContactEvent> contacts = new ArrayList<>();
//        for (CalendarComponent component : calendar.getComponents()) {
//            Map<String, String> contactValues = new HashMap<>();
//            for (Property property : component.getProperties()) {
//                contactValues.put(property.getName(), property.getValue());
//            }
//            try {
//                ContactEvent contactFrom = factory.createContactFrom(contactValues);
//                contacts.add(contactFrom);
//            } catch (DateParseException e) {
//                // do not throw an exception just for one exception
//                ErrorTracker.track(e);
//            }
//        }
//        return contacts;
    }

}
