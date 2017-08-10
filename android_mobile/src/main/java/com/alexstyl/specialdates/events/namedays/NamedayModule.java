package com.alexstyl.specialdates.events.namedays;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class NamedayModule {

    private final Context context;

    public NamedayModule(Context context) {
        this.context = context;
    }

    @Provides
    NamedayUserSettings userSettings() {
        return new NamedayPreferences(context);
    }
}
