package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;

final class ContactsObserver extends ContentObserver {

    private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
    private final ContentResolver resolver;

    private Callback callback;

    ContactsObserver(ContentResolver resolver) {
        super(new Handler());
        this.resolver = resolver;
    }

    void startObserving(Callback callback) {
        this.callback = callback;
        resolver.registerContentObserver(URI, false, this);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        callback.onContactsUpdated();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        callback.onContactsUpdated();
    }

    public void stopObserving() {
        resolver.unregisterContentObserver(this);
    }

    public interface Callback {
        void onContactsUpdated();
    }
}
