package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.contact.ContactSource;

import java.util.List;

public class ContactEventsMarshaller {

    private final DateDisplayStringCreator instance;
    @ContactSource
    private final int source;

    public ContactEventsMarshaller(@ContactSource int source) {
        this.source = source;
        instance = DateDisplayStringCreator.INSTANCE;
    }

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

        ContentValues values = new ContentValues(4);
        values.put(PeopleEventsContract.PeopleEvents.CONTACT_ID, contact.getContactID());
        values.put(PeopleEventsContract.PeopleEvents.DISPLAY_NAME, contact.getDisplayName().toString());
        String date = instance.stringOf(event.getDate());
        values.put(PeopleEventsContract.PeopleEvents.DATE, date);
        values.put(PeopleEventsContract.PeopleEvents.EVENT_TYPE, event.getType().getId());
        values.put(PeopleEventsContract.PeopleEvents.SOURCE, source);

        putDeviceContactIdIfPresent(event, values);

        return values;
    }

    private void putDeviceContactIdIfPresent(ContactEvent event, ContentValues values) {
        Optional<Long> deviceEventId = event.getDeviceEventId();
        if (deviceEventId.isPresent()) {
            values.put(PeopleEventsContract.PeopleEvents.DEVICE_EVENT_ID, deviceEventId.get());
        } else {
            values.put(PeopleEventsContract.PeopleEvents.DEVICE_EVENT_ID, -1);
        }
    }

}
