package com.alexstyl.specialdates.util;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;

public class ContactsObserver {

    private final Handler handler;
    private final ContentResolver contentResolver;

    private Callback callback;
    private ContentObserver observer;
    private boolean contactsWereUpdated = false;

    public ContactsObserver(ContentResolver resolver, Handler handler) {
        this.contentResolver = resolver;
        this.handler = handler;
    }

    public void register() {
        Uri CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI;
        if (observer == null) {
            observer = createObserver();
        }
        contentResolver.registerContentObserver(CONTACTS_URI, true, observer);
    }

    private ContentObserver createObserver() {
        return new ContentObserver(handler) {

            @Override
            public void onChange(boolean selfChange) {
                contactsWereUpdated = true;
                if (callback != null) {
                    callback.onContactsUpdated();
                }
            }
        };
    }

    public void registerWith(Callback callback) {
        this.callback = callback;
        register();
    }

    public void unregister() {
        callback = null;
        if (observer != null) {
            contentResolver.unregisterContentObserver(observer);
            observer = null;
        }
    }

    public void resetFlag() {
        contactsWereUpdated = false;
    }

    public boolean wereContactsUpdated() {
        return contactsWereUpdated;
    }

    public interface Callback {
        void onContactsUpdated();
    }
}
