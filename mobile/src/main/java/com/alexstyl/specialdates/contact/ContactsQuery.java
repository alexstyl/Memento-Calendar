package com.alexstyl.specialdates.contact;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.util.Utils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class ContactsQuery {
    public final static Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;

    public static String COL_DISPLAY_NAME = Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY //3
            : ContactsContract.Contacts.DISPLAY_NAME;

    public static String COL_LOOKUP = ContactsContract.Contacts.LOOKUP_KEY;

    @SuppressLint("InlinedApi")
    public final static String SORT_ORDER = Utils.hasHoneycomb() ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    @SuppressLint("InlinedApi")
    public static final String[] PROJECTION = {
            ContactsContract.Data.MIMETYPE,//0
            ContactsContract.Data.CONTACT_ID,//1
            COL_LOOKUP, //2
            COL_DISPLAY_NAME,//3
            ContactsContract.CommonDataKinds.Event.START_DATE, //4
    };

    public static final int ROW_TYPE = 0;

    public static final int ID = 1;
    public static final int LOOKUP_KEY = 2;
    public static final int DISPLAY_NAME = 3;
    public static final int BIRTHDAY = 4;

    public static boolean isBirthdayRow(Cursor cursor) {
        return cursor.getString(ROW_TYPE).equals(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE);
    }
}
