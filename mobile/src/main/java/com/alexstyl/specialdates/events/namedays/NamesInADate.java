package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The names celebrating in a specific Date
 */
public class NamesInADate {

    private DayDate date;
    private List<String> names;

    public NamesInADate(DayDate date) {
        this(date, new ArrayList<String>());
    }

    public NamesInADate(DayDate date, List<String> names) {
        this.date = date;
        this.names = names;
    }

    public DayDate getDate() {
        return date;
    }

    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    public void addName(String name) {
        names.add(name);
    }

    public int nameCount() {
        return names.size();
    }

}
