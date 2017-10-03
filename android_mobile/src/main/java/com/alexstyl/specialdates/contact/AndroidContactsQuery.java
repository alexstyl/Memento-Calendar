package com.alexstyl.specialdates.contact;

import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

final class AndroidContactsQuery {

    public static final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
    static final String[] PROJECTION = {
            Contacts._ID,
            Contacts.DISPLAY_NAME_PRIMARY,
    };
    static final String SORT_ORDER = Contacts._ID;

    public static final int CONTACT_ID = 0;
    static final int DISPLAY_NAME = 1;
    public static final String _ID = Contacts._ID;

    private AndroidContactsQuery() {
        // hide this
    }
}
