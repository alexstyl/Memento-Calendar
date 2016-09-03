package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.DayDate;

import java.io.Serializable;

public class LoadingTimeDuration implements Serializable {

    private DayDate from;
    private DayDate to;

    public LoadingTimeDuration(DayDate from, DayDate to) {
        this.from = from;
        this.to = to;
    }

    public DayDate getFrom() {
        return from;
    }

    public DayDate getTo() {
        return to;
    }

}
