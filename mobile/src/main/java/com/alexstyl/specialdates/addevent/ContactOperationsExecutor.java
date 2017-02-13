package com.alexstyl.specialdates.addevent;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.ErrorTracker;

import java.util.ArrayList;

final class ContactOperationsExecutor {

    private final ContentResolver contentResolver;

    ContactOperationsExecutor(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    boolean execute(ArrayList<ContentProviderOperation> operations) {
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            ErrorTracker.track(e);
        }
        return false;
    }

}
