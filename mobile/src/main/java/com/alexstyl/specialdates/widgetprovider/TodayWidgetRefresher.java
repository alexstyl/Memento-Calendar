package com.alexstyl.specialdates.widgetprovider;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.wear.WidgetRefresher;

public class TodayWidgetRefresher implements WidgetRefresher {

    private final Context context;
    private final AppWidgetManager instance;

    public TodayWidgetRefresher(Context context, AppWidgetManager instance) {
        this.context = context;
        this.instance = instance;
    }

    @Override
    public void refreshView() {
        Intent intent = new Intent(context, TodayAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int ids[] = instance.getAppWidgetIds(
                new ComponentName(context, TodayAppWidgetProvider.class)
        );

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
