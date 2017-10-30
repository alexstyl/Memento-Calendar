package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;

import java.util.List;

public class ContactEventsMarshaller {

    private static final int DEFAULT_VALUES_SIZE = 5;
    private final ShortDateLabelCreator DATE_LABEL_CREATOR = ShortDateLabelCreator.INSTANCE;

    public ContentValues[] marshall(List<ContactEvent> item) {
        ContentValues[] returningValues = new ContentValues[item.size()];
        for (int i = 0; i < item.size(); i++) {
            ContactEvent event = item.get(i);
            returningValues[i] = createValuesFor(event);
        }
        return returningValues;
    }

    private ContentValues createValuesFor(ContactEvent event) {
        Contact contact = event.getContact();

        ContentValues values = new ContentValues(DEFAULT_VALUES_SIZE);

        values.put(AnnualEventsContract.CONTACT_ID, contact.getContactID());
        values.put(AnnualEventsContract.DISPLAY_NAME, contact.getDisplayName().toString());
        values.put(AnnualEventsContract.DATE, DATE_LABEL_CREATOR.createLabelWithYearPreferredFor(event.getDate()));
        values.put(AnnualEventsContract.EVENT_TYPE, event.getType().getId());
        values.put(AnnualEventsContract.SOURCE, contact.getSource());

        putDeviceContactIdIfPresent(event, values);

        return values;
    }

    private void putDeviceContactIdIfPresent(ContactEvent event, ContentValues values) {
        Optional<Long> deviceEventId = event.getDeviceEventId();
        if (deviceEventId.isPresent()) {
            values.put(AnnualEventsContract.DEVICE_EVENT_ID, deviceEventId.get());
        } else {
            values.put(AnnualEventsContract.DEVICE_EVENT_ID, -1);
        }
    }

}
