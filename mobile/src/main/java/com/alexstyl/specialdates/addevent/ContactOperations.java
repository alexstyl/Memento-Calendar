package com.alexstyl.specialdates.addevent;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.Event;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.images.DecodedImage;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.date.TimePeriod;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ContactOperations {

    private final ContentResolver contentResolver;
    private final WriteableAccountsProvider accountsProvider;
    private final PeopleEventsProvider peopleEventsProvider;

    ContactOperations(ContentResolver contentResolver, WriteableAccountsProvider accountsProvider, PeopleEventsProvider peopleEventsProvider) {
        this.contentResolver = contentResolver;
        this.accountsProvider = accountsProvider;
        this.peopleEventsProvider = peopleEventsProvider;
    }

    ContactOperationsBuilder updateExistingContact(Contact contact) {
        int rawContactID = rawContactID(contact);
        OperationsFactory operationsFactory = new OperationsFactory(rawContactID);
        List<ContactEvent> contactEvents = getAllDeviceEventsFor(contact);
        ArrayList<ContentProviderOperation> operations = operationsFactory.deleteEvents(contactEvents);
        return new ContactOperationsBuilder(operations, operationsFactory);
    }

    ContactOperationsBuilder createNewContact(String contactName) {
        OperationsFactory operationsFactory = OperationsFactory.forNewContact();
        ArrayList<ContentProviderOperation> operations = operationsFactory.createContactIn(getAccountToStoreContact(), contactName);
        return new ContactOperationsBuilder(operations, operationsFactory);
    }

    static class ContactOperationsBuilder {
        private final ArrayList<ContentProviderOperation> existingOperations;
        private final OperationsFactory operationsFactory;
        private Collection<Event> events;

        ContactOperationsBuilder(ArrayList<ContentProviderOperation> initialOperations, OperationsFactory operationsFactory) {
            existingOperations = initialOperations;
            this.operationsFactory = operationsFactory;
        }

        ContactOperationsBuilder withEvents(Collection<Event> events) {
            this.events = events;
            return this;
        }

        ContactOperationsBuilder addContactImage(DecodedImage decodedImage) {
            if (decodedImage != DecodedImage.EMPTY) {
                existingOperations.add(operationsFactory.insertImageToContact(decodedImage));
            }
            return this;
        }

        ContactOperationsBuilder updateContactImage(DecodedImage decodedImage) {
            existingOperations.add(operationsFactory.updateImageContact(decodedImage));
            return this;
        }

        public ArrayList<ContentProviderOperation> build() {
            ArrayList<ContentProviderOperation> operations = new ArrayList<>(existingOperations);
            if (events.size() > 0) {
                operations.addAll(operationsFactory.insertEvents(events));
            }
            return operations;
        }
    }

    private int rawContactID(Contact contact) {
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID};
        String selection = ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = ?";
        String[] selectionArgs = new String[]{
                String.valueOf(contact.getContactID())};
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null);

        throwIfInvalid(cursor);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return 0;
    }

    private List<ContactEvent> getAllDeviceEventsFor(Contact contact) {
        List<ContactEvent> contactEvents = new ArrayList<>();
        List<ContactEvent> contactEventsOnDate = peopleEventsProvider.getCelebrationDateFor(TimePeriod.aYearFromNow());
        for (ContactEvent contactEvent : contactEventsOnDate) {
            Contact dbContact = contactEvent.getContact();
            if (dbContact.getContactID() == contact.getContactID() && contactEvent.getType() != StandardEventType.NAMEDAY) {
                contactEvents.add(contactEvent);
            }
        }
        return contactEvents;
    }

    private AccountData getAccountToStoreContact() {
        ArrayList<AccountData> availableAccounts = accountsProvider.getAvailableAccounts();
        if (availableAccounts.size() == 0) {
            return AccountData.NO_ACCOUNT;
        } else {
            return availableAccounts.get(0);
        }
    }

    private static void throwIfInvalid(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            throw new DeveloperError("Cursor was invalid");
        }
    }
}
