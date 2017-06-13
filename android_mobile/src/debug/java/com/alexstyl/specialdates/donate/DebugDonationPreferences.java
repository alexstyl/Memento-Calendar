package com.alexstyl.specialdates.donate;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public final class DebugDonationPreferences {

    private final EasyPreferences easyPreferences;

    private DebugDonationPreferences(EasyPreferences easyPreferences) {
        this.easyPreferences = easyPreferences;
    }

    public static DebugDonationPreferences newInstance(Context context) {
        return new DebugDonationPreferences(EasyPreferences.createForDefaultPreferences(context));
    }

    public void reset() {
        easyPreferences.setBoolean(R.string.key_has_donated, false);
        DonateMonitor.getInstance().onDonationUpdated();
    }
}
