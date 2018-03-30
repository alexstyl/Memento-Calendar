package com.alexstyl.specialdates.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.ShareAppIntentCreator;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.addevent.AddEventActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DonateActivity;
import com.alexstyl.specialdates.events.namedays.activity.NamedayActivity;
import com.alexstyl.specialdates.facebook.FacebookProfileActivity;
import com.alexstyl.specialdates.facebook.FacebookUserSettings;
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity;
import com.alexstyl.specialdates.permissions.ContactPermissionActivity;
import com.alexstyl.specialdates.person.PersonActivity;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;
import com.novoda.simplechromecustomtabs.navigation.IntentCustomizer;
import com.novoda.simplechromecustomtabs.navigation.NavigationFallback;
import com.novoda.simplechromecustomtabs.navigation.SimpleChromeCustomTabsIntentBuilder;

public final class HomeNavigator {

    private static final Uri SUPPORT_URL = Uri.parse("https://g3mge.app.goo.gl/jdF1");

    private final AttributeExtractor attributeExtractor;
    private final Analytics analytics;
    private final Strings strings;
    private final FacebookUserSettings facebookUserSettings;
    private final CrashAndErrorTracker tracker;

    public HomeNavigator(Analytics analytics, Strings strings, FacebookUserSettings facebookUserSettings, CrashAndErrorTracker tracker) {
        this.analytics = analytics;
        this.strings = strings;
        this.facebookUserSettings = facebookUserSettings;
        this.tracker = tracker;
        this.attributeExtractor = new AttributeExtractor();
    }

    public void toDonate(final Activity activity) {
        if (hasPlayStoreInstalled(activity)) {
            Intent intent = DonateActivity.createIntent(activity);
            activity.startActivity(intent);
        } else {
            SimpleChromeCustomTabs.getInstance()
                    .withFallback(new NavigationFallback() {
                        @Override
                        public void onFallbackNavigateTo(Uri url) {
                            navigateToDonateWebsite(activity);
                        }
                    })
                    .withIntentCustomizer(new IntentCustomizer() {
                        @Override
                        public SimpleChromeCustomTabsIntentBuilder onCustomiseIntent(SimpleChromeCustomTabsIntentBuilder simpleChromeCustomTabsIntentBuilder) {
                            int toolbarColor = attributeExtractor.extractPrimaryColorFrom(activity);
                            return simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor);
                        }
                    })
                    .navigateTo(SUPPORT_URL, activity);

        }
        analytics.trackScreen(Screen.DONATE);
    }

    private boolean hasPlayStoreInstalled(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void navigateToDonateWebsite(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(SUPPORT_URL);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            tracker.track(e);
        }
    }

    public void toAddEvent(Activity activity) {
        Intent intent = new Intent(activity, AddEventActivity.class);
        activity.startActivity(intent);
    }

    public void toFacebookImport(Activity activity) {
        if (facebookUserSettings.isLoggedIn()) {
            Intent intent = new Intent(activity, FacebookProfileActivity.class);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, FacebookLogInActivity.class);
            activity.startActivity(intent);
        }
    }

    public void toSettings(Activity activity) {
        Intent intent = new Intent(activity, MainPreferenceActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.SETTINGS);
    }

    void toSearch(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.SEARCH);
    }

    public void toDateDetails(Date dateSelected, Activity activity) {
        Intent intent = NamedayActivity.Companion.getStartIntent(activity, dateSelected);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.DATE_DETAILS);
    }

    public void toAppInvite(Activity activity) {
        Intent intent = new ShareAppIntentCreator(strings).buildIntent();
        String shareTitle = strings.inviteFriend();
        activity.startActivity(Intent.createChooser(intent, shareTitle));
        analytics.trackAppInviteRequested();
    }

    public void toGithubPage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/alexstyl/Memento-Calendar"));
        analytics.trackVisitGithub();
        activity.startActivity(intent);
    }

    public void toContactDetails(Contact contact, Activity activity) {
        Intent intent = PersonActivity.Companion.buildIntentFor(activity, contact);
        activity.startActivity(intent);
        analytics.trackContactDetailsViewed(contact);
    }

    public void toContactPermission(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ContactPermissionActivity.class);
        activity.startActivityForResult(intent, requestCode);
        analytics.trackScreen(Screen.CONTACT_PERMISSION_REQUESTED);
    }
}
