package com.alexstyl.specialdates.events.namedays;

import android.content.Context;
import android.content.res.Resources;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.AndroidJSONResourceLoader;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayJSONProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.RomanianEasterSpecialCalculator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class NamedayModule {

    @Provides
    NamedayJSONProvider namedayJSONProvider(Resources resources) {
        return new NamedayJSONProvider(new AndroidJSONResourceLoader(resources));
    }

    @Provides
    RomanianEasterSpecialCalculator romanianEasterSpecialCalculator(OrthodoxEasterCalculator calculator) {
        return new RomanianEasterSpecialCalculator(calculator);
    }

    @Provides
    OrthodoxEasterCalculator calculator() {
        return new OrthodoxEasterCalculator();
    }


    @Provides
    @Singleton
    NamedayCalendarProvider provider(NamedayJSONProvider namedayJSONProvider,
                                     OrthodoxEasterCalculator orthodoxEasterCalculator,
                                     RomanianEasterSpecialCalculator romanianEasterSpecialCalculator) {
        return new NamedayCalendarProvider(namedayJSONProvider, orthodoxEasterCalculator, romanianEasterSpecialCalculator);
    }

    @Provides
    NamedayUserSettings userSettings(Context context) {
        return new NamedayPreferences(context);
    }

    @Provides
    NamedayCalendar calendar(NamedayUserSettings settings, NamedayCalendarProvider namedayCalendarProvider) {
        NamedayLocale selectedLanguage = settings.getSelectedLanguage();
        int year = Date.Companion.getCURRENT_YEAR();
        return namedayCalendarProvider.loadNamedayCalendarForLocale(selectedLanguage, year);
    }

}
