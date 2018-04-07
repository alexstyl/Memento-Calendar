package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;

public class TestNamedayCalendarBuilder {
    private NamedayLocale locale = NamedayLocale.GREEK;
    private int year = Date.Companion.getCURRENT_YEAR();
    private SpecialNamedaysHandlerFactory factory = new SpecialNamedaysHandlerFactory(easterCalculator, romanianEasterCalculator);

    public TestNamedayCalendarBuilder forLocale(NamedayLocale locale) {
        this.locale = locale;
        return this;
    }

    public TestNamedayCalendarBuilder forYear(int year) {
        this.year = year;
        return this;
    }

    public NamedayCalendar build() {
        NamedayJSONProvider jsonProvider = new NamedayJSONProvider(new TestJSONResourceLoader());
        NamedayCalendarProvider namedayCalendarProvider = new NamedayCalendarProvider(jsonProvider, factory);
        return namedayCalendarProvider.loadNamedayCalendarForLocale(locale, year);
    }
}
