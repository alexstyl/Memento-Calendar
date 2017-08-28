package com.alexstyl.specialdates.search;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;

final class ContactEventLabelCreator {

    private final Date today;
    private final Strings strings;
    private final DateLabelCreator dateLabelCreator;

    ContactEventLabelCreator(Date today, Strings strings, DateLabelCreator dateLabelCreator) {
        this.today = today;
        this.strings = strings;
        this.dateLabelCreator = dateLabelCreator;
    }

    public String createFor(ContactEvent event) {
        String eventLabel = event.getLabel(today, strings);
        String dateLabel = dateLabelCreator.createLabelWithYearPreferredFor(event.getDate());
        return strings.getString(R.string.search_event_label, eventLabel, dateLabel);
    }
}
