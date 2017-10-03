package com.alexstyl.specialdates.events.namedays.calendar.resource;

import android.content.res.Resources;

import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;

import org.json.JSONException;

public class NamedayCalendarProvider {

    private static NamedayCalendar cachedCalendar;

    private final SpecialNamedaysHandlerFactory factory;
    private final NamedayJSONProvider jsonProvider;

    /**
     * @deprecated Use the constructor instead.
     */
    public static NamedayCalendarProvider newInstance(Resources resources) {
        NamedayJSONResourceLoader loader = new AndroidJSONResourceLoader(resources);
        NamedayJSONProvider jsonResourceProvider = new NamedayJSONProvider(loader);
        SpecialNamedaysHandlerFactory factory = new SpecialNamedaysHandlerFactory();
        return new NamedayCalendarProvider(jsonResourceProvider, factory);
    }

    public NamedayCalendarProvider(NamedayJSONProvider jsonProvider, SpecialNamedaysHandlerFactory factory) {
        this.factory = factory;
        this.jsonProvider = jsonProvider;
    }

    public NamedayCalendar loadNamedayCalendarForLocale(NamedayLocale locale, int year) {
        if (hasRequestedSameCalendar(locale, year)) {
            return cachedCalendar;
        }
        NamedayJSON namedayJSON = getNamedayJSONFor(locale);
        SpecialNamedays specialCaseHandler = getSpecialnamedaysHandler(locale, namedayJSON);
        NamedayBundle namedaysBundle = getNamedayBundle(locale, namedayJSON);
        NamedayCalendar namedayCalendar = new NamedayCalendar(locale, namedaysBundle, specialCaseHandler, year);

        cacheCalendar(namedayCalendar);

        return namedayCalendar;
    }

    private boolean hasRequestedSameCalendar(NamedayLocale locale, int year) {
        return cachedCalendar != null && cachedCalendar.getYear() == year && cachedCalendar.getLocale().equals(locale);
    }

    private NamedayJSON getNamedayJSONFor(NamedayLocale locale) {
        try {
            return jsonProvider.getNamedayJSONFor(locale);
        } catch (JSONException e) {
            throw new IllegalStateException("Could not load nameday JSON for " + locale);
        }
    }

    private SpecialNamedays getSpecialnamedaysHandler(NamedayLocale locale, NamedayJSON namedayJSON) {
        return factory.createStrategyForLocale(locale, namedayJSON);
    }

    private NamedayBundle getNamedayBundle(NamedayLocale locale, NamedayJSON namedayJSON) {
        if (locale.isComparedBySound()) {
            return NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON);
        } else {
            return NamedayJSONParser.getNamedaysFrom(namedayJSON);
        }
    }

    private void cacheCalendar(NamedayCalendar namedayCalendar) {
        cachedCalendar = namedayCalendar;
    }

}
