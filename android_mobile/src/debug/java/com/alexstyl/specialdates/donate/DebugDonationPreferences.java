package com.alexstyl.specialdates.donate;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public final class DebugDonationPreferences {

    private final EasyPreferences easyPreferences;
    private final DonateMonitor donateMonitor;

    public static DebugDonationPreferences newInstance(Context context, DonateMonitor donateMonitor) {
        return new DebugDonationPreferences(EasyPreferences.Companion.createForDefaultPreferences(context), donateMonitor);
    }

    private DebugDonationPreferences(EasyPreferences easyPreferences, DonateMonitor donateMonitor) {
        this.easyPreferences = easyPreferences;
        this.donateMonitor = donateMonitor;
    }

    public void reset() {
        easyPreferences.setBoolean(R.string.key_has_donated, false);
        donateMonitor.onDonationUpdated();
    }
}
