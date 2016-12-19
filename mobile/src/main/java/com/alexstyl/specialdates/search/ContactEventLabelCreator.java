package com.alexstyl.specialdates.search;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;

final class ContactEventLabelCreator {

    private final StringResources stringResources;
    private final DateLabelCreator dateLabelCreator;

    ContactEventLabelCreator(StringResources stringResources, DateLabelCreator dateLabelCreator) {
        this.stringResources = stringResources;
        this.dateLabelCreator = dateLabelCreator;
    }

    public String createFor(ContactEvent event) {
        String eventLabel = event.getLabel(stringResources);
        String dateLabel = dateLabelCreator.createLabelFor(event.getDate());
        return stringResources.getString(R.string.search_event_label, eventLabel, dateLabel);
    }
}
