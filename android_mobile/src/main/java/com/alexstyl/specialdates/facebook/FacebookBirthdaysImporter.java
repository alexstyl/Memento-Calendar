package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.contact.Contact;

import java.util.List;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;

import org.apache.log4j.BasicConfigurator;

class FacebookBirthdaysImporter {

    private final CalendarLoader fileLoader;

    FacebookBirthdaysImporter(CalendarLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    List<Contact> fetchFriends() {
        BasicConfigurator.configure();

        Calendar calendar = fileLoader.loadCalendar();

        for (CalendarComponent component : calendar.getComponents()) {
            System.out.println("Component [" + component.getName() + "]");

            for (Property property : component.getProperties()) {
                System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
            }
        }
        return null;
    }
}
