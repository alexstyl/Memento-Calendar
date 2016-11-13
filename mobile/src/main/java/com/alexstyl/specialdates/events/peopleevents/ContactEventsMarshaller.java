package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.database.SourceType;

import java.util.List;

public class ContactEventsMarshaller {

    @SourceType
    private final int sourceType;

    public ContactEventsMarshaller(@SourceType int sourceType) {
        this.sourceType = sourceType;
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
        values.put(PeopleEventsContract.PeopleEvents.SOURCE, sourceType);
        values.put(PeopleEventsContract.PeopleEvents.DISPLAY_NAME, contact.getDisplayName().toString());

        String date = DateDisplayStringCreator.getInstance().stringOf(event.getDate());
        values.put(PeopleEventsContract.PeopleEvents.DATE, date);

        values.put(PeopleEventsContract.PeopleEvents.EVENT_TYPE, event.getType().getId());
        return values;
    }

}
