package com.alexstyl.specialdates.addevent;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.TimePeriod;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ContactEventPersister {

    private final ContentResolver contentResolver;
    private final WriteableAccountsProvider accountsProvider;
    private final PeopleEventsProvider peopleEventsProvider;

    ContactEventPersister(ContentResolver contentResolver, WriteableAccountsProvider accountsProvider, PeopleEventsProvider peopleEventsProvider) {
        this.contentResolver = contentResolver;
        this.accountsProvider = accountsProvider;
        this.peopleEventsProvider = peopleEventsProvider;
    }

    boolean updateExistingContact(Contact contact, Set<Map.Entry<EventType, Date>> events, byte[] image) {
        int rawContactID = rawContactID(contact);
        OperationsFactory operationsFactory = new OperationsFactory(rawContactID);
        List<ContactEvent> contactEvents = getAllDeviceEventsFor(contact);
        ArrayList<ContentProviderOperation> operations = operationsFactory.deleteEvents(contactEvents);
        operations.addAll(operationsFactory.insertEvents(events));
        operations.add(operationsFactory.insertImageToContact(image));
        return execute(operations);
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

    boolean createNewContactWithEvents(String contactName, TemporaryEventsState state, byte[] image) {
        OperationsFactory factory = OperationsFactory.forNewContact();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.addAll(factory.createContactIn(getAccountToStoreContact(), contactName));
        operations.addAll(factory.insertEvents(state.getEvents()));
        operations.add(factory.insertImageToContact(image));
        return execute(operations);
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

    private boolean execute(ArrayList<ContentProviderOperation> operations) {
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            ErrorTracker.track(e);
        }
        return false;
    }
}
