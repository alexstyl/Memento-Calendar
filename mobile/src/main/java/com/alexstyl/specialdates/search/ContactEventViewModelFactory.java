package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;

import java.util.ArrayList;
import java.util.List;

final class ContactEventViewModelFactory {

    private final EventLabelCreator eventLabelCreator;

    ContactEventViewModelFactory(EventLabelCreator eventLabelCreator) {
        this.eventLabelCreator = eventLabelCreator;
    }

    List<ContactEventViewModel> createViewModelFrom(List<ContactWithEvents> contactEvents) {
        List<ContactEventViewModel> models = new ArrayList<>();
        for (ContactWithEvents contactEvent : contactEvents) {

            Contact contact = contactEvent.getContact();

            for (ContactEvent event : contactEvent.getEvents()) {
                String eventLabel = eventLabelCreator.createFor(event);
                ContactEventViewModel viewModel = new ContactEventViewModel(contact, eventLabel, event.getType().getColorRes(), 5);
                models.add(viewModel);
            }
        }
        return models;
    }
}
