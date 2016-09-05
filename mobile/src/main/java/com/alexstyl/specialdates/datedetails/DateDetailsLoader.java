package com.alexstyl.specialdates.datedetails;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.EventType;
import com.alexstyl.specialdates.events.PeopleEventsContract.PeopleEvents;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.util.ContactsObserver;
import com.novoda.notils.logger.simple.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DateDetailsLoader extends SimpleAsyncTaskLoader<List<ContactEvent>> {

    private final DayDate date;
    private final ContactProvider contactProvider;
    private final ContactsObserver contactsObserver;

    private final Comparator<ContactEvent> displayNameComparator = new SpecialDateDisplayNameComparator();

    public static DateDetailsLoader newInstance(Context context, DayDate date) {
        context = context.getApplicationContext();
        ContactProvider contactProvider = ContactProvider.get(context);
        ContactsObserver contactsObserver = new ContactsObserver(context.getContentResolver(), new Handler());
        return new DateDetailsLoader(context, date, contactProvider, contactsObserver);
    }

    DateDetailsLoader(Context context, DayDate date, ContactProvider contactProvider, ContactsObserver contactsObserver) {
        super(context);
        this.date = date;
        this.contactProvider = contactProvider;
        this.contactsObserver = contactsObserver;

        contactsObserver.registerWith(
                new ContactsObserver.Callback() {
                    @Override
                    public void onContactsUpdated() {
                        onContentChanged();
                    }
                }
        );
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        contactsObserver.unregister();
    }

    @Override
    public List<ContactEvent> loadInBackground() {
        Cursor cursor = queryEventsOfDate();
        // TODO handle events for multiple people
        List<ContactEvent> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                Contact contact = buildContactFrom(cursor);
                EventType eventType = PeopleEvents.getEventType(cursor);
                ContactEvent contactEvent = new ContactEvent(eventType, date, contact);

                result.add(contactEvent);
            } catch (ContactNotFoundException e) {
                Log.w(e);
            }
        }
        cursor.close();
        Collections.sort(result, displayNameComparator);
        return result;
    }

    private Cursor queryEventsOfDate() {
        return getContentProvider().query(
                PeopleEvents.CONTENT_URI,
                null,
                PeopleEvents.DATE + " = ?", new String[]{date.toShortDate()},
                PeopleEvents.SORT_ORDER_DEFAULT
        );
    }

    @Nullable
    private Contact buildContactFrom(Cursor cursor) throws ContactNotFoundException {
        long contactId = PeopleEvents.getContactIdFrom(cursor);
        return contactProvider.getOrCreateContact(contactId);
    }

    private ContentResolver getContentProvider() {
        return getContext().getContentResolver();
    }

    private class SpecialDateDisplayNameComparator implements Comparator<ContactEvent> {
        Collator col;

        {
            col = Collator.getInstance();
            col.setStrength(Collator.SECONDARY);
        }

        @Override
        public int compare(ContactEvent lhs, ContactEvent rhs) {
            return col.compare(lhs.getContact().getDisplayName().toString(), rhs.getContact().getDisplayName().toString());
        }

    }
}
