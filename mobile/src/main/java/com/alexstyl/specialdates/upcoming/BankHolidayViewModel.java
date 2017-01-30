package com.alexstyl.specialdates.upcoming;

import com.alexstyl.android.ViewVisibility;

final public class BankHolidayViewModel {
    private final String bankHolidayName;
    @ViewVisibility
    private final int bankHolidaysVisibility;

    BankHolidayViewModel(String bankHolidayName, int bankHolidaysVisibility) {
        this.bankHolidayName = bankHolidayName;
        this.bankHolidaysVisibility = bankHolidaysVisibility;
    }

    @ViewVisibility
    public int getBankHolidaysVisibility() {
        return bankHolidaysVisibility;
    }

    public String getBankHolidayName() {
        return bankHolidayName;
    }
}
