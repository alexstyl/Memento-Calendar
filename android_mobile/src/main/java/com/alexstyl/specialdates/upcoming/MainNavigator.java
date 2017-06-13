package com.alexstyl.specialdates.upcoming;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.ShareAppIntentCreator;
import com.alexstyl.specialdates.addevent.AddEventActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.donate.DonateActivity;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;
import com.novoda.simplechromecustomtabs.navigation.IntentCustomizer;
import com.novoda.simplechromecustomtabs.navigation.NavigationFallback;
import com.novoda.simplechromecustomtabs.navigation.SimpleChromeCustomTabsIntentBuilder;

final class MainNavigator {

    private static final Uri SUPPORT_URL = Uri.parse("https://g3mge.app.goo.gl/jdF1");

    private final AttributeExtractor attributeExtractor;
    private final Analytics analytics;
    private final Activity activity;
    private final StringResources stringResource;

    MainNavigator(Analytics analytics, Activity activity, StringResources stringResource) {
        this.analytics = analytics;
        this.activity = activity;
        this.stringResource = stringResource;
        this.attributeExtractor = new AttributeExtractor();
    }

    void toDonate() {
        if (hasPlayStoreInstalled()) {
            Intent intent = DonateActivity.createIntent(activity);
            activity.startActivity(intent);
        } else {
            SimpleChromeCustomTabs.getInstance()
                    .withFallback(new NavigationFallback() {
                        @Override
                        public void onFallbackNavigateTo(Uri url) {
                            navigateToDonateWebsite();
                        }
                    })
                    .withIntentCustomizer(intentCustomizer)
                    .navigateTo(SUPPORT_URL, activity);

        }
        analytics.trackScreen(Screen.DONATE);
    }

    private boolean hasPlayStoreInstalled() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void navigateToDonateWebsite() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(SUPPORT_URL);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ErrorTracker.track(e);
        }
    }

    void toAddEvent() {
        Intent intent = new Intent(activity, AddEventActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.ADD_EVENT);
    }

    void toSettings() {
        Intent intent = new Intent(activity, MainPreferenceActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.SETTINGS);
    }

    void toSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.SEARCH);
    }

    private final IntentCustomizer intentCustomizer = new IntentCustomizer() {
        @Override
        public SimpleChromeCustomTabsIntentBuilder onCustomiseIntent(SimpleChromeCustomTabsIntentBuilder simpleChromeCustomTabsIntentBuilder) {
            int toolbarColor = attributeExtractor.extractPrimaryColorFrom(activity);
            return simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor);
        }
    };

    void toDateDetails(Date dateSelected) {
        Intent intent = DateDetailsActivity.getStartIntent(activity, dateSelected);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.DATE_DETAILS);
    }

    void toAppInvite() {
        Intent intent = new ShareAppIntentCreator(activity, stringResource).buildIntent();
        String shareTitle = stringResource.getString(R.string.invite_title);
        activity.startActivity(Intent.createChooser(intent, shareTitle));
        analytics.trackAppInviteRequested();
    }
}
