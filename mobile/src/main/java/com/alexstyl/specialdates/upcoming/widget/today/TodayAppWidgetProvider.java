package com.alexstyl.specialdates.upcoming.widget.today;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.RemoteViews;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.activity.MainActivity;
import com.alexstyl.specialdates.util.NaturalLanguageUtils;

public class TodayAppWidgetProvider extends AppWidgetProvider {

    private WidgetImageLoader imageLoader;
    private StringResources stringResources;

    @Override
    public void onReceive(Context context, Intent intent) {
        getOrCreateImageLoader(context);
        super.onReceive(context, intent);
    }

    private void getOrCreateImageLoader(Context context) {
        if (imageLoader == null) {
            imageLoader = new WidgetImageLoader(
                    AppWidgetManager.getInstance(context),
                    UILImageLoader.createLoader(context.getResources())
            );
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (new PermissionChecker(context).canReadAndWriteContacts()) {
            updateTodayWidget(context, appWidgetManager, appWidgetIds);
        } else {
            promptForContactPermission(context, appWidgetManager, appWidgetIds);
        }
    }

    private void updateTodayWidget(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        new QueryUpcomingPeopleEventsTask(PeopleEventsProvider.newInstance(context)) {
            @Override
            void onNextDateLoaded(ContactEventsOnADate events) {
                updateForDate(context, appWidgetManager, appWidgetIds, events);
            }

            @Override
            void onNoEventsFound() {
                onUpdateNoEventsFound(context, appWidgetManager, appWidgetIds);
            }

        }.execute();
    }

    StringResources getOrCreateStringResources(Resources resources) {
        if (stringResources == null) {
            stringResources = new AndroidStringResources(resources);
        }
        return stringResources;
    }

    private void updateForDate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds, ContactEventsOnADate contactEvents) {
        Date eventDate = contactEvents.getDate();
        Date date = Date.on(eventDate.getDayOfMonth(), eventDate.getMonth(), Date.today().getYear());
        Intent intent = DateDetailsActivity.getStartIntent(context, date);
        intent.setData(Uri.parse(String.valueOf(date.hashCode())));

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        String title = toString(context, date);

        final int N = appWidgetIds.length;

        String label = NaturalLanguageUtils.joinContacts(getOrCreateStringResources(context.getResources()), contactEvents.getContacts(), 2);

        UpcomingWidgetPreferences preferences = new UpcomingWidgetPreferences(context);
        WidgetVariant selectedVariant = preferences.getSelectedVariant();
        TransparencyColorCalculator transparencyColorCalculator = new TransparencyColorCalculator();
        float opacity = preferences.getOppacityLevel();
        int selectedTextColor = context.getResources().getColor(selectedVariant.getTextColor());

        WidgetColorCalculator calculator = new WidgetColorCalculator(selectedTextColor);
        int finalHeaderColor = calculator.getColor(Date.today(), date);
        int avatarSizeInPx = context.getResources().getDimensionPixelSize(R.dimen.widget_avatar_size);
        for (int i = 0; i < N; i++) {
            final int appWidgetId = appWidgetIds[i];

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_today);

            remoteViews.setTextViewText(R.id.upcoming_widget_header, title);
            remoteViews.setTextViewText(R.id.upcoming_widget_events_text, label);

            remoteViews.setTextColor(R.id.upcoming_widget_events_text, selectedTextColor);
            remoteViews.setTextColor(R.id.upcoming_widget_header, finalHeaderColor);

            int background = context.getResources().getColor(selectedVariant.getBackgroundColorResId());
            int backgroundColor = transparencyColorCalculator.calculateColor(background, opacity);
            remoteViews.setInt(R.id.upcoming_widget_background_image, "setBackgroundColor", backgroundColor);

            remoteViews.setOnClickPendingIntent(R.id.upcoming_widget_background, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            imageLoader.loadPicture(contactEvents.getContacts(), appWidgetId, remoteViews, avatarSizeInPx);
        }
    }

    private String toString(Context context, Date todayDate) {
        return DateFormatUtils.formatTimeStampString(context, todayDate.toMillis(), false, true);
    }

    public void onUpdateNoEventsFound(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_today_nocontacts
            );
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            views.setTextViewText(android.R.id.text1, context.getString(R.string.today_widget_empty));
            views.setOnClickPendingIntent(R.id.upcoming_widget_background, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void promptForContactPermission(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_prompt_permissions);
        remoteViews.setOnClickPendingIntent(R.id.widget_prompt_permission_background, pendingIntentToMain(context));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    private PendingIntent pendingIntentToMain(Context context) {
        Intent clickIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
