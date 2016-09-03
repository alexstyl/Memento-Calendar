package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.Node;

import java.util.ArrayList;

public class NamedayBundle {

    private final Node namesToDate;
    private final NamedaysList namedaysList;

    public NamedayBundle(Node namesToDate, NamedaysList dateToNames) {
        this.namesToDate = namesToDate;
        this.namedaysList = dateToNames;
    }

    public NameCelebrations getDatesFor(String name) {
        NameCelebrations bundle = namesToDate.getDates(name);
        if (bundle.containsNoDate()) {
            return NameCelebrations.EMPTY;
        }
        return new NameCelebrations(bundle.getName(), bundle.getDates());
    }

    public NamesInADate getNamedaysFor(DayDate date) {
        return namedaysList.getNamedaysFor(date);
    }

    public ArrayList<String> getNames() {
        return namedaysList.getNames();
    }
}
