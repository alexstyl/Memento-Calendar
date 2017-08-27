package com.alexstyl.specialdates.search;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;

final class ContactEventLabelCreator {

    private final Date today;
    private final StringResources stringResources;
    private final DateLabelCreator dateLabelCreator;

    ContactEventLabelCreator(Date today, StringResources stringResources, DateLabelCreator dateLabelCreator) {
        this.today = today;
        this.stringResources = stringResources;
        this.dateLabelCreator = dateLabelCreator;
    }

    public String createFor(ContactEvent event) {
        String eventLabel = event.getLabel(today, stringResources);
        String dateLabel = dateLabelCreator.createLabelWithYearPreferredFor(event.getDate());
        return stringResources.getString(R.string.search_event_label, eventLabel, dateLabel);
    }
}
