package com.novoda.simplechromecustomtabs.navigation;

import android.app.Activity;
import android.net.Uri;

public interface Navigator {

    Navigator withFallback(NavigationFallback navigationFallback);

    Navigator withIntentCustomizer(IntentCustomizer intentCustomizer);

    void navigateTo(Uri url, Activity activityContext);

}
