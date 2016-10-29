package com.alexstyl.specialdates.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.about.AboutActivity;
import com.alexstyl.specialdates.addevent.AddBirthdayActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;
import com.novoda.simplechromecustomtabs.navigation.IntentCustomizer;
import com.novoda.simplechromecustomtabs.navigation.NavigationFallback;
import com.novoda.simplechromecustomtabs.navigation.SimpleChromeCustomTabsIntentBuilder;

public class MainNavigator {

    private static final Uri SUPPORT_URL = Uri.parse("http://paypal.me/alexstyl");
    private final AttributeExtractor attributeExtractor;
    private final Analytics analytics;
    private final Activity activity;

    MainNavigator(Analytics analytics, Activity activity) {
        this.analytics = analytics;
        this.activity = activity;
        this.attributeExtractor = new AttributeExtractor();
    }

    void toDonate() {
        SimpleChromeCustomTabs.getInstance()
                .withFallback(new NavigationFallback() {
                    @Override
                    public void onFallbackNavigateTo(Uri url) {
                        navigateToDonateWebsite();
                    }
                })
                .withIntentCustomizer(intentCustomizer)
                .navigateTo(SUPPORT_URL, activity);
        analytics.trackScreen(Screen.DONATE);
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

    void toAddBirthday() {
        Intent intent = new Intent(activity, AddBirthdayActivity.class);
        activity.startActivity(intent);
    }

    void toAbout() {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.ABOUT);
    }

    void toSettings() {
        Intent intent = new Intent(activity, MainPreferenceActivity.class);
        activity.startActivity(intent);
        analytics.trackScreen(Screen.SETTINGS);
    }

    public void toSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    private final IntentCustomizer intentCustomizer = new IntentCustomizer() {
        @Override
        public SimpleChromeCustomTabsIntentBuilder onCustomiseIntent(SimpleChromeCustomTabsIntentBuilder simpleChromeCustomTabsIntentBuilder) {
            int toolbarColor = attributeExtractor.extractPrimaryColorFrom(activity);
            return simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor);
        }
    };
}
