package com.alexstyl.specialdates.events.bankholidays;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class BankHolidaysPreferences {

    private final EasyPreferences preferences;

    public static BankHolidaysPreferences newInstance(Context context) {
        EasyPreferences preferences = EasyPreferences.createForDefaultPreferences(context);
        return new BankHolidaysPreferences(preferences);
    }

    BankHolidaysPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    public boolean isEnabled() {
        return preferences.getBoolean(R.string.key_enable_bank_holidays, R.bool.isBankholidaysSupported);
    }
}
