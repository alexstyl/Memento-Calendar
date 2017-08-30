package com.alexstyl.specialdates.upcoming.widget.today;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Widget;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;
import com.alexstyl.specialdates.util.NaturalLanguageUtils;

import javax.inject.Inject;

public class TodayAppWidgetProvider extends AppWidgetProvider {

    private PermissionChecker permissionChecker;
    private UpcomingWidgetPreferences preferences;
    private WidgetImageLoader widgetImageLoader;
    @Inject Analytics analytics;
    @Inject Strings strings;
    @Inject ImageLoader imageLoader;
    @Inject NamedayUserSettings namedayUserSettings;
    @Inject ContactsProvider contactsProvider;
    @Inject DateLabelCreator labelCreator;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppComponent applicationModule = ((MementoApplication) context.getApplicationContext()).getApplicationModule();
        applicationModule.inject(this);
        widgetImageLoader = new WidgetImageLoader(AppWidgetManager.getInstance(context), imageLoader);
        preferences = new UpcomingWidgetPreferences(context);
        permissionChecker = new PermissionChecker(context);
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        analytics.trackWidgetAdded(Widget.UPCOMING_EVENTS_SIMPLE);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        analytics.trackWidgetRemoved(Widget.UPCOMING_EVENTS_SIMPLE);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (permissionChecker.canReadAndWriteContacts()) {
            updateTodayWidget(context, appWidgetManager, appWidgetIds);
        } else {
            promptForContactPermission(context, appWidgetManager, appWidgetIds);
        }
    }

    private void updateTodayWidget(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        new QueryUpcomingPeopleEventsTask(PeopleEventsProvider.newInstance(context, namedayUserSettings, contactsProvider)) {
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

    private void updateForDate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds, ContactEventsOnADate contactEvents) {
        Date eventDate = contactEvents.getDate();
        Date date = Date.Companion.on(eventDate.getDayOfMonth(), eventDate.getMonth(), Date.Companion.today().getYear());
        Intent intent = UpcomingEventsActivity.getStartIntent(context, date);
        intent.setData(Uri.parse(String.valueOf(date.hashCode())));

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        String title = labelOf(date);

        String label = NaturalLanguageUtils.joinContacts(strings, contactEvents.getContacts(), 2);

        WidgetVariant selectedVariant = preferences.getSelectedVariant();
        TransparencyColorCalculator transparencyColorCalculator = new TransparencyColorCalculator();
        float opacity = preferences.getOppacityLevel();
        int selectedTextColor = context.getResources().getColor(selectedVariant.getTextColor());

        WidgetColorCalculator calculator = new WidgetColorCalculator(selectedTextColor);
        int finalHeaderColor = calculator.getColor(Date.Companion.today(), date);
        int avatarSizeInPx = context.getResources().getDimensionPixelSize(R.dimen.widget_avatar_size);
        for (int appWidgetId : appWidgetIds) {
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

            widgetImageLoader.loadPicture(contactEvents.getContacts(), appWidgetId, remoteViews, avatarSizeInPx);
        }
    }

    private String labelOf(Date todayDate) {
        return labelCreator.createLabelFor(todayDate);
    }

    public void onUpdateNoEventsFound(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_today_nocontacts
            );
            Intent intent = new Intent(context, UpcomingEventsActivity.class);
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
        Intent clickIntent = new Intent(context, UpcomingEventsActivity.class);
        return PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
