package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.namedays.NameCelebrations;

public class NamedayCard {

    private NameCelebrations mNameday;

    public boolean isAvailable() {
        return mNameday != null && mNameday.size() > 0;
    }

    public DayDate getDate(int i) {
        return mNameday.getDate(i);
    }

    public void clear() {
        this.mNameday = null;
    }

    public void setNameday(NameCelebrations nameday) {
        this.mNameday = nameday;
    }

    public String getName() {
        return mNameday.getName();
    }

    public int size() {
        return mNameday.size();
    }
}
