package com.novoda.simplechromecustomtabs.navigation;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import com.novoda.simplechromecustomtabs.connection.Connection;

class SimpleChromeCustomTabsWebNavigator implements WebNavigator {

    private final Connection connection;
    NavigationFallback navigationFallback;
    IntentCustomizer intentCustomizer;

    SimpleChromeCustomTabsWebNavigator(Connection connection) {
        this.connection = connection;
    }

    @Override
    public WebNavigator withFallback(NavigationFallback navigationFallback) {
        this.navigationFallback = navigationFallback;
        return this;
    }

    @Override
    public WebNavigator withIntentCustomizer(IntentCustomizer intentCustomizer) {
        this.intentCustomizer = intentCustomizer;
        return this;
    }

    @Override
    public void navigateTo(Uri url, Activity activityContext) {
        if (connection.isConnected()) {
            buildIntent().launchUrl(activityContext, url);
        } else if (hasNavigationFallback()) {
            navigationFallback.onFallbackNavigateTo(url);
        }
    }

    @Override
    public void release() {
        intentCustomizer = null;
        navigationFallback = null;
    }

    private CustomTabsIntent buildIntent() {
        SimpleChromeCustomTabsIntentBuilder basicIntentBuilder = SimpleChromeCustomTabsIntentBuilder.newInstance(connection);

        if (intentCustomizer == null) {
            return basicIntentBuilder.createIntent();
        }

        return intentCustomizer.onCustomiseIntent(basicIntentBuilder).createIntent();
    }

    private boolean hasNavigationFallback() {
        return navigationFallback != null;
    }

}
