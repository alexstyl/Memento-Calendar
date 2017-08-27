package com.alexstyl.specialdates;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactSource;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;

import java.util.ArrayList;
import java.util.List;

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
        if (contact.getSource() == ContactSource.SOURCE_FACEBOOK) {
            toFacebookContactDetails(contact);
        } else if (contact.getSource() == ContactSource.SOURCE_DEVICE) {
            toDeviceContactDetails(contact);
        } else {
            throw new IllegalStateException("Invalid contact source " + contact.getSource());
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
            Intent intent = checkContactSomewhereElse(contact);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }

    private Intent checkContactSomewhereElse(Contact contact) {
        PackageManager packageManager = activity.getPackageManager();

        Intent intent = viewContact(contact);
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        String myPackage = activity.getPackageName();
        ArrayList<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo currentInfo : activities) {
            String packageName = currentInfo.activityInfo.packageName;
            if (!myPackage.equals(packageName)) {
                Intent targetIntent = viewContact(contact);
                intent.setPackage(packageName);
                targetIntents.add(targetIntent);
            }
        }
        if (targetIntents.isEmpty()) {
            throw new ActivityNotFoundException("NOOOOO!");
        } else {
            Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), "Open file with");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
            return chooserIntent;
        }
    }

    private Intent viewContact(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(Contacts.CONTENT_URI, String.valueOf(contact.getContactID()));
        intent.setData(uri);
        return intent;
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

