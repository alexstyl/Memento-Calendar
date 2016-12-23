package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.date.Date;

class BirthdayEntry {
    private final long id;
    private final Date birthday;

    BirthdayEntry(long id, Date birthday) {
        this.id = id;
        this.birthday = birthday;
    }

    long getEntryID() {
        return id;
    }
}
