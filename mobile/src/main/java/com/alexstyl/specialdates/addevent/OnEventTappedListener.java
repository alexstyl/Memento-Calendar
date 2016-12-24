package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.events.peopleevents.EventType;

interface OnEventTappedListener {
    void onEventTapped(ContactEventViewModel viewModel);

    void onEventRemoved(EventType eventType);
}
