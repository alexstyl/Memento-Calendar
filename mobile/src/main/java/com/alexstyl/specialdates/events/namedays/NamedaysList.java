package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DayDate;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NamedaysList {

    private final List<NamesInADate> namedays = new ArrayList<>();
    private final Set<String> names = new TreeSet<>(Collator.getInstance());

    public NamesInADate getNamedaysFor(Date date) {
        int index = indexOf(date);
        if (index != -1) {
            return namedays.get(index);
        }
        return new NamesInADate(date);
    }

    public void addNameday(Date date, String name) {
        NamesInADate names = getOrCreateNameDateFor(date);

        names.addName(name);
        this.names.add(name);
    }

    private NamesInADate getOrCreateNameDateFor(Date date) {
        int index = indexOf(date);
        if (index == -1) {
            NamesInADate lol = new NamesInADate(date);
            namedays.add(lol);
            return lol;
        } else {
            return namedays.get(index);
        }
    }

    private int indexOf(Date date) {
        for (int i = 0; i < namedays.size(); i++) {
            NamesInADate inlistDate = namedays.get(i);
            Date comparingDate = inlistDate.getDate();
            if (isRecurringEvent(comparingDate) && areMatching(comparingDate, date)) {
                return i;
            } else if (comparingDate.equals(date)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isRecurringEvent(Date comparingDate) {
        return !(comparingDate instanceof DayDate);
    }

    private boolean areMatching(Date date, Date otherDate) {
        return date.getDayOfMonth() == otherDate.getDayOfMonth()
                && date.getMonth() == otherDate.getMonth();
    }

    public ArrayList<String> getNames() {
        return new ArrayList<>(names);
    }
}
