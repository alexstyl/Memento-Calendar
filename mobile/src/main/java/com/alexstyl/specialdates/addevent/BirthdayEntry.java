package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.contact.Birthday;

public class BirthdayEntry {
    private final long id;
    private final Birthday birthday;

    public BirthdayEntry(long id, Birthday birthday) {
        this.id = id;
        this.birthday = birthday;
    }

    public long getEntryID() {
        return id;
    }
}
