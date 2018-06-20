package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.analytics.Analytics;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchModule {

    @Provides
    SearchNavigator navigator(Analytics analytics) {
        return new SearchNavigator(analytics);
    }
}
