package com.alexstyl.specialdates;

import com.alexstyl.resources.ResourcesModule;
import com.alexstyl.specialdates.addevent.AddEventActivity;
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.analytics.AnalyticsModule;
import com.alexstyl.specialdates.contact.ContactsModule;
import com.alexstyl.specialdates.dailyreminder.DailyReminderIntentService;
import com.alexstyl.specialdates.dailyreminder.DailyReminderModule;
import com.alexstyl.specialdates.date.DateModule;
import com.alexstyl.specialdates.donate.DonateActivity;
import com.alexstyl.specialdates.donate.DonateModule;
import com.alexstyl.specialdates.events.namedays.NamedayModule;
import com.alexstyl.specialdates.events.namedays.activity.NamedayActivity;
import com.alexstyl.specialdates.events.namedays.activity.NamedaysInADayModule;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule;
import com.alexstyl.specialdates.facebook.FacebookProfileActivity;
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity;
import com.alexstyl.specialdates.images.ImageModule;
import com.alexstyl.specialdates.person.PersonActivity;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.settings.DailyReminderFragment;
import com.alexstyl.specialdates.settings.MainPreferenceFragment;
import com.alexstyl.specialdates.settings.NamedayListPreference;
import com.alexstyl.specialdates.support.RateDialog;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.alexstyl.specialdates.ui.widget.CompactCardView;
import com.alexstyl.specialdates.ui.widget.ViewModule;
import com.alexstyl.specialdates.upcoming.UpcomingEventsActivity;
import com.alexstyl.specialdates.upcoming.UpcomingEventsFragment;
import com.alexstyl.specialdates.upcoming.UpcomingEventsModule;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsRemoteViewService;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingAppWidgetProvider;
import com.alexstyl.specialdates.upcoming.widget.list.WidgetRouterActivity;
import com.alexstyl.specialdates.upcoming.widget.today.TodayAppWidgetProvider;
import com.alexstyl.specialdates.upcoming.widget.today.UpcomingWidgetConfigureActivity;
import com.alexstyl.specialdates.wear.WearSyncService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        AnalyticsModule.class,
        ResourcesModule.class,
        ContactsModule.class,
        DateModule.class,
        ImageModule.class,
        ViewModule.class,
        NamedayModule.class,
        UpcomingEventsModule.class,
        NamedaysInADayModule.class,
        DailyReminderModule.class,
        DonateModule.class,
        PeopleEventsModule.class
})
public interface AppComponent {
    void inject(UpcomingEventsActivity activity);

    void inject(UpcomingEventsFragment fragment);

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

    void inject(PersonActivity activity);

    void inject(NamedayActivity activity);

    void inject(EventDatePickerDialogFragment fragment);

    void inject(DailyReminderIntentService service);

    void inject(UpcomingEventsRemoteViewService viewService);

    void inject(ContactSuggestionView view);

    void inject(NamedayListPreference preference);

    void inject(WearSyncService service);

    void inject(ColorImageView view);

    void inject(CompactCardView view);

    void inject(UpcomingWidgetConfigureActivity activity);

    void inject(WidgetRouterActivity widgetRouterActivity);
}
