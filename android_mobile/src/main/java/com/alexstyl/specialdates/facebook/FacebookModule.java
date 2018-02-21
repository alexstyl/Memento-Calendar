package com.alexstyl.specialdates.facebook;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

import dagger.Module;
import dagger.Provides;

@Module
public class FacebookModule {

    private final Context context;

    public FacebookModule(Context context) {
        this.context = context;
    }

    @Provides
    FacebookUserSettings userSettings() {
        return new AndroidFacebookPreferences(EasyPreferences.createForPrivatePreferences(context, R.string.pref_facebook));
    }
}
