package com.alexstyl.specialdates.events.namedays.calendar;

import android.content.Context;

import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;

import org.json.JSONArray;

public class NamedayCalendarProvider {

    private static NamedayCalendar cachedCalendar;
    private final NamedayRawJSONProvider rawJSONProvider;
    private final NamedayJSONParser parser;
    private final SpecialNamedaysHandlerFactory factory;

    public static NamedayCalendarProvider newInstance(Context context) {
        NamedayPreferences preferences = NamedayPreferences.newInstance(context);
        NamedayLocale locale = preferences.getSelectedLanguage();

        NamedayRawJSONProvider rawloader = NamedayRawJSONProvider.newInstance(context, locale);
        NamedayJSONParser parser = new NamedayJSONParser(locale);
        SpecialNamedaysHandlerFactory factory = new SpecialNamedaysHandlerFactory();
        return new NamedayCalendarProvider(rawloader, parser, factory);
    }

    NamedayCalendarProvider(NamedayRawJSONProvider rawJSONProvider, NamedayJSONParser parser, SpecialNamedaysHandlerFactory factory) {
        this.rawJSONProvider = rawJSONProvider;
        this.parser = parser;
        this.factory = factory;
    }

    public NamedayCalendar loadNamedayCalendarForLocale(NamedayLocale locale, int year) {
        if (hasRequestedSameCalendar(locale, year)) {
            return cachedCalendar;
        }

        NamedayBundle namedaysBundle = getNamedayBundle();
        SpecialNamedaysStrategy specialCaseHandler = getSpecialnamedaysHandler(locale);
        NamedayCalendar namedayCalendar = new NamedayCalendar(locale, namedaysBundle, specialCaseHandler, year);

        cacheCalendar(namedayCalendar);

        return namedayCalendar;
    }

    private SpecialNamedaysStrategy getSpecialnamedaysHandler(NamedayLocale locale) {
        return factory.createStrategyForLocale(locale, rawJSONProvider);
    }

    private NamedayBundle getNamedayBundle() {
        JSONArray namedaysJSON = rawJSONProvider.getNamedays();
        return parser.parseAsNamedays(namedaysJSON);
    }

    private void cacheCalendar(NamedayCalendar namedayCalendar) {
        cachedCalendar = namedayCalendar;
    }

    private boolean hasRequestedSameCalendar(NamedayLocale locale, int year) {
        return cachedCalendar != null && cachedCalendar.getYear() == year && cachedCalendar.getLocale().equals(locale);
    }

}
