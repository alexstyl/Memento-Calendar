package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.events.peopleevents.EventType;

interface ContactEventsListener {
    void onAddEventClicked(ContactEventViewModel viewModel);

    void onRemoveEventClicked(EventType eventType);
}
