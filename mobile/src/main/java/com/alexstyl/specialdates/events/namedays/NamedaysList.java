package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class NamedaysList {

    private final ArrayList<NamesInADate> namedays = new ArrayList<>();
    private final Set<String> names = new TreeSet<>(Collator.getInstance());

    public NamesInADate getNamedaysFor(DayDate date) {
        int index = indexOf(date);
        if (index != -1) {
            return namedays.get(index);
        }
        return new NamesInADate(date);
    }

    public void clear() {
        namedays.clear();
    }

    public void addAll(NamedaysList namedaysList) {
        this.namedays.addAll(namedaysList.namedays);
        this.names.addAll(namedaysList.names);
    }

    public void addNameday(DayDate date, String name) {
        NamesInADate names = getOrCreateNameDateFor(date);

        names.addName(name);
        this.names.add(name);
    }

    private NamesInADate getOrCreateNameDateFor(DayDate date) {
        int index = indexOf(date);
        if (index == -1) {
            NamesInADate lol = new NamesInADate(date);
            namedays.add(lol);
            return lol;
        } else {
            return namedays.get(index);
        }
    }

    private int indexOf(DayDate date) {
        for (int i = 0; i < namedays.size(); i++) {
            NamesInADate inlistDate = namedays.get(i);
            DayDate comparingDate = inlistDate.getDate();

            if (yearCheckIsNeeded(inlistDate)) {
                if (hasYearSet(comparingDate) && referToDifferentYear(date, comparingDate)) {
                    continue;
                }
            }
            if (areMatching(date, comparingDate)) {
                return i;
            }
        }
        return -1;
    }

    private boolean yearCheckIsNeeded(NamesInADate namesInADate) {
        return namesInADate.getDate().getYear() != -1;
    }

    private boolean referToDifferentYear(DayDate date, DayDate otherDate) {
        return date.getYear() != otherDate.getYear();
    }

    private boolean hasYearSet(DayDate date) {
        return date.getYear() != -1;
    }

    private boolean areMatching(DayDate date, DayDate otherDate) {
        return date.getDayOfMonth() == otherDate.getDayOfMonth()
                && date.getMonth() == otherDate.getMonth();
    }

    public ArrayList<String> getNames() {
        return new ArrayList<>(names);
    }
}
