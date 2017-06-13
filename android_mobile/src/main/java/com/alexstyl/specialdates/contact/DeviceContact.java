package com.alexstyl.specialdates.contact;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;

import com.alexstyl.specialdates.DisplayName;

public class DeviceContact extends Contact {

    private final String lookupKey;
    private final Uri avatarUri;

    public DeviceContact(long contactId, DisplayName displayName, String lookupKey) {
        super(contactId, displayName);
        this.lookupKey = lookupKey;
        this.avatarUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, getContactID());
    }

    @Override
    public Uri getLookupUri() {
        return Contacts.getLookupUri(getContactID(), lookupKey);
    }

    @Override
    public Uri getImagePath() {
        return avatarUri;
    }

}
