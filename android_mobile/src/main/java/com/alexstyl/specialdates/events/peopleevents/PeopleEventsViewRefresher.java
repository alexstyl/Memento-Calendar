package com.alexstyl.specialdates.events.peopleevents;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.PeopleEventsView;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingWidgetView;
import com.alexstyl.specialdates.upcoming.widget.today.TodayPeopleEventsView;
import com.alexstyl.specialdates.wear.WearSyncPeopleEventsView;

import java.util.Arrays;
import java.util.List;

public final class PeopleEventsViewRefresher {

    private final List<PeopleEventsView> views;
    private static PeopleEventsViewRefresher instance;

    public static PeopleEventsViewRefresher get(Context appContext) {
        if (instance == null) {
            ContentResolver resolver = appContext.getContentResolver();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(appContext);
            instance = new PeopleEventsViewRefresher(
                    Arrays.asList(
                            new ContentResolverPeopleEventsView(resolver),
                            new WearSyncPeopleEventsView(appContext),
                            new TodayPeopleEventsView(appContext, appWidgetManager),
                            new UpcomingEventsScrollingWidgetView(appContext, appWidgetManager)
                    ));
        }
        return instance;
    }

    private PeopleEventsViewRefresher(List<PeopleEventsView> views) {
        this.views = views;
    }

    public void updateAllViews() {
        for (PeopleEventsView view : views) {
            view.requestUpdate();
        }
    }
}
