package com.alexstyl.specialdates;

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
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.support.SupportDonateDialog;
import com.alexstyl.specialdates.util.Utils;

public class Navigator {

    public static final Uri GOOGLE_PLUS_COMMUNITY = Uri.parse("https://plus.google.com/u/0/communities/112144353599130693487");

    private final Context context;
    private final Analytics analytics;

    public Navigator(Context context, Analytics analytics) {
        this.context = context;
        this.analytics = analytics;
    }

    public boolean canGoToPlayStore() {
        Intent intent = buildPlayStoreIntent();
        return canResolveIntent(intent);
    }

    private boolean canResolveIntent(Intent intent) {
        return context.getPackageManager().resolveActivity(intent, 0) != null;
    }

    public void toPlayStore() {
        try {
            Intent intent = buildPlayStoreIntent();
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    @NonNull
    private Intent buildPlayStoreIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        return intent;
    }

    public void toGooglePlusCommunityBrowser() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(GOOGLE_PLUS_COMMUNITY);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public void toGooglePlusCommunityApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.apps.plus");
            intent.setData(GOOGLE_PLUS_COMMUNITY);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    public boolean canGoToEmailSupport() {
        return canResolveIntent(Utils.getSupportEmailIntent(context));
    }

    public void toEmailSupport() {
        Utils.openIntentSafely(context, new IntentAction() {
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
        Intent intent = new Intent(context, AddBirthdayActivity.class);
        context.startActivity(intent);
    }

    public void toSearch() {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public void toAbout() {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
        analytics.trackScreen(Screen.ABOUT);
    }

    public void toDonateDialog() {
        Intent intent = new Intent(context, SupportDonateDialog.class);
        context.startActivity(intent);
        analytics.trackScreen(Screen.DONATE);
    }

    public void toSettings() {
        Intent intent = new Intent(context, MainPreferenceActivity.class);
        context.startActivity(intent);
        analytics.trackScreen(Screen.SETTINGS);
    }
}
