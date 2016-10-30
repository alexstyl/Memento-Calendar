package com.alexstyl.specialdates;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.util.Utils;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;

public class ExternalNavigator {

    public static final Uri GOOGLE_PLUS_COMMUNITY = Uri.parse("https://plus.google.com/u/0/communities/112144353599130693487");
    private static final String GOOGLE_PLUS_PACKAGE_NAME = "com.google.android.apps.plus";
    private static final Uri URI_PLAYSTORE;
    private static final String NO_FRAGMENT = null;

    static {
        String packageName = MementoApplication.getContext().getPackageName();
        URI_PLAYSTORE = Uri.parse("market://details?id=" + packageName);
    }

    private final Activity activity;
    private final Analytics analytics;

    public ExternalNavigator(Activity activity, Analytics analytics) {
        this.activity = activity;
        this.analytics = analytics;
        SimpleChromeCustomTabs.initialize(activity);
    }

    public boolean canGoToPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, URI_PLAYSTORE);
        return canResolveIntent(intent);
    }

    public void toPlayStore() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, URI_PLAYSTORE);
            activity.startActivity(intent);
            analytics.trackScreen(Screen.PLAY_STORE);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public void toGooglePlusCommunityBrowser() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(GOOGLE_PLUS_COMMUNITY);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public void toGooglePlusCommunityApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(GOOGLE_PLUS_PACKAGE_NAME);
            intent.setData(GOOGLE_PLUS_COMMUNITY);
            activity.startActivity(intent);
            analytics.trackScreen(Screen.GOOGLE_PLUS_COMMUNITY);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public boolean canGoToEmailSupport() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "to", NO_FRAGMENT));
        return canResolveIntent(emailIntent);
    }

    public void toEmailSupport() {
        try {
            Intent intent = Utils.getSupportEmailIntent(activity);
            activity.startActivity(intent);
            analytics.trackScreen(Screen.EMAIL_SUPPORT);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, R.string.no_app_found, Toast.LENGTH_SHORT).show();
            ErrorTracker.track(ex);
        }

    }

    public void connectTo(Activity activity) {
        SimpleChromeCustomTabs.getInstance().connectTo(activity);
    }

    public void disconnectTo(Activity activity) {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(activity);
    }

    private boolean canResolveIntent(Intent intent) {
        return activity.getPackageManager().resolveActivity(intent, 0) != null;
    }
}
