package com.alexstyl.specialdates.events.namedays.calendar;

import android.content.Context;
import android.content.res.Resources;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.namedays.NamedayBundle;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.resource.AndroidJSONResourceLoader;
import com.alexstyl.specialdates.events.namedays.resource.NamedayJSON;
import com.alexstyl.specialdates.events.namedays.resource.NamedayJSONParser;
import com.alexstyl.specialdates.events.namedays.resource.NamedayJSONResourceProvider;

import org.json.JSONArray;
import org.json.JSONException;

public class NamedayCalendarProvider {

    private static NamedayCalendar cachedCalendar;
    private final SpecialNamedaysHandlerFactory factory;
    private final NamedayJSONResourceProvider jsonResourceProvider;

    public static NamedayCalendarProvider newInstance(Context context) {
        SpecialNamedaysHandlerFactory factory = new SpecialNamedaysHandlerFactory();
        Resources resources = context.getResources();
        AndroidJSONResourceLoader loader = new AndroidJSONResourceLoader(resources);
        NamedayJSONResourceProvider jsonResourceProvider = new NamedayJSONResourceProvider(loader);
        return new NamedayCalendarProvider(factory, jsonResourceProvider);
    }

    private NamedayCalendarProvider(SpecialNamedaysHandlerFactory factory, NamedayJSONResourceProvider jsonResourceProvider) {
        this.factory = factory;
        this.jsonResourceProvider = jsonResourceProvider;
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
            return jsonResourceProvider.getNamedayJSONFor(locale);
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
            return NamedayJSONParser.createAsSounds(namedayJSON);
        } else {
            return NamedayJSONParser.create(namedayJSON);
        }
    }

    private void cacheCalendar(NamedayCalendar namedayCalendar) {
        cachedCalendar = namedayCalendar;
    }

    private boolean hasRequestedSameCalendar(NamedayLocale locale, int year) {
        return cachedCalendar != null && cachedCalendar.getYear() == year && cachedCalendar.getLocale().equals(locale);
    }

}
