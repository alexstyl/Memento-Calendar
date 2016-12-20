package com.alexstyl.specialdates.addevent;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.events.peopleevents.CustomEventType;
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
        boolean containsCustomType = containsCustomType(existingTypes);

        for (StandardEventType eventType : StandardEventType.values()) {
            if (existingTypes.contains(eventType)
                    || eventType == StandardEventType.NAMEDAY
                    || (eventType == StandardEventType.CUSTOM && containsCustomType)
                    ) {
                continue;
            }
            String eventName = eventType.getEventName(stringResources);
            ContactEventViewModel viewModel = new ContactEventViewModel(eventName, eventType);
            addEventViewModels.add(viewModel);
        }
        return addEventViewModels;
    }

    private boolean containsCustomType(List<EventType> existingTypes) {
        for (EventType existingType : existingTypes) {
            if (existingType instanceof CustomEventType) {
                return true;
            }
        }
        return false;
    }

}
