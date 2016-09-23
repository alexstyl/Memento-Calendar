package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

public class BankholidayCalendar {

    private static final Object LOCK = new Object();

    private static BankholidayCalendar INSTANCE;
    private final BankHolidayRepository repository;

    private BankholidayCalendar(BankHolidayRepository repository) {
        this.repository = repository;
    }

    public static BankholidayCalendar get() {
        if (INSTANCE == null) {
            INSTANCE = new BankholidayCalendar(new BankHolidayRepository(new EasterCalculator()));
            INSTANCE.initialise();
        }
        return INSTANCE;
    }

    private void initialise() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    int year = DayDate.today().getYear();
                    repository.preloadHolidaysForYear(year);
                }
            }
        });
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }

    public Optional<BankHoliday> getBankholidayFor(DayDate date) {
        synchronized (LOCK) {
            return repository.calculateBankholidayFor(date);
        }
    }
}
