package com.alexstyl.specialdates.upcoming.widget.list;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Widget;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;

public class UpcomingEventsScrollingAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AnalyticsProvider.getAnalytics(context).trackWidgetAdded(Widget.UPCOMING_EVENTS_SCROLLING);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AnalyticsProvider.getAnalytics(context).trackWidgetRemoved(Widget.UPCOMING_EVENTS_SCROLLING);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        PermissionChecker permissionChecker = new PermissionChecker(context);
        if (permissionChecker.canReadAndWriteContacts()) {
            showUpcomingEvents(context, appWidgetManager, appWidgetIds);
        } else {
            askForContactReadPermission(context, appWidgetManager, appWidgetIds);
        }
    }

    private void showUpcomingEvents(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Date date = Date.today();
        String dateLabel = DateDisplayStringCreator.INSTANCE.fullyFormattedDate(date, context);

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, UpcomingEventsRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_upcoming_events);
            remoteViews.setRemoteAdapter(R.id.widget_upcoming_events_list, intent);
            Intent clickIntent = new Intent(context, DateDetailsActivity.class);
            PendingIntent listPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_upcoming_events_list, listPendingIntent);

            remoteViews.setTextViewText(R.id.widget_upcoming_events_date, dateLabel);
            PendingIntent todayDatePendingIntent = pendingIntentToMain(context);
            remoteViews.setOnClickPendingIntent(R.id.widget_upcoming_events_date, todayDatePendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private PendingIntent pendingIntentToMain(Context context) {
        Intent clickIntent = new Intent(context, UpcomingEventsActivity.class);
        return PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void askForContactReadPermission(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_prompt_permissions);
            remoteViews.setOnClickPendingIntent(R.id.widget_prompt_permission_background, pendingIntentToMain(context));
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }
}
