package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DayDate;

public class DateTransformer {

    private final int year;

    public DateTransformer(int year) {
        this.year = year;
    }

    public DayDate asDayDate(Date date) {
        if (date instanceof DayDate) {
            return (DayDate) date;
        }
        return DayDate.newInstance(date.getDayOfMonth(), date.getMonth(), year);
    }
}
