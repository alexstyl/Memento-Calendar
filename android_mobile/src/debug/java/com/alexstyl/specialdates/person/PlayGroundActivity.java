package com.alexstyl.specialdates.person;

import android.os.Bundle;

import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PlayGroundActivity extends ThemedMementoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidContactCallActionsProvider actionsProvider = new AndroidContactCallActionsProvider(
                getContentResolver(),
                new AndroidStringResources(getResources()),
                new AndroidContactActionsFactory(thisActivity())
        );

        Contact contact = new Contact(250L, DisplayName.NO_NAME, URI.create(""), ContactSource.SOURCE_DEVICE);
        List<ContactAction> actions = new ArrayList<>();
        List<ContactAction> callActions = actionsProvider.callActionsFor(contact);
        actions.addAll(callActions);
        List<ContactAction> customActions = actionsProvider.customActionsFor(contact);
        actions.addAll(customActions);
        for (ContactAction action : actions) {
            print(action.toString());
        }
        print("done");

    }

    private void print(String messsage) {
        android.util.Log.d("TEST", messsage);
    }
}
