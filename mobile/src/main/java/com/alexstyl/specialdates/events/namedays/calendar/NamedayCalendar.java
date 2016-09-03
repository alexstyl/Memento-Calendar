package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.Dates;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.SpecialNamedaysStrategy;

import java.util.ArrayList;
import java.util.List;

public final class NamedayCalendar {

    private final NamedayLocale locale;
    private final NamedayBundle namedayBundle;
    private final SpecialNamedaysStrategy strategy;
    private final int year;

    public NamedayCalendar(NamedayLocale locale, NamedayBundle namedays, SpecialNamedaysStrategy strategy, int year) {
        this.locale = locale;
        this.namedayBundle = namedays;
        this.strategy = strategy;
        this.year = year;
    }

    public NameCelebrations getNormalNamedaysFor(String name) {
        return namedayBundle.getDatesFor(name);
    }

    public NameCelebrations getSpecialNamedaysFor(String name) {
        return strategy.getNamedaysFor(name, year);
    }

    public NamesInADate getAllNamedayOn(DayDate date) {
        List<String> names = namedayBundle.getNamedaysFor(date).getNames();
        List<String> specialNames = strategy.getNamedayByDate(date).getNames();
        List<String> arrayList = new ArrayList<>(names.size() + specialNames.size());
        arrayList.addAll(names);
        arrayList.addAll(specialNames);
        return new NamesInADate(date, arrayList);
    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> names = namedayBundle.getNames();
        names.addAll(strategy.getAllNames());
        return names;
    }

    public NamedayLocale getLocale() {
        return locale;
    }

    public int getYear() {
        return year;
    }

    public NameCelebrations getAllNamedays(String searchQuery) {
        NameCelebrations names = namedayBundle.getDatesFor(searchQuery);
        NameCelebrations specialNames = strategy.getNamedaysFor(searchQuery, year);

        Dates dates = new Dates(names.getDates());
        dates.addAll(specialNames.getDates());
        String matchingName = getName(names, specialNames);
        return new NameCelebrations(matchingName, dates);
    }

    private String getName(NameCelebrations names, NameCelebrations specialNames) {
        if (names.getName().length() > 0) {
            return names.getName();
        }
        return specialNames.getName();
    }
}
