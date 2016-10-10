package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.List;

final class GreekSpecialNamedays implements SpecialNamedays {

    private final GreekNamedays greekNamedays;

    public static GreekSpecialNamedays from(NamedayJSON namedayJSON) {
        GreekNamedays greekNamedays = GreekNamedays.from(namedayJSON.getSpecial());
        return new GreekSpecialNamedays(greekNamedays);
    }

    private GreekSpecialNamedays(GreekNamedays greekNamedays) {
        this.greekNamedays = greekNamedays;
    }

    @Override
    public NamesInADate getNamedayOn(Date date) {
        return greekNamedays.getNamedayByDate(date);
    }

    @Override
    public List<String> getAllNames() {
        return greekNamedays.getNames();
    }

    @Override
    public NameCelebrations getNamedaysFor(String name, int year) {
        return greekNamedays.getNamedaysFor(name, year);
    }
}
