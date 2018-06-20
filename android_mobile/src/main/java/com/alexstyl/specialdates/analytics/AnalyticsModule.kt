package com.alexstyl.specialdates.analytics

import android.content.Context

import com.alexstyl.specialdates.common.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.mixpanel.android.mpmetrics.MixpanelAPI

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
@Singleton
class AnalyticsModule {

    @Provides
    @Singleton
    internal fun providesAnalytics(context: Context): Analytics {
        return CompositeAnalytics(buildMixPanel(context), buildFirebase(context))
    }

    private fun buildFirebase(context: Context) =
            FirebaseAnalyticsImpl(FirebaseAnalytics.getInstance(context))

    private fun buildMixPanel(context: Context): MixPanel {
        val projectToken = BuildConfig.MIXPANEL_TOKEN
        val mixpanel = MixpanelAPI.getInstance(context, projectToken)
        val analytics = MixPanel(mixpanel)
        return analytics
    }
}
