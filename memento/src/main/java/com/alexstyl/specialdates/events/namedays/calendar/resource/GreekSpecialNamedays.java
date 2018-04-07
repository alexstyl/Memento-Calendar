package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import java.util.ArrayList;

final class GreekSpecialNamedays implements SpecialNamedays {

    private final GreekNamedays greekNamedays;

    public static GreekSpecialNamedays from(NamedayJSON namedayJSON, OrthodoxEasterCalculator easterCalculator) {
        GreekNamedays greekNamedays = GreekNamedays.from(namedayJSON.getSpecial(), easterCalculator);
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
    public ArrayList<String> getAllNames() {
        return greekNamedays.getNames();
    }

    @Override
    public NameCelebrations getNamedaysFor(String name, int year) {
        return greekNamedays.getNamedaysFor(name, year);
    }
}
