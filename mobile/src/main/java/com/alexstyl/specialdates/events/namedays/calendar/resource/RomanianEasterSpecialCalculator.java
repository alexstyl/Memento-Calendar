package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

import static com.alexstyl.specialdates.date.DateConstants.SUNDAY;

class RomanianEasterSpecialCalculator {

    private final EasterCalculator easterCalculator;

    RomanianEasterSpecialCalculator() {
        easterCalculator = new EasterCalculator();
    }

    Date calculateSpecialRomanianDayForYear(int year) {
        Date easter = easterCalculator.calculateEasterForYear(year);
        while (easter.getDayOfWeek() != SUNDAY) {
            easter = easter.addDay(-1);
        }
        return easter.addWeek(-1);
    }

}
