package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

public final class SpecialNamedaysHandlerFactory {
    private final OrthodoxEasterCalculator orthodoxEasterCalculator;
    private final RomanianEasterSpecialCalculator romanianEasterCalculator;

    public SpecialNamedaysHandlerFactory(OrthodoxEasterCalculator orthodoxEasterCalculator,
                                         RomanianEasterSpecialCalculator romanianEasterCalculator) {
        this.orthodoxEasterCalculator = orthodoxEasterCalculator;
        this.romanianEasterCalculator = romanianEasterCalculator;
    }

    SpecialNamedays createStrategyForLocale(NamedayLocale locale, NamedayJSON namedayJSON) {
        if (isGreekLocale(locale)) {
            return GreekSpecialNamedays.Companion.from(namedayJSON, orthodoxEasterCalculator);
        } else if (isRomanian(locale)) {
            return RomanianSpecialNamedays.Companion.from(namedayJSON, romanianEasterCalculator);
        }
        return new NoSpecialNamedays();
    }


    private boolean isRomanian(NamedayLocale locale) {
        return locale == NamedayLocale.ROMANIAN;
    }

    private boolean isGreekLocale(NamedayLocale locale) {
        return locale == NamedayLocale.GREEK;
    }

}
