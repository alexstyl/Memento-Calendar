package com.alexstyl.specialdates.events.namedays;

import android.content.Context;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.activity.NamedaysViewModelFactory;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.AndroidJSONResourceLoader;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayJSONProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.SpecialNamedaysHandlerFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class NamedayModule {

    private final Context context;

    public NamedayModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    NamedayCalendarProvider provider() {
        AndroidJSONResourceLoader loader = new AndroidJSONResourceLoader(context.getResources());
        return new NamedayCalendarProvider(new NamedayJSONProvider(loader), new SpecialNamedaysHandlerFactory());
    }

    @Provides
    NamedayUserSettings userSettings() {
        return new NamedayPreferences(context);
    }

    @Provides
    NamedaysViewModelFactory viewModelFactory() {
        return new NamedaysViewModelFactory(ContactsProvider.get(context));
    }

    @Provides
    NamedayCalendar calendar(NamedayUserSettings settings, NamedayCalendarProvider namedayCalendarProvider) {
        NamedayLocale selectedLanguage = settings.getSelectedLanguage();
        int year = Date.Companion.getCURRENT_YEAR();
        return namedayCalendarProvider.loadNamedayCalendarForLocale(selectedLanguage, year);
    }
}
