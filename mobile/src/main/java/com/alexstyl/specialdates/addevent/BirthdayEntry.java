package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.date.Date;

public class BirthdayEntry {
    private final long id;
    private final Date birthday;

    public BirthdayEntry(long id, Date birthday) {
        this.id = id;
        this.birthday = birthday;
    }

    public long getEntryID() {
        return id;
    }
}
