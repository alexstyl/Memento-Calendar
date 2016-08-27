package com.alexstyl.specialdates.date;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.ContactEvents;
import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.List;

/**
 * A date that contains celebrations. A CelebrationDate can contain namedays, and contacts celebrating
 * <p>Created by alexstyl on 20/07/15.</p>
 */
public class CelebrationDate implements Comparable<CelebrationDate> {

    private final DayDate date;
    private final ContactEvents events;
    private final Optional<NamesInADate> namedays;
    private final Optional<BankHoliday> bankHoliday;

    public CelebrationDate(DayDate date, ContactEvents contactEvent, Optional<NamesInADate> namedays, Optional<BankHoliday> bankHoliday) {
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

    public List<Contact> getContacts() {
        return events.getContacts();
    }

    public int getContactCount() {
        return events.getContactCount();
    }

    public DayDate getDate() {
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

        return date.compareTo(another.date);
    }
}
