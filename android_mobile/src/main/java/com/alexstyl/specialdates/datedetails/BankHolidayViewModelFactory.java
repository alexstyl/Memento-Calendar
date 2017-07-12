package com.alexstyl.specialdates.datedetails;

import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

final class BankHolidayViewModelFactory {
    DateDetailsViewModel convertToViewModel(BankHoliday bankHoliday) {
        return new BankHolidayViewModel(bankHoliday);
    }
}
