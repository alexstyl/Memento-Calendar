package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.search.DateLabelCreator;

import java.util.ArrayList;
import java.util.List;

final class ContactEventViewModelFactory {

    private final DateLabelCreator creator;

    ContactEventViewModelFactory(DateLabelCreator creator) {
        this.creator = creator;
    }

    List<ContactEventViewModel> createViewModelsFor(List<ContactEvent> contactEvents) {
        List<ContactEventViewModel> viewModels = new ArrayList<>(contactEvents.size());

        for (ContactEvent contactEvent : contactEvents) {
            String eventHint = creator.createLabelFor(contactEvent.getDate());
            Optional<Date> dateOptional = new Optional<>(contactEvent.getDate());
            EventType eventType = contactEvent.getType();
            viewModels.add(new ContactEventViewModel(eventHint, eventType, dateOptional));
        }
        return viewModels;
    }

}
