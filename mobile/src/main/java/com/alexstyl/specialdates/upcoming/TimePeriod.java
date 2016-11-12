package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;

public class TimePeriod {

    private final Date from;
    private final Date to;

    public TimePeriod(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public int getYear() {
        return from.getYear();
    }

    public boolean containsDate(Date date) {
        return (DateComparator.INSTANCE.compare(from, date) < 0
                &&
                DateComparator.INSTANCE.compare(date, to) < 0
        );
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
