package com.alexstyl.specialdates.ui.activity;

import android.app.Activity;
import android.content.Intent;

import com.alexstyl.specialdates.about.AboutActivity;
import com.alexstyl.specialdates.addevent.AddBirthdayActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.MainPreferenceActivity;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;

public class MainNavigator {

    private final Analytics analytics;
    private final Activity activity;

    static MainNavigator prepareFor(Activity activity, Analytics analytics) {
        SimpleChromeCustomTabs.initialize(activity);
        return new MainNavigator(analytics, activity);
    }

    private MainNavigator(Analytics analytics, Activity activity) {
        this.analytics = analytics;
        this.activity = activity;
    }

    void toDonate() {

        analytics.trackScreen(Screen.DONATE);
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
}
