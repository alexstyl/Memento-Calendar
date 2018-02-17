package com.alexstyl.specialdates.addevent;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.CrashAndErrorTracker;

import java.util.ArrayList;

final class ContactOperationsExecutor {

    private final ContentResolver contentResolver;
    private final CrashAndErrorTracker tracker;

    ContactOperationsExecutor(ContentResolver contentResolver, CrashAndErrorTracker tracker) {
        this.contentResolver = contentResolver;
        this.tracker = tracker;
    }

    boolean execute(ArrayList<ContentProviderOperation> operations) {
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            tracker.track(e);
        }
        return false;
    }

}
