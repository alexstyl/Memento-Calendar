package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.Collections;
import java.util.List;

public enum NoSpecialNamedays implements SpecialNamedays {
    INSTANCE;

    @Override
    public NamesInADate getNamedayByDate(DayDate date) {
        return new NamesInADate(date);
    }

    @Override
    public List<String> getAllNames() {
        return Collections.emptyList();
    }

    @Override
    public NameCelebrations getNamedaysFor(String name, int year) {
        return new NameCelebrations(name);
    }
}
