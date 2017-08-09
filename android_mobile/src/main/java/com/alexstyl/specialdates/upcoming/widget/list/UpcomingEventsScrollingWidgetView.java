package com.alexstyl.specialdates.upcoming.widget.list;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.PeopleEventsView;

public class UpcomingEventsScrollingWidgetView implements PeopleEventsView {

    private final AppWidgetManager widgetManager;
    private final Context context;

    public UpcomingEventsScrollingWidgetView(Context context, AppWidgetManager widgetManager) {
        this.widgetManager = widgetManager;
        this.context = context;
    }

    @Override
    public void requestUpdate() {
        Intent intent = new Intent(context, UpcomingEventsScrollingAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = widgetManager.getAppWidgetIds(
                new ComponentName(context, UpcomingEventsScrollingAppWidgetProvider.class)
        );

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_upcoming_events_list);

        context.sendBroadcast(intent);
    }
}
