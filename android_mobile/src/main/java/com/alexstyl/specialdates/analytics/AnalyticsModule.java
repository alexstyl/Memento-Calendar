package com.alexstyl.specialdates.analytics;

import android.content.Context;

import com.alexstyl.specialdates.common.BuildConfig;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class AnalyticsModule {
    private final Context context;

    public AnalyticsModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Analytics providesAnalytics() {
        String projectToken = BuildConfig.MIXPANEL_TOKEN;
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, projectToken);
        return new MixPanel(mixpanel);
    }
}
