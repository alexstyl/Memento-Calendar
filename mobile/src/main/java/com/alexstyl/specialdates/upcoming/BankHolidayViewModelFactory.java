package com.alexstyl.specialdates.upcoming;

import android.view.View;

import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

final class BankHolidayViewModelFactory {

    BankHolidayViewModel createViewModelFor(BankHoliday bankHoliday) {
        BankHolidayViewModel bankHolidayViewModel;
        if (bankHoliday == null) {
            bankHolidayViewModel = new BankHolidayViewModel("", View.GONE);
        } else {
            bankHolidayViewModel = new BankHolidayViewModel(bankHoliday.getHolidayName(), View.VISIBLE);
        }
        return bankHolidayViewModel;
    }

}
