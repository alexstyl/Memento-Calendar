package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.AnnualEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.Dates;
import com.alexstyl.specialdates.date.DayDate;

/**
 * A name and the list of Namedays it's celebrated
 */
public class NameCelebrations {

    public static final NameCelebrations EMPTY = new NameCelebrations("");

    private final String name;
    private final Dates dates;

    public NameCelebrations(String name) {
        this.name = name;
        this.dates = new Dates();
    }

    public NameCelebrations(String name, DayDate easter) {
        this(name, new Dates(easter));
    }

    public NameCelebrations(String name, Dates dates) {
        this.name = name;
        this.dates = dates;
    }

    public String getName() {
        return name;
    }

    public Dates getDates() {
        return dates;
    }

    public boolean containsNoDate() {
        return dates.containsNoDate();
    }

    public AnnualEvent getDate(int i) {
//        return dates.getDate(i);
        throw new UnsupportedOperationException("");
    }

    public int size() {
        return dates.size();
    }

    public void sortDates() {
        dates.sort();
    }

    public void addDate(Date date) {
        this.dates.add(date);
    }

    public boolean isEmpty() {
        return dates.size() == 0;
    }
}
