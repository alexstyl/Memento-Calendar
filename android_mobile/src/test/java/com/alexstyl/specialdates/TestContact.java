package com.alexstyl.specialdates;

import android.content.Context;
import android.net.Uri;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.datedetails.actions.LabeledAction;

import java.util.List;

public class TestContact extends Contact {

    public TestContact(long id, DisplayName displayName) {
        super(id, displayName);
    }

    @Override
    protected List<LabeledAction> onBuildActions(Context context) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri getLookupUri() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri getImagePath() {
        throw new UnsupportedOperationException("Not supported");
    }

}
