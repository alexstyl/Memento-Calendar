package com.alexstyl.specialdates.events.namedays;

import org.json.JSONArray;

public class SpecialNamedaysHandlerFactory {

    public SpecialNamedaysStrategy createStrategyForLocale(NamedayLocale locale, NamedayRawJSONProvider rawloader) {
        if (isGreekLocale(locale)) {
            JSONArray jsonArray = rawloader.getSpecialNamedays();
            return GreekSpecialNamedaysStrategy.newInstance(jsonArray);
        } else if (isRomanian(locale)) {
            JSONArray jsonArray = rawloader.getSpecialNamedays();
            return RomanianSpecialNamedaysStrategy.from(jsonArray);
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
