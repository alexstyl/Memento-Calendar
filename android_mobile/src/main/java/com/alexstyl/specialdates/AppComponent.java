package com.alexstyl.specialdates;

import com.alexstyl.resources.ResourcesModule;
import com.alexstyl.specialdates.addevent.AddEventActivity;
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment;
import com.alexstyl.specialdates.analytics.AnalyticsModule;
import com.alexstyl.specialdates.dailyreminder.DailyReminderIntentService;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.datedetails.DateDetailsFragment;
import com.alexstyl.specialdates.donate.DonateActivity;
import com.alexstyl.specialdates.events.peopleevents.StaticEventsContentProvider;
import com.alexstyl.specialdates.facebook.FacebookProfileActivity;
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.DailyReminderFragment;
import com.alexstyl.specialdates.settings.MainPreferenceFragment;
import com.alexstyl.specialdates.support.RateDialog;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;
import com.alexstyl.specialdates.upcoming.UpcomingEventsFragment;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsRemoteViewService;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingAppWidgetProvider;
import com.alexstyl.specialdates.upcoming.widget.today.TodayAppWidgetProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AnalyticsModule.class, ResourcesModule.class})
public interface AppComponent {
    void inject(UpcomingEventsActivity activity);

    void inject(UpcomingEventsFragment fragment);

    void inject(DateDetailsActivity activity);

    void inject(DateDetailsFragment fragment);

    void inject(AddEventActivity activity);

    void inject(SearchActivity activity);

    void inject(FacebookProfileActivity activity);

    void inject(FacebookLogInActivity activity);

    void inject(RateDialog activity);

    void inject(MainPreferenceFragment fragment);

    void inject(DailyReminderFragment fragment);

    void inject(DonateActivity activity);

    void inject(UpcomingEventsScrollingAppWidgetProvider widgetProvider);

    void inject(TodayAppWidgetProvider widgetProvider);

    void inject(EventDatePickerDialogFragment fragment);

    void inject(DailyReminderIntentService service);

    void inject(StaticEventsContentProvider contentProvider);

    void inject(UpcomingEventsRemoteViewService viewService);
}
