package com.alexstyl.specialdates.donate;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class DonationPreferences {

    private final EasyPreferences preferences;

    public static DonationPreferences newInstance(Context context) {
        EasyPreferences preferences = EasyPreferences.createForDefaultPreferences(context);
        return new DonationPreferences(preferences);
    }

    private DonationPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    void markAsDonated() {
        preferences.setBoolean(R.string.key_has_donated, true);
    }

    public boolean hasDonated() {
        return preferences.getBoolean(R.string.key_has_donated, false);
    }
}
