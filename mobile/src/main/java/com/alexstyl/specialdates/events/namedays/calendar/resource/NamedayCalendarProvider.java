package com.alexstyl.specialdates.events.namedays.calendar.resource;

import android.content.res.Resources;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;

import org.json.JSONArray;
import org.json.JSONException;

public class NamedayCalendarProvider {

    private static NamedayCalendar cachedCalendar;

    private final SpecialNamedaysHandlerFactory factory;
    private final NamedayJSONResourceProvider jsonProvider;

    public static NamedayCalendarProvider newInstance(Resources resources) {
        NamedayJSONResourceLoader loader = new AndroidJSONResourceLoader(resources);
        NamedayJSONResourceProvider jsonResourceProvider = new NamedayJSONResourceProvider(loader);
        SpecialNamedaysHandlerFactory factory = new SpecialNamedaysHandlerFactory();
        return new NamedayCalendarProvider(jsonResourceProvider, factory);
    }

    // eventually we will use DI to provide this
    public NamedayCalendarProvider(NamedayJSONResourceProvider jsonProvider, SpecialNamedaysHandlerFactory factory) {
        this.factory = factory;
        this.jsonProvider = jsonProvider;
    }

    public NamedayCalendar loadNamedayCalendarForLocale(NamedayLocale locale, int year) {
        if (hasRequestedSameCalendar(locale, year)) {
            return cachedCalendar;
        }
        NamedayJSON namedayJSON = getNamedayJSONFor(locale);
        SpecialNamedaysStrategy specialCaseHandler = getSpecialnamedaysHandler(locale, namedayJSON);
        NamedayBundle namedaysBundle = getNamedayBundle(locale, namedayJSON);
        NamedayCalendar namedayCalendar = new NamedayCalendar(locale, namedaysBundle, specialCaseHandler, year);

        cacheCalendar(namedayCalendar);

        return namedayCalendar;
    }

    private NamedayJSON getNamedayJSONFor(NamedayLocale locale) {
        try {
            return jsonProvider.getNamedayJSONFor(locale);
        } catch (JSONException e) {
            ErrorTracker.track(e);
            return new NamedayJSON(new JSONArray(), new JSONArray());
        }
    }

    private SpecialNamedaysStrategy getSpecialnamedaysHandler(NamedayLocale locale, NamedayJSON namedayJSON) {
        return factory.createStrategyForLocale(locale, namedayJSON);
    }

    private NamedayBundle getNamedayBundle(NamedayLocale locale, NamedayJSON namedayJSON) {
        if (locale.isComparedBySounds()) {
            return NamedayJSONParser.getNamedaysFromJSONasSounds(namedayJSON);
        } else {
            return NamedayJSONParser.getNamedaysFrom(namedayJSON);
        }
    }

    private void cacheCalendar(NamedayCalendar namedayCalendar) {
        cachedCalendar = namedayCalendar;
    }

    private boolean hasRequestedSameCalendar(NamedayLocale locale, int year) {
        return cachedCalendar != null && cachedCalendar.getYear() == year && cachedCalendar.getLocale().equals(locale);
    }

}
