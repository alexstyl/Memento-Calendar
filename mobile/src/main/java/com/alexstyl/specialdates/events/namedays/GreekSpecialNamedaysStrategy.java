package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

import java.util.ArrayList;

import org.json.JSONArray;

public final class GreekSpecialNamedaysStrategy implements SpecialNamedaysStrategy {

    private final GreekNamedays greekNamedays;

    public static GreekSpecialNamedaysStrategy newInstance(JSONArray specialJSON) {
        GreekNamedays greekNamedays = GreekNamedays.from(specialJSON);
        return new GreekSpecialNamedaysStrategy(greekNamedays);
    }

    public GreekSpecialNamedaysStrategy(GreekNamedays greekNamedays) {
        this.greekNamedays = greekNamedays;
    }

    @Override
    public NamesInADate getNamedayByDate(DayDate date) {
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
