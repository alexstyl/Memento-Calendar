package com.alexstyl.specialdates.analytics;

import android.content.Context;

import com.alexstyl.specialdates.BuildConfig;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class AnalyticsProvider {

    private static Analytics ANALYTICS;

    public static Analytics getAnalytics(Context context) {
        if (ANALYTICS == null) {
            ANALYTICS = createMixpanelAnalytics(context);
        }
        return ANALYTICS;
    }

    private static Analytics createMixpanelAnalytics(Context context) {
        String projectToken = BuildConfig.MIXPANEL_TOKEN;
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, projectToken);
        return new MixPanel(mixpanel);
    }

}
