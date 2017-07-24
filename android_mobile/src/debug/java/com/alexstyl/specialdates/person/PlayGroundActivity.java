package com.alexstyl.specialdates.person;

import android.os.Bundle;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;

import java.net.URI;
import java.util.List;

public class PlayGroundActivity extends ThemedMementoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidContactActionsProvider actionsProvider = new AndroidContactActionsProvider(getContentResolver());

        List<ContactActionViewModel> actions = actionsProvider.buildActionsFor(new Contact(250L, DisplayName.NO_NAME, URI.create(""), ContactSource.SOURCE_DEVICE));

        print("done");

    }

    private void print(String messsage) {
        android.util.Log.d("TEST", messsage);
    }
}
