package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public final class RomanianSpecialNamedaysStrategy implements SpecialNamedaysStrategy {

    private final RomanianNamedays namedays;

    public static SpecialNamedaysStrategy from(JSONArray specialJSON) {
        EasternNamedaysExtractor extractor = new EasternNamedaysExtractor(specialJSON);
        List<EasternNameday> easternNamedays = extractor.parse();
        ArrayList<String> names = namesOf(easternNamedays);
        RomanianNamedays namedays = RomanianNamedays.from(names);
        return new RomanianSpecialNamedaysStrategy(namedays);
    }

    private static ArrayList<String> namesOf(List<EasternNameday> easternNamedays) {
        ArrayList<String> names = new ArrayList<>();
        for (EasternNameday easternNameday : easternNamedays) {
            names.addAll(easternNameday.getNamesCelebrating());
        }
        return names;
    }

    public RomanianSpecialNamedaysStrategy(RomanianNamedays namedays) {
        this.namedays = namedays;
    }

    @Override
    public ArrayList<String> getAllNames() {
        return namedays.getAllNames();
    }

    @Override
    public NamesInADate getNamedayByDate(DayDate date) {
        return namedays.getNamedaysFor(date);
    }

    @Override
    public NameCelebrations getNamedaysFor(String name, int year) {
        return namedays.getNamedaysFor(name, year);
    }
}
