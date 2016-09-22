package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;

public final class GreekSpecialNamedays implements SpecialNamedays {

    private final GreekNamedays greekNamedays;

    public static GreekSpecialNamedays from(NamedayJSON namedayJSON) {
        GreekNamedays greekNamedays = GreekNamedays.from(namedayJSON.getSpecial());
        return new GreekSpecialNamedays(greekNamedays);
    }

    public GreekSpecialNamedays(GreekNamedays greekNamedays) {
        this.greekNamedays = greekNamedays;
    }

    @Override
    public NamesInADate getNamedayOn(DayDate date) {
        return greekNamedays.getNamedayByDate(date);
    }

    @Override
    public ArrayList<String> getAllNames() {
        return greekNamedays.getNames();
    }

    @Override
    public NameCelebrations getNamedaysFor(String name, int year) {
        return greekNamedays.getNamedaysFor(name, year);
    }
}
