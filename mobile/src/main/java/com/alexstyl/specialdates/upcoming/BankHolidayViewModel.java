package com.alexstyl.specialdates.upcoming;

import android.view.View;

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

    boolean isHidden() {
        return bankHolidaysVisibility == View.GONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankHolidayViewModel that = (BankHolidayViewModel) o;

        if (bankHolidaysVisibility != that.bankHolidaysVisibility) {
            return false;
        }
        return bankHolidayName.equals(that.bankHolidayName);

    }

    @Override
    public int hashCode() {
        int result = bankHolidayName.hashCode();
        result = 31 * result + bankHolidaysVisibility;
        return result;
    }
}
