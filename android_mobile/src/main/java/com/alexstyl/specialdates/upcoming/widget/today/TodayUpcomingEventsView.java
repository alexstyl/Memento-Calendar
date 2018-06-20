package com.alexstyl.specialdates.upcoming.widget.today;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.UpcomingEventsView;

public class TodayUpcomingEventsView implements UpcomingEventsView {

    private final Context context;
    private final AppWidgetManager instance;

    public TodayUpcomingEventsView(Context context, AppWidgetManager instance) {
        this.context = context;
        this.instance = instance;
    }

    @Override
    public void reloadUpcomingEventsView() {
        Intent intent = new Intent(context, TodayAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = instance.getAppWidgetIds(new ComponentName(context, TodayAppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
