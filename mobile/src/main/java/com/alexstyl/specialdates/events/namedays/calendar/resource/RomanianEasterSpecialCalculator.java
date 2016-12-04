package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import static com.alexstyl.specialdates.date.DateConstants.SUNDAY;

class RomanianEasterSpecialCalculator {

    private final OrthodoxEasterCalculator easterCalculator;

    RomanianEasterSpecialCalculator(OrthodoxEasterCalculator easterCalculator) {
        this.easterCalculator = easterCalculator;
    }

    Date calculateSpecialRomanianDayForYear(int year) {
        Date easter = easterCalculator.calculateEasterForYear(year);
        while (easter.getDayOfWeek() != SUNDAY) {
            easter = easter.addDay(-1);
        }
        return easter.addWeek(-1);
    }

}
