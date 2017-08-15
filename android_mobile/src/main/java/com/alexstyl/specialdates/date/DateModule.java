package com.alexstyl.specialdates.date;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class DateModule {

    private final Context context;

    public DateModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    DateLabelCreator labelCreator() {
        return new AndroidDateLabelCreator(context);
    }
}
