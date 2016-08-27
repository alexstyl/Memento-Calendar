package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.events.DayDate;

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

    public LoadingTimeDuration increaseTopBoundary() {
        return new LoadingTimeDuration(from.minusMonth(1), to);
    }

    public LoadingTimeDuration increaseBottomBoundary() {
        return new LoadingTimeDuration(from, to.addMonth(1));
    }

    public LoadingTimeDuration increaseBoundariesBy(int i) {
        return new LoadingTimeDuration(from.minusMonth(1), to.addMonth(1));
    }
}
