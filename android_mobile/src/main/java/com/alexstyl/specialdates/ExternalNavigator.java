package com.alexstyl.specialdates;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.facebook.friendimport.FacebookContact;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;

public class ExternalNavigator {

    private static Uri createPlayStoreUri() {
        String packageName = MementoApplication.getContext().getPackageName();
        return Uri.parse("market://details?id=" + packageName);
    }

    private final Activity activity;
    private final Analytics analytics;

    public ExternalNavigator(Activity activity, Analytics analytics) {
        this.activity = activity;
        this.analytics = analytics;
        SimpleChromeCustomTabs.initialize(activity);
    }

    public void toPlayStore() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, createPlayStoreUri());
            activity.startActivity(intent);
            analytics.trackScreen(Screen.PLAY_STORE);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public void connectTo(Activity activity) {
        SimpleChromeCustomTabs.getInstance().connectTo(activity);
    }

    public void disconnectTo(Activity activity) {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(activity);
    }

    public void toContactDetails(Contact contact) {
        if (contact instanceof FacebookContact) {
            toFacebookContactDetails(contact);
        } else {
            toDeviceContactDetails(contact);
        }
    }

    private void toFacebookContactDetails(Contact contact) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/" + contact.getContactID()));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void toDeviceContactDetails(Contact contact) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(Contacts.CONTENT_URI, String.valueOf(contact.getContactID()));
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }

    public void toFacebookPage() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/memento.calendar/"));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
        analytics.trackScreen(Screen.FACEBOOK_PAGE);
    }
}

