package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.widgetprovider.UpcomingWidgetProvider;

public class UpcomingEventsWidgetRefresher {

    private final AppWidgetManager appWidgetManager;
    private final Context context;

    public UpcomingEventsWidgetRefresher(AppWidgetManager appWidgetManager, Context context) {
        this.appWidgetManager = appWidgetManager;
        this.context = context;
    }

    public void refreshLists() {
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(this.context, UpcomingWidgetProvider.class)
        );
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_upcoming_events_list);
    }
}
