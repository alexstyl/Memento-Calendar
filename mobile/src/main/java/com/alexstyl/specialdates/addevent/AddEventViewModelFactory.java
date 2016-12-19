package com.alexstyl.specialdates.addevent;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

import java.util.ArrayList;
import java.util.List;

final class AddEventViewModelFactory {

    private final StringResources stringResources;

    AddEventViewModelFactory(StringResources stringResources) {
        this.stringResources = stringResources;
    }

    List<ContactEventViewModel> createViewModelsForAllEventsBut(List<EventType> existingTypes) {
        List<ContactEventViewModel> addEventViewModels = new ArrayList<>();

        for (StandardEventType eventType : StandardEventType.values()) {
            if (existingTypes.contains(eventType) || eventType == StandardEventType.NAMEDAY) {
                continue;
            }
            String eventName = eventType.getEventName(stringResources);
            ContactEventViewModel viewModel = new ContactEventViewModel(eventName, eventType);
            addEventViewModels.add(viewModel);
        }
        return addEventViewModels;
    }

}
