package com.alexstyl.specialdates.date;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class DateModule {

    @Provides
    @Singleton
    DateLabelCreator labelCreator(Context context) {
        return new AndroidDateLabelCreator(context);
    }
}
