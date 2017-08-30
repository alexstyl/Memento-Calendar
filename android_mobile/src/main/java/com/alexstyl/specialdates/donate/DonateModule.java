package com.alexstyl.specialdates.donate;

import android.content.Context;

import com.alexstyl.specialdates.donate.util.IabHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class DonateModule {

    @Provides
    IabHelper providesIabHelper(Context context) {
        return new IabHelper(context, AndroidDonationConstants.PUBLIC_KEY);
    }

    @Provides
    DonationPreferences providesDonationPreferences(Context context) {
        return DonationPreferences.newInstance(context);
    }
}
