package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;

public class TimePeriod {

    private final Date from;
    private final Date to;

    public static TimePeriod between(Date startDate, Date endDate) {
        if (DateComparator.INSTANCE.compare(startDate, endDate) > 0) {
            throw new IllegalArgumentException("starting Date was after end Date");
        }
        return new TimePeriod(startDate, endDate);
    }

    public static TimePeriod aYearFromNow() {
        Date today = Date.today();
        Date endDate = today.addDay(364);
        return TimePeriod.between(today, endDate);
    }

    private TimePeriod(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date getStartingDate() {
        return from;
    }

    public Date getEndingDate() {
        return to;
    }

    public boolean containsDate(Date date) {
        return (DateComparator.INSTANCE.compare(from, date) <= 0
                &&
                DateComparator.INSTANCE.compare(date, to) <= 0
        );
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimePeriod that = (TimePeriod) o;

        if (!from.equals(that.from)) {
            return false;
        }
        return to.equals(that.to);

    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
