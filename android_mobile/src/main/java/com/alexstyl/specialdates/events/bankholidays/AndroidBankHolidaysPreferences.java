package com.alexstyl.specialdates.events.bankholidays;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class AndroidBankHolidaysPreferences implements BankHolidaysPreferences {

    private final EasyPreferences preferences;

    public static AndroidBankHolidaysPreferences newInstance(Context context) {
        EasyPreferences preferences = EasyPreferences.createForDefaultPreferences(context);
        return new AndroidBankHolidaysPreferences(preferences);
    }

    AndroidBankHolidaysPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean isEnabled() {
        return preferences.getBoolean(R.string.key_enable_bank_holidays, R.bool.isBankholidaysSupported);
    }
}
