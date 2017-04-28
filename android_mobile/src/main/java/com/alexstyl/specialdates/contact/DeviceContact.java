package com.alexstyl.specialdates.contact;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.actions.ContactActionFactory;
import com.alexstyl.specialdates.contact.actions.LabeledAction;
import com.alexstyl.specialdates.util.ContactUtils;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Displays the information window for this contact.
     * </p>
     */
    @Override
    public void displayQuickInfo(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
            intent.setData(uri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected List<LabeledAction> onBuildActions(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean hasPhoneNumber = ContactUtils.hasPhoneNumber(resolver, getContactID());
        List<LabeledAction> actions = new ArrayList<>(2);
        if (hasPhoneNumber) {
            actions.add(ContactActionFactory.get().createCallAction(getContactID()));
            actions.add(ContactActionFactory.get().createSMSAction(getContactID()));
        }

        boolean hasEmails = ContactUtils.hasEmail(resolver, getContactID());
        if (hasEmails) {
            actions.add(ContactActionFactory.get().createEmailAction(getContactID()));
        }

        return actions;
    }

}
