package com.alexstyl.specialdates.upcoming.widget.list;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.AddEventActivity;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Widget;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.home.HomeActivity;
import com.alexstyl.specialdates.permissions.MementoPermissions;

import javax.inject.Inject;

public class UpcomingEventsScrollingAppWidgetProvider extends AppWidgetProvider {
    @Inject Analytics analytics;
    @Inject DateLabelCreator dateLabelCreator;
    @Inject CrashAndErrorTracker tracker;
    @Inject MementoPermissions permissionChecker;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppComponent applicationModule = ((MementoApplication) context.getApplicationContext()).getApplicationModule();
        applicationModule.inject(this);
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        analytics.trackWidgetAdded(Widget.UPCOMING_EVENTS_SCROLLING);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        analytics.trackWidgetRemoved(Widget.UPCOMING_EVENTS_SCROLLING);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        if (permissionChecker.canReadAndWriteContacts()) {
            showUpcomingEvents(context, appWidgetManager, appWidgetIds);
        } else {
            askForContactReadPermission(context, appWidgetManager, appWidgetIds);
        }
    }

    private void showUpcomingEvents(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String dateLabel = dateLabelCreator.createWithYearPreferred(Date.Companion.today());
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, UpcomingEventsRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_upcoming_events);
            remoteViews.setRemoteAdapter(R.id.widget_upcoming_events_list, intent);
            remoteViews.setTextViewText(R.id.widget_upcoming_events_date, dateLabel);
            setAddEventClickListener(context, remoteViews);
            setListClickListener(context, remoteViews);
            setListHeaderClickListener(context, remoteViews);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private void setAddEventClickListener(Context context, RemoteViews remoteViews) {
        Intent intent = AddEventActivity.buildIntent(context);
        PendingIntent todayDatePendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_upcoming_events_add_event, todayDatePendingIntent);
    }

    private void setListHeaderClickListener(Context context, RemoteViews remoteViews) {
        PendingIntent todayDatePendingIntent = pendingIntentToMain(context);
        remoteViews.setOnClickPendingIntent(R.id.widget_upcoming_events_date, todayDatePendingIntent);
    }

    private void setListClickListener(Context context, RemoteViews remoteViews) {
        Intent clickIntent = WidgetRouterActivity.Companion.buildIntent(context);
        PendingIntent listPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_upcoming_events_list, listPendingIntent);
    }

    private PendingIntent pendingIntentToMain(Context context) {
        Intent clickIntent = new Intent(context, HomeActivity.class);
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
