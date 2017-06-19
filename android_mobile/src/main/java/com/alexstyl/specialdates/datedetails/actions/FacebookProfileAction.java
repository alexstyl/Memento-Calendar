package com.alexstyl.specialdates.datedetails.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class FacebookProfileAction implements IntentAction {

    private static final String DATA_PROFILE = "fb://profile/";
    private static final String FB_PAGE_NAME = "days.official";
    private static final String URL_FACEBOOK = "https://m.facebook.com/";

    private final long contactID;

    FacebookProfileAction(long contactID) {
        this.contactID = contactID;
    }

    @Override
    public void onStartAction(Context context) throws ActivityNotFoundException {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(DATA_PROFILE + contactID));
            context.startActivity(i);

        } catch (ActivityNotFoundException e) {
            // the fb app is not installed. open the browser instead
            Intent i = new Intent(Intent.ACTION_VIEW);

            i.setData(Uri.parse(URL_FACEBOOK + contactID));
            context.startActivity(i);

        }
    }

    @Override
    public String getAnalyticsName() {
        return "facebook_profile";
    }
}
