package com.alexstyl.specialdates.date;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

/**
 * A date that contains celebrations. A CelebrationDate can contain namedays, and contacts celebrating
 */
public class CelebrationDate implements Comparable<CelebrationDate> {

    private final Date date;
    private final ContactEvents events;
    private final Optional<NamesInADate> namedays;
    private final Optional<BankHoliday> bankHoliday;

    public CelebrationDate(Date date, ContactEvents contactEvent, Optional<NamesInADate> namedays, Optional<BankHoliday> bankHoliday) {
        this.date = date;
        this.events = contactEvent;
        this.namedays = namedays;
        this.bankHoliday = bankHoliday;
    }

    public int getDayofMonth() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public NamesInADate getDaysNamedays() {
        return namedays.get();
    }

    public int getYear() {
        return date.getYear();
    }

    public int getContactCount() {
        return events.getContactCount();
    }

    public Date getDate() {
        return date;
    }

    public BankHoliday getBankHoliday() {
        return bankHoliday.get();
    }

    public boolean hasBankholiday() {
        return bankHoliday.isPresent();
    }

    public boolean hasNamedays() {
        return namedays.isPresent();
    }

    public boolean hasContactEvents() {
        return events.size() > 0;
    }

    public ContactEvents getContactEvents() {
        return events;
    }

    @Override
    public int compareTo(@NonNull CelebrationDate another) {
        if (another == this) {
            return 0;
        }
        return DateComparator.INSTANCE.compare(date, another.date);
    }
}
