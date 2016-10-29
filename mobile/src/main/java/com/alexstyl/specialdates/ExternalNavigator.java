package com.alexstyl.specialdates;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.actions.IntentAction;
import com.alexstyl.specialdates.util.Utils;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;

public class ExternalNavigator {

    public static final Uri GOOGLE_PLUS_COMMUNITY = Uri.parse("https://plus.google.com/u/0/communities/112144353599130693487");

    private final Activity activity;
    private final Analytics analytics;

    public ExternalNavigator(Activity activity, Analytics analytics) {
        this.activity = activity;
        this.analytics = analytics;
        SimpleChromeCustomTabs.initialize(activity);
    }

    public boolean canGoToPlayStore() {
        Intent intent = buildPlayStoreIntent();
        return canResolveIntent(intent);
    }

    private boolean canResolveIntent(Intent intent) {
        return activity.getPackageManager().resolveActivity(intent, 0) != null;
    }

    public void toPlayStore() {
        try {
            Intent intent = buildPlayStoreIntent();
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    private Intent buildPlayStoreIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + activity.getPackageName()));
        return intent;
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
            intent.setPackage("com.google.android.apps.plus");
            intent.setData(GOOGLE_PLUS_COMMUNITY);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public boolean canGoToEmailSupport() {
        return canResolveIntent(Utils.getSupportEmailIntent(activity));
    }

    public void toEmailSupport() {
        Utils.openIntentSafely(activity, new IntentAction() {
            @Override
            public void onStartAction(Context context) throws ActivityNotFoundException {
                Intent intent = Utils.getSupportEmailIntent(context);
                context.startActivity(intent);
            }

            @Override
            public String getName() {
                return "email support";
            }
        });
    }

    public void connectTo(Activity activity) {
        SimpleChromeCustomTabs.getInstance().connectTo(activity);
    }

    public void disconnectTo(Activity activity) {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(activity);
    }
}
