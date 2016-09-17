package com.alexstyl.specialdates;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.about.AboutActivity;
import com.alexstyl.specialdates.addevent.AddBirthdayActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.actions.IntentAction;
import com.alexstyl.specialdates.permissions.ContactPermissionActivity;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.support.SupportDonateDialog;
import com.alexstyl.specialdates.util.Utils;

public class Navigator {

    public static final Uri GOOGLE_PLUS_COMMUNITY = Uri.parse("https://plus.google.com/u/0/communities/112144353599130693487");

    private final Activity activity;
    private final Analytics analytics;

    public Navigator(Activity activity, Analytics analytics) {
        this.activity = activity;
        this.analytics = analytics;
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

    @NonNull
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

    public void toAddBirthday() {
        Intent intent = new Intent(activity, AddBirthdayActivity.class);
        activity.startActivity(intent);
    }

    public void toSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    public void toAbout() {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.ABOUT);
    }

    public void toDonateDialog() {
        Intent intent = new Intent(activity, SupportDonateDialog.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.DONATE);
    }

    public void toSettings() {
        Intent intent = new Intent(activity, MainPreferenceActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.SETTINGS);
    }

    public void toContactPermissionRequired(int requestCode) {
        Intent intent = new Intent(activity, ContactPermissionActivity.class);
        activity.startActivityForResult(intent, requestCode);
        analytics.trackScreen(Screen.CONTACT_PERMISSION_REQUESTED);
    }
}
