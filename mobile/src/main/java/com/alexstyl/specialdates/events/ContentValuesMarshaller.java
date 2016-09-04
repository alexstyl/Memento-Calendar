package com.alexstyl.specialdates.events;

import android.content.ContentValues;

import com.alexstyl.specialdates.Marshaller;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;

import java.util.List;

public class ContentValuesMarshaller implements Marshaller<List<ContactEvent>> {

    @Override
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
        values.put(PeopleEventsContract.PeopleEvents.SOURCE, ContactSource.DEVICE);
        values.put(PeopleEventsContract.PeopleEvents.DISPLAY_NAME, contact.getDisplayName().toString());

        String date = DateDisplayStringCreator.getInstance().stringOf(event.getDate());
        values.put(PeopleEventsContract.PeopleEvents.DATE, date);

        values.put(PeopleEventsContract.PeopleEvents.EVENT_TYPE, PeopleEventsContract.PeopleEvents.TYPE_NAMEDAY);
        return values;
    }
}
