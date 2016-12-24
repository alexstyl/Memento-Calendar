package com.alexstyl.specialdates.addevent;

import android.view.View;

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

    List<ContactEventViewModel> createViewModel(List<ContactEvent> contactEvents) {
        List<ContactEventViewModel> viewModels = new ArrayList<>(contactEvents.size());

        for (ContactEvent contactEvent : contactEvents) {
            ContactEventViewModel viewModel = createViewModelWith(contactEvent.getType(), contactEvent.getDate());
            viewModels.add(viewModel);
        }
        return viewModels;
    }

    ContactEventViewModel createViewModelWith(EventType eventType, Date date) {
        String eventHint = date.hasYear() ? creator.createLabelWithYearFor(date) : creator.createLabelWithoutYearFor(date);
        Optional<Date> dateOptional = new Optional<>(date);
        return new ContactEventViewModel(eventHint, eventType, dateOptional, View.VISIBLE);
    }
}
