package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.json.JSONArray;

class SpecialNamedaysHandlerFactory {

    SpecialNamedaysStrategy createStrategyForLocale(NamedayLocale locale, NamedayJSON namedayJSON) {
        JSONArray specialJSON = namedayJSON.getSpecial();
        if (isGreekLocale(locale)) {
            return GreekSpecialNamedaysStrategy.from(specialJSON);
        } else if (isRomanian(locale)) {
            return RomanianSpecialNamedaysStrategy.from(specialJSON);
        }
        return NoSpecialNamedaysStrategy.INSTANCE;
    }

    private boolean isRomanian(NamedayLocale locale) {
        return locale == NamedayLocale.ro;
    }

    private boolean isGreekLocale(NamedayLocale locale) {
        return locale == NamedayLocale.gr;
    }

}
