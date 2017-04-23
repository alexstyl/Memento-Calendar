package com.alexstyl.specialdates;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import com.alexstyl.specialdates.wear.WearSyncWidgetRefresher;
import com.alexstyl.specialdates.wear.WidgetRefresher;
import com.alexstyl.specialdates.upcoming.widget.today.TodayWidgetRefresher;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsWidgetRefresher;

import java.util.Arrays;
import java.util.List;

public final class ExternalWidgetRefresher {

    private final List<WidgetRefresher> views;
    private static ExternalWidgetRefresher INSTANCE;

    public static ExternalWidgetRefresher get(Context appContext) {
        if (INSTANCE == null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(appContext);
            INSTANCE = new ExternalWidgetRefresher(
                    Arrays.asList(
                            new WearSyncWidgetRefresher(appContext),
                            new TodayWidgetRefresher(appContext, appWidgetManager),
                            new UpcomingEventsWidgetRefresher(appContext, appWidgetManager)
                    ));
        }
        return INSTANCE;
    }

    private ExternalWidgetRefresher(List<WidgetRefresher> views) {
        this.views = views;
    }

    public void refreshAllWidgets() {
        for (WidgetRefresher view : views) {
            view.refreshWidget();
        }
    }
}
