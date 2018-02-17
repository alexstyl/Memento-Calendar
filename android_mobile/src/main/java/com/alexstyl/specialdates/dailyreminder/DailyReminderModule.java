package com.alexstyl.specialdates.dailyreminder;

import android.app.NotificationManager;
import android.content.Context;

import com.alexstyl.resources.Colors;
import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.images.ImageLoader;

import dagger.Module;
import dagger.Provides;

@Module
public class DailyReminderModule {

    @Provides
    DailyReminderNotifier notifier(Context context,
                                   ImageLoader imageLoader,
                                   Strings strings,
                                   Colors colors,
                                   DimensionResources dimensionResources,
                                   DailyReminderPreferences preferences) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return new DailyReminderNotifier(context, notificationManager, imageLoader, strings, colors, dimensionResources, preferences);
    }

    @Provides
    DailyReminderPreferences preferences(Context context) {
        return DailyReminderPreferences.newInstance(context);
    }
}
