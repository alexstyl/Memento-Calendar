package com.alexstyl.specialdates;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    private final Context context;

    AppModule(Context appContext) {
        this.context = appContext;
    }

    @Provides
    Context appContext() {
        return context;
    }
}
