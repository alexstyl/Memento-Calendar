package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.date.ContactEvent;
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
            // int iconResId = contactEvent.getType().getIconResId();
            String eventHint = creator.createLabelFor(contactEvent.getDate());
            viewModels.add(new ContactEventViewModel(eventHint, contactEvent.getType()));
        }
        return viewModels;
    }

}
