package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.namedays.NameCelebrations;
import com.novoda.notils.exception.DeveloperError;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public final class GreekNamedays {

    private final EasterCalculator easterCalculator = new EasterCalculator();

    private final SpecialGreekNamedaysCalculator specialGreekNamedaysCalculator;

    private DayDate easter;
    private NamedayBundle namedays;

    public GreekNamedays(SpecialGreekNamedaysCalculator specialGreekNamedaysCalculator) {
        this.specialGreekNamedaysCalculator = specialGreekNamedaysCalculator;
    }

    public static GreekNamedays from(JSONArray specialJSON) {
        EasternNamedaysExtractor extractor = new EasternNamedaysExtractor(specialJSON);
        List<EasternNameday> namedays = extractor.parse(); // TODO performing haevy operation on constructor
        SpecialGreekNamedaysCalculator specialGreekNamedaysCalculator = new SpecialGreekNamedaysCalculator(namedays);
        return new GreekNamedays(specialGreekNamedaysCalculator);
    }

    public NamesInADate getNamedayByDate(DayDate date) {
        throwExceptionIfYearIsMissing(date);

        int year = date.getYear();
        refreshNamedaysIfNeeded(year);

        return namedays.getNamedaysFor(date);
    }

    private void throwExceptionIfYearIsMissing(DayDate date) {
        if (noYearSpecifiedIn(date)) {
            throw new DeveloperError("You must specify a year");
        }
    }

    private boolean noYearSpecifiedIn(DayDate date) {
        return date.getYear() == DayDate.NO_YEAR;
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

    public ArrayList<String> getNames() {
        int year = DayDate.today().getYear();
        refreshNamedaysIfNeeded(year);
        return namedays.getNames();
    }

    public NameCelebrations getNamedaysFor(String name, int year) {
        refreshNamedaysIfNeeded(year);
        return namedays.getDatesFor(name);
    }
}
