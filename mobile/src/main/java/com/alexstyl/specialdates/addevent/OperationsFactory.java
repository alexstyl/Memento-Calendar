package com.alexstyl.specialdates.addevent;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.Event;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.images.DecodedImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class OperationsFactory {

    private static final int NO_RAW_CONTACT_ID = 0;

    private final int rawContactID;

    static OperationsFactory forNewContact() {
        return new OperationsFactory(NO_RAW_CONTACT_ID);
    }

    OperationsFactory(int rawContactID) {
        this.rawContactID = rawContactID;
    }

    ContentProviderOperation newInsertFor(EventType eventType, Date date) {
        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValue(Data.MIMETYPE, CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Event.TYPE, eventType.getAndroidType())
                .withValue(CommonDataKinds.Event.START_DATE, date.toShortDate());
        addRawContactID(builder);
        return builder.build();
    }

    private void addRawContactID(ContentProviderOperation.Builder builder) {
        if (rawContactID == NO_RAW_CONTACT_ID) {
            builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactID);
        } else {
            builder.withValue(Data.RAW_CONTACT_ID, rawContactID);

        }
    }

    ArrayList<ContentProviderOperation> deleteEvents(List<ContactEvent> contactEvents) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (ContactEvent contactEvent : contactEvents) {
            long eventId = contactEvent.getDeviceEventId().get();
            ops.add(
                    ContentProviderOperation
                            .newDelete(Data.CONTENT_URI)
                            .withSelection(CommonDataKinds.Event._ID + "= " + eventId, null)
                            .build());
        }
        return ops;
    }

    ArrayList<ContentProviderOperation> createContactIn(AccountData account, String contactName) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>(2);
        ops.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.getAccountName())
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.getAccountType())
                        .build());
        ops.add(
                ContentProviderOperation.newInsert(Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, contactName)
                        .build());

        return ops;
    }

    List<ContentProviderOperation> insertEvents(Collection<Event> events) {
        List<ContentProviderOperation> operations = new ArrayList<>(events.size());
        for (Event entry : events) {
            EventType eventType = entry.getEventType();
            Date date = entry.getDate();
            ContentProviderOperation operation = newInsertFor(eventType, date);
            operations.add(operation);
        }
        return operations;
    }

    ContentProviderOperation insertImageToContact(DecodedImage image) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
                .withValue(Photo.PHOTO, image.getBytes());
        addRawContactID(builder);
        return builder.build();
    }

    ContentProviderOperation updateImageContact(DecodedImage decodedImage) {
        return ContentProviderOperation
                .newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                        Data.RAW_CONTACT_ID + "= ?" + " AND " + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{String.valueOf(rawContactID),
                                CommonDataKinds.Photo.CONTENT_ITEM_TYPE}
                )
                .withValue(CommonDataKinds.Photo.PHOTO, decodedImage.getBytes())
                .build();
    }

    ContentProviderOperation deleteImageFor(Contact contact) {
        return ContentProviderOperation.newDelete(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, rawContactID)
                .withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
                .withSelection(Photo.CONTACT_ID + "=?", new String[]{String.valueOf(contact.getContactID())})
                .build();
    }
}
