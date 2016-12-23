package com.alexstyl.specialdates.addevent;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class OperationsFactory {
    private final int rawContactID;

    static OperationsFactory forNewContact() {
        return new OperationsFactory(0);
    }

    OperationsFactory(int rawContactID) {
        this.rawContactID = rawContactID;
    }

    ContentProviderOperation newInsertFor(EventType eventType, Date date) {
        return ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValue(Event.RAW_CONTACT_ID, rawContactID)
                .withValue(Event.TYPE, eventType.getAndroidType())
                .withValue(Event.MIMETYPE, Event.CONTENT_ITEM_TYPE)
                .withValue(Event.START_DATE, date.toShortDate())
                .build();
    }

    ArrayList<ContentProviderOperation> deleteEvents(List<ContactEvent> contactEvents) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (ContactEvent contactEvent : contactEvents) {
            long eventId = contactEvent.getDeviceEventId().get();
            ops.add(
                    ContentProviderOperation
                            .newDelete(Data.CONTENT_URI)
                            .withSelection(Event._ID + "= " + eventId, null)
                            .build());
        }
        return ops;
    }

    List<ContentProviderOperation> createContactIn(AccountData account, String contactName) {
        List<ContentProviderOperation> ops = new ArrayList<>(2);
        ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.getAccountName())
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.getAccountType())
                        .build());
        ops.add(ContentProviderOperation
                        .newInsert(Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, contactName)
                        .build());

        return ops;
    }

    List<ContentProviderOperation> insertEvents(Set<Map.Entry<EventType, Date>> events) {
        List<ContentProviderOperation> operations = new ArrayList<>(events.size());
        for (Map.Entry<EventType, Date> entry : events) {
            EventType eventType = entry.getKey();
            Date date = entry.getValue();
            ContentProviderOperation operation = newInsertFor(eventType, date);
            operations.add(operation);
        }
        return operations;
    }
}
