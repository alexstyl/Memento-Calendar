package com.alexstyl.specialdates.widgetprovider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.ui.activity.MainActivity;

public class UpcomingEventsAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Date date = Date.today();
        String dateLabel = DateDisplayStringCreator.INSTANCE.fullyFormattedDate(date);

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
            PendingIntent todayDatePendingIntent = pendingIntentFor(context);
            remoteViews.setOnClickPendingIntent(R.id.widget_upcoming_events_date, todayDatePendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private PendingIntent pendingIntentFor(Context context) {
        Intent clickIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
