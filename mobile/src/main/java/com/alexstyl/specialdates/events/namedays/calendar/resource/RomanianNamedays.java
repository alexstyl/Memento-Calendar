package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedaysList;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;
import java.util.List;

public class RomanianNamedays {

    private final RomanianEasterSpecialCalculator calculator;
    private final List<String> names;

    private NamedayBundle namedays;
    private DayDate romanianDate;

    public static RomanianNamedays from(List<String> names) {
        RomanianEasterSpecialCalculator calculator = new RomanianEasterSpecialCalculator();
        return new RomanianNamedays(calculator, names);
    }

    public RomanianNamedays(RomanianEasterSpecialCalculator calculator, List<String> names) {
        this.calculator = calculator;
        this.names = names;
    }

    public ArrayList<String> getAllNames() {
        return new ArrayList<>(names);
    }

    public NamesInADate getNamedaysFor(DayDate date) {
        calculateEasterIfNecessary(date.getYear());
        if (romanianDate.equals(date)) {
            return namedays.getNamedaysFor(date);
        }
        return new NamesInADate(date);
    }

    public NameCelebrations getNamedaysFor(String name, int year) {
        calculateEasterIfNecessary(year);
        return namedays.getDatesFor(name);
    }

    private void calculateEasterIfNecessary(int year) {
        if (romanianDate == null || romanianDate.getYear() != year) {
            romanianDate = calculator.calculateSpecialRomanianDayForYear(year);

            NamedaysList dateToNames = new NamedaysList();
            CharacterNode namesToDate = new CharacterNode();
            for (String name : names) {
                dateToNames.addNameday(romanianDate, name);
                namesToDate.addDate(name, romanianDate);
            }

            namedays = new NamedayBundle(namesToDate, dateToNames);
        }
    }
}
