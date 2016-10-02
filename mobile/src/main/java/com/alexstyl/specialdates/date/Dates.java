package com.alexstyl.specialdates.date;

import java.util.ArrayList;
import java.util.List;

public class Dates {

    private final List<Date> dates = new ArrayList<>();

    public Dates() {
    }

    public Dates(Date date) {
        this.dates.add(date);
    }

    public Dates(List<Date> dates) {
        this.dates.addAll(dates);
    }

    public Dates(Dates dates) {
        this.dates.addAll(dates.dates);
    }

    @Override
    public String toString() {
        return dates.toString();
    }

    public Date getDate(int i) {
        return dates.get(i);
    }

    public int size() {
        return dates.size();
    }

    public boolean containsNoDate() {
        return dates.size() == 0;
    }

    public void add(Date date) {
        if (dates.contains(date)) {
            return;
        }
        this.dates.add(date);
    }

    public void addAll(Dates dates) {
        this.dates.addAll(dates.dates);
    }
}
