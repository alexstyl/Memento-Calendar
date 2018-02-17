package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The names celebrating on a specific Date
 */
public class NamesInADate {

    private Date date;
    private List<String> names;

    public NamesInADate(Date date) {
        this(date, new ArrayList<String>());
    }

    public NamesInADate(Date date, List<String> names) {
        this.date = date;
        this.names = names;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    /**
     * Do not use this method. Prefer passing names from the constructor of the class
     */
    @Deprecated
    public void addName(String name) {
        names.add(name);
    }

    public int nameCount() {
        return names.size();
    }

}
