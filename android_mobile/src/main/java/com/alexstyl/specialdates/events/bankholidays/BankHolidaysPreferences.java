package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class BankHolidaysPreferences implements BankHolidaysUserSettings {

    private final EasyPreferences preferences;

    BankHolidaysPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean isEnabled() {
        return preferences.getBoolean(R.string.key_enable_bank_holidays, R.bool.isBankholidaysSupported);
    }
}
