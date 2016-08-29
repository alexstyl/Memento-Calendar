package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dates {

    private final List<DayDate> dates = new ArrayList<>();

    public Dates() {
    }

    public Dates(DayDate date) {
        this.dates.add(date);
    }

    public Dates(List<DayDate> dates) {
        this.dates.addAll(dates);
    }

    public Dates(Dates dates) {
        this.dates.addAll(dates.dates);
    }

    @Override
    public String toString() {
        return dates.toString();
    }

    public DayDate getDate(int i) {
        return dates.get(i);
    }

    public int size() {
        return dates.size();
    }

    public boolean containsNoDate() {
        return dates.size() == 0;
    }

    public void add(DayDate date) {
        if (dates.contains(date)) {
            return;
        }
        this.dates.add(date);
    }

    public void sort() {
        Collections.sort(dates);
    }

    public static Dates merge(Dates... lists) {
        ArrayList<DayDate> list = new ArrayList<>();
        for (Dates dates : lists) {
            for (int i = 0; i < dates.size(); i++) {
                list.add(dates.getDate(i));
            }
        }
        return new Dates(list);
    }

    public void addAll(Dates dates) {
        this.dates.addAll(dates.dates);
    }
}
