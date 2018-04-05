package com.alexstyl.specialdates.debug;

import android.content.Context;

import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugModule {

    @Provides
    DailyReminderDebugPreferences debugPreferences(Context context) {
        return DailyReminderDebugPreferences.newInstance(context);
    }
}
