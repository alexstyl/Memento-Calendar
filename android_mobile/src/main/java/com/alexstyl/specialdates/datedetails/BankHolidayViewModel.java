package com.alexstyl.specialdates.datedetails;

import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

class BankHolidayViewModel implements DateDetailsViewModel {
    private final BankHoliday bankHoliday;

    BankHolidayViewModel(BankHoliday bankHoliday) {
        this.bankHoliday = bankHoliday;
    }

    @Override
    public int getViewType() {
        return DateDetailsViewType.BANKHOLIDAY;
    }

    public BankHoliday getBankHoliday() {
        return bankHoliday;
    }
}
