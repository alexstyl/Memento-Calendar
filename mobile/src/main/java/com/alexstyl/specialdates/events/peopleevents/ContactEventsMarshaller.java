package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.novoda.notils.exception.DeveloperError;

import java.util.List;

public class ContactEventsMarshaller implements Marshaller<ContactEvent> {

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

        values.put(PeopleEventsContract.PeopleEvents.EVENT_TYPE, getTypeFor(event));
        return values;
    }

    private int getTypeFor(ContactEvent event) {
        switch (event.getType()) {
            case BIRTHDAY:
                return AnnualEventsContract.TYPE_BIRTHDAY;
            case NAMEDAY:
                return AnnualEventsContract.TYPE_NAMEDAY;
        }
        throw new DeveloperError(event.getType() + " has no EventColumn reference");
    }

}
