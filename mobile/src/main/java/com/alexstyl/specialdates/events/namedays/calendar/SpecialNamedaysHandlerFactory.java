package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.resource.NamedayJSON;

import org.json.JSONArray;

public class SpecialNamedaysHandlerFactory {

    public SpecialNamedaysStrategy createStrategyForLocale(NamedayLocale locale, NamedayJSON namedayJSON) {
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
