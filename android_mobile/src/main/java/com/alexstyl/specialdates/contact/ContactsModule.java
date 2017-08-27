package com.alexstyl.specialdates.contact;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class ContactsModule {

    private final Context context;

    public ContactsModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    ContactsProvider provider() {
        return ContactsProvider.get(context);
    }
}
