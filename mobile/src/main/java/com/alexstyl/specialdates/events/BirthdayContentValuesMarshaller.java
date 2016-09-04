package com.alexstyl.specialdates.events;

import android.content.ContentValues;

import com.alexstyl.specialdates.Marshaller;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;

import java.util.List;

class BirthdayContentValuesMarshaller implements Marshaller<List<Contact>> {

    @Override
    public ContentValues[] marshall(List<Contact> contacts) {
        ContentValues[] values = new ContentValues[contacts.size()];

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            values[i] = createValuesFor(contact);
        }
        return values;
    }

    ContentValues createValuesFor(Contact contact) {
        ContentValues values = new ContentValues(5);
        values.put(PeopleEventsContract.PeopleEvents.CONTACT_ID, contact.getContactID());
        values.put(PeopleEventsContract.PeopleEvents.DISPLAY_NAME, contact.getDisplayName().toString());

        Birthday birthday = contact.getBirthday();
        values.put(PeopleEventsContract.PeopleEvents.DATE, birthday.toShortDate());

        values.put(PeopleEventsContract.PeopleEvents.SOURCE, ContactSource.DEVICE);
        values.put(PeopleEventsContract.PeopleEvents.EVENT_TYPE, PeopleEventsContract.PeopleEvents.TYPE_BIRTHDAY);

        return values;
    }
}
