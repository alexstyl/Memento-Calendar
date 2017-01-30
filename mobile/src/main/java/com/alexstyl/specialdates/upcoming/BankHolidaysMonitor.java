package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;

class BankHolidaysMonitor implements EventUpdatedMonitor {

    private final BankHolidaysPreferences preferences;

    private boolean wasEnabled;

    BankHolidaysMonitor(BankHolidaysPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean dataWasUpdated() {
        return wasEnabled != preferences.isEnabled();
    }

    @Override
    public void refreshData() {
        wasEnabled = preferences.isEnabled();
    }

    @Override
    public void initialise() {
        wasEnabled = preferences.isEnabled();
    }
}
