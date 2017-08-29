package com.alexstyl.specialdates.addevent;

import android.view.View;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.date.DateLabelCreator;

import java.util.ArrayList;
import java.util.List;

final class AddEventContactEventViewModelFactory {

    private final DateLabelCreator creator;

    AddEventContactEventViewModelFactory(DateLabelCreator creator) {
        this.creator = creator;
    }

    List<AddEventContactEventViewModel> createViewModel(List<ContactEvent> contactEvents) {
        List<AddEventContactEventViewModel> viewModels = new ArrayList<>(contactEvents.size());

        for (ContactEvent contactEvent : contactEvents) {
            AddEventContactEventViewModel viewModel = createViewModelWith(contactEvent.getType(), contactEvent.getDate());
            viewModels.add(viewModel);
        }
        return viewModels;
    }

    AddEventContactEventViewModel createViewModelWith(EventType eventType, Date date) {
        String eventHint = creator.createLabelFor(date);
        Optional<Date> dateOptional = new Optional<>(date);
        return new AddEventContactEventViewModel(eventHint, eventType, dateOptional, View.VISIBLE);
    }
}
