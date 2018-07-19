package com.novoda.simplechromecustomtabs.navigation;

import android.app.Activity;
import android.net.Uri;

import com.novoda.simplechromecustomtabs.connection.Connection;

public interface WebNavigator {

    WebNavigator withFallback(NavigationFallback navigationFallback);

    WebNavigator withIntentCustomizer(IntentCustomizer intentCustomizer);

    void navigateTo(Uri url, Activity activityContext);

    void release();

    class Creator {

        private Creator() {
            throw new IllegalStateException("Shouldn't be instantiated");
        }

        public static WebNavigator create(Connection connection) {
            return new SimpleChromeCustomTabsWebNavigator(connection);
        }
    }
}
