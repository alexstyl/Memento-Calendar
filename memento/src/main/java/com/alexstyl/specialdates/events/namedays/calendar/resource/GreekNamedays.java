package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.StaticNamedays;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.EasternNameday;
import com.alexstyl.specialdates.events.namedays.calendar.EasternNamedaysExtractor;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import org.json.JSONArray;

import java.util.List;

public final class GreekNamedays {

    private final OrthodoxEasterCalculator easterCalculator;

    private final SpecialGreekNamedaysCalculator specialGreekNamedaysCalculator;

    private Date easter;
    private StaticNamedays namedays;

    private GreekNamedays(OrthodoxEasterCalculator easterCalculator, SpecialGreekNamedaysCalculator specialGreekNamedaysCalculator) {
        this.easterCalculator = easterCalculator;
        this.specialGreekNamedaysCalculator = specialGreekNamedaysCalculator;
    }

    public static GreekNamedays from(JSONArray specialJSON, OrthodoxEasterCalculator easterCalculator) {
        EasternNamedaysExtractor extractor = new EasternNamedaysExtractor(specialJSON);
        List<EasternNameday> namedays = extractor.parse();
        SpecialGreekNamedaysCalculator specialGreekNamedaysCalculator = new SpecialGreekNamedaysCalculator(namedays);
        return new GreekNamedays(easterCalculator, specialGreekNamedaysCalculator);
    }

    NamesInADate getNamedayByDate(Date date) {
        int year = date.getYear();
        refreshNamedaysIfNeeded(year);
        return namedays.getNamedaysFor(date);
    }

    private void refreshNamedaysIfNeeded(int year) {
        if (needsToCalculateNamedaysFor(year)) {
            calculateNamedaysForYear(year);
        }
    }

    private boolean needsToCalculateNamedaysFor(int year) {
        return (easter == null || namedays == null) || easter.getYear() != year;
    }

    private void calculateNamedaysForYear(int year) {
        easter = easterCalculator.calculateEasterForYear(year);
        namedays = specialGreekNamedaysCalculator.calculateForEasterDate(easter);
    }

    public List<String> getNames() {
        int year = Date.Companion.today().getYear();
        refreshNamedaysIfNeeded(year);
        return namedays.getNames();
    }

    public NameCelebrations getNamedaysFor(String name, int year) {
        refreshNamedaysIfNeeded(year);
        return namedays.getDatesFor(name);
    }
}
