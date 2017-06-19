package com.alexstyl.specialdates.datedetails.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class FacebookMessengerAction implements IntentAction {

    private static final String URL_MESSENGER = "https://www.facebook.com/messages/";
    private static final String DATA_COMPOSE = "fb://messaging/compose/";

    private final long contactID;

    FacebookMessengerAction(long contactID) {
        this.contactID = contactID;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(DATA_COMPOSE + contactID));

            context.startActivity(i);

        } catch (ActivityNotFoundException e) {
            // the fb app is not installed. open the browser instead
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(URL_MESSENGER + contactID));
            context.startActivity(i);

        }
    }

    @Override
    public String getAnalyticsName() {
        return "fb_messenger";
    }
}
