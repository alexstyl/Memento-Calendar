package com.alexstyl.specialdates.addevent;

import android.view.View;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

import java.util.ArrayList;
import java.util.List;

final class AddEventViewModelFactory {

    private static final Optional<Date> NO_DATE = Optional.absent();
    private final Strings strings;

    AddEventViewModelFactory(Strings strings) {
        this.strings = strings;
    }

    List<AddEventContactEventViewModel> createViewModelsForAllEventsBut(List<EventType> existingTypes) {
        List<AddEventContactEventViewModel> addEventViewModels = new ArrayList<>();

        for (StandardEventType eventType : StandardEventType.values()) {
            if (existingTypes.contains(eventType)
                    || eventType == StandardEventType.NAMEDAY
                    || eventType == StandardEventType.CUSTOM) {
                continue;
            }
            addEventViewModels.add(createAddEventViewModelsFor(eventType));
        }
        return addEventViewModels;
    }

    AddEventContactEventViewModel createAddEventViewModelsFor(EventType eventType) {
        String eventName = eventType.getEventName(strings);
        return new AddEventContactEventViewModel(eventName, eventType, NO_DATE, View.GONE);
    }

}
