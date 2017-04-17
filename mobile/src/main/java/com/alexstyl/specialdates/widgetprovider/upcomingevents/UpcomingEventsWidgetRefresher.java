package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.wear.WidgetRefresher;
import com.alexstyl.specialdates.widgetprovider.UpcomingEventsAppWidgetProvider;

public class UpcomingEventsWidgetRefresher implements WidgetRefresher {

    private final AppWidgetManager widgetManager;
    private final Context context;

    public UpcomingEventsWidgetRefresher(Context context, AppWidgetManager widgetManager) {
        this.widgetManager = widgetManager;
        this.context = context;
    }

    @Override
    public void refreshView() {
        Intent intent = new Intent(context, UpcomingEventsAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int ids[] = widgetManager.getAppWidgetIds(
                new ComponentName(context, UpcomingEventsAppWidgetProvider.class)
        );

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_upcoming_events_list);

        context.sendBroadcast(intent);
    }
}
