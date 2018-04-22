package com.alexstyl.specialdates.addevent;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AddEventModule {

//    @Provides
//    AddContactEventsPresenter presenter() {
//        return AddContactEventsPresenter();
//    }

    @Provides
    FilePathProvider filePathProvider(Context context) {
        return new FilePathProvider(context);
    }
}
