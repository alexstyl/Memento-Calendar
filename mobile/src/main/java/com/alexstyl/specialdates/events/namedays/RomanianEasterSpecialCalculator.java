package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;

public class RomanianEasterSpecialCalculator {

    private final EasterCalculator easterCalculator;

    public RomanianEasterSpecialCalculator() {
        easterCalculator = new EasterCalculator();
    }

    DayDate calculateSpecialRomanianDayForYear(int year) {
        DayDate easter = easterCalculator.calculateEasterForYear(year);
        while (easter.getDayOfWeek() != DayDate.SUNDAY) {
            easter = easter.addDay(-1);
        }
        return easter.addWeek(-1);
    }

}
