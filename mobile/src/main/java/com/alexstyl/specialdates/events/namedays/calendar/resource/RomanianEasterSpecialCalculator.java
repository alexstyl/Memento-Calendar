package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

class RomanianEasterSpecialCalculator {

    private final EasterCalculator easterCalculator;

    RomanianEasterSpecialCalculator() {
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
