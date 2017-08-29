package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.upcoming.EventColors;

import java.util.ArrayList;
import java.util.List;

final class ContactEventViewModelFactory {

    private final ContactEventLabelCreator eventLabelCreator;

    ContactEventViewModelFactory(ContactEventLabelCreator eventLabelCreator) {
        this.eventLabelCreator = eventLabelCreator;
    }

    List<ContactEventViewModel> createViewModelFrom(List<ContactWithEvents> contactEvents) {
        List<ContactEventViewModel> models = new ArrayList<>();
        for (ContactWithEvents contactEvent : contactEvents) {

            Contact contact = contactEvent.getContact();

            for (ContactEvent event : contactEvent.getEvents()) {
                String eventLabel = eventLabelCreator.createFor(event);
                int variant = getVariationFor(event);
                ContactEventViewModel viewModel = new ContactEventViewModel(
                        contact,
                        eventLabel,
                        EventColors.INSTANCE.colorOf(event.getType()),
                        variant
                );
                models.add(viewModel);
            }
        }
        return models;
    }

    private int getVariationFor(ContactEvent event) {
        return (int) event.getContact().getContactID();
    }
}
