package com.alexstyl.specialdates.contact;

import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;

public final class ContactsQuery {

    public final static Uri CONTENT_URI = Data.CONTENT_URI;
    public final static String SORT_ORDER = Data.CONTACT_ID;
    public static final String[] PROJECTION = {
            Data.CONTACT_ID,//0
            Contacts.LOOKUP_KEY, //1
            Contacts.DISPLAY_NAME_PRIMARY,//2
            ContactsContract.CommonDataKinds.Event.START_DATE, //3
    };

    public static final int CONTACT_ID = 0;
    public static final int LOOKUP_KEY = 1;
    public static final int DISPLAY_NAME = 2;
}
