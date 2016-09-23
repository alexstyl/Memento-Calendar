package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

public class NamedayCard {

    private NameCelebrations namedays;

    public void setNameday(NameCelebrations nameday) {
        this.namedays = nameday;
    }

    public boolean isAvailable() {
        return namedays != null && namedays.size() > 0;
    }

    public Date getDate(int i) {
        return namedays.getDate(i);
    }

    public void clear() {
        this.namedays = null;
    }

    public String getName() {
        return namedays.getName();
    }

    public int size() {
        return namedays.size();
    }
}
