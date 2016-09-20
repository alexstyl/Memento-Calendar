package com.alexstyl.specialdates.search;

import android.content.Context;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;

public class NamedaysLoader extends SimpleAsyncTaskLoader<NameCelebrations> {

    private final NamedayPreferences namedayPreferences;

    private NamedayCalendar namedayCalendar;
    private final String searchQuery;
    private final int year;

    public static NamedaysLoader newInstance(Context context, String searchQuery) {
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        int year = DayDate.today().getYear();
        return new NamedaysLoader(context, namedayPreferences, searchQuery, year);
    }

    NamedaysLoader(Context context, NamedayPreferences namedayPreferences, String searchQuery, int year) {
        super(context);
        this.namedayPreferences = namedayPreferences;
        this.searchQuery = searchQuery;
        this.year = year;
    }

    @Override
    public NameCelebrations loadInBackground() {
        return getNamedays(searchQuery);
    }

    private NameCelebrations getNamedays(String searchQuery) {
        if (namedayPreferences.isEnabled()) {
            NameCelebrations namedays = getNamedayCalendar().getAllNamedays(searchQuery);
            namedays.sortDates();
            return namedays;
        }
        return NameCelebrations.EMPTY;
    }

    private NamedayCalendar getNamedayCalendar() {
        if (namedayCalendar == null) {
            NamedayLocale locale = NamedayPreferences.newInstance(getContext()).getSelectedLanguage();
            namedayCalendar = NamedayCalendarProvider.newInstance(getContext().getResources()).loadNamedayCalendarForLocale(locale, year);
        }
        return namedayCalendar;

    }
}
