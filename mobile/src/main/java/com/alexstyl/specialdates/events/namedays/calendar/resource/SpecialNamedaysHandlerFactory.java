package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

class SpecialNamedaysHandlerFactory {

    SpecialNamedays createStrategyForLocale(NamedayLocale locale, NamedayJSON namedayJSON) {
        if (isGreekLocale(locale)) {
            return GreekSpecialNamedays.from(namedayJSON);
        } else if (isRomanian(locale)) {
            return RomanianSpecialNamedays.from(namedayJSON);
        }
        return NoSpecialNamedays.INSTANCE;
    }

    private boolean isRomanian(NamedayLocale locale) {
        return locale == NamedayLocale.ro;
    }

    private boolean isGreekLocale(NamedayLocale locale) {
        return locale == NamedayLocale.gr;
    }

}
