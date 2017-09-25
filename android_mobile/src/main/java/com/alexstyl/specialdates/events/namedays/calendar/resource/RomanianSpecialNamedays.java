package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.EasternNameday;
import com.alexstyl.specialdates.events.namedays.calendar.EasternNamedaysExtractor;

import java.util.ArrayList;
import java.util.List;

final class RomanianSpecialNamedays implements SpecialNamedays {

    private final RomanianNamedays namedays;

    public static SpecialNamedays from(NamedayJSON namedayJSON) {
        EasternNamedaysExtractor extractor = new EasternNamedaysExtractor(namedayJSON.getSpecial());
        List<EasternNameday> easternNamedays = extractor.parse();
        ArrayList<String> names = namesOf(easternNamedays);
        RomanianNamedays namedays = RomanianNamedays.from(names);
        return new RomanianSpecialNamedays(namedays);
    }

    private static ArrayList<String> namesOf(List<EasternNameday> easternNamedays) {
        ArrayList<String> names = new ArrayList<>();
        for (EasternNameday easternNameday : easternNamedays) {
            names.addAll(easternNameday.getNamesCelebrating());
        }
        return names;
    }

    private RomanianSpecialNamedays(RomanianNamedays namedays) {
        this.namedays = namedays;
    }

    @Override
    public List<String> getAllNames() {
        return namedays.getAllNames();
    }

    @Override
    public NamesInADate getNamedayOn(Date date) {
        return namedays.getNamedaysFor(date);
    }

    @Override
    public NameCelebrations getNamedaysFor(String name, int year) {
        return namedays.getNamedaysFor(name, year);
    }
}
