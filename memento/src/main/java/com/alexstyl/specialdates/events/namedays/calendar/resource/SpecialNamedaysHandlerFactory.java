package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.Collections;
import java.util.List;

public final class SpecialNamedaysHandlerFactory {

    SpecialNamedays createStrategyForLocale(NamedayLocale locale, NamedayJSON namedayJSON) {
        if (isGreekLocale(locale)) {
            return GreekSpecialNamedays.from(namedayJSON);
        } else if (isRomanian(locale)) {
            return RomanianSpecialNamedays.from(namedayJSON);
        }
        return NO_SPECIAL_NAMEDAYS;
    }

    private boolean isRomanian(NamedayLocale locale) {
        return locale == NamedayLocale.ROMANIAN;
    }

    private boolean isGreekLocale(NamedayLocale locale) {
        return locale == NamedayLocale.GREEK;
    }

    private static final SpecialNamedays NO_SPECIAL_NAMEDAYS = new SpecialNamedays() {
        @Override
        public NamesInADate getNamedayOn(Date date) {
            return new NamesInADate(date, Collections.<String>emptyList());
        }

        @Override
        public NameCelebrations getNamedaysFor(String name, int year) {
            return new NameCelebrations(name);
        }

        @Override
        public List<String> getAllNames() {
            return Collections.emptyList();
        }
    };

}
