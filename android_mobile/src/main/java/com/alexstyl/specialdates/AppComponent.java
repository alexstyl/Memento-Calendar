package com.alexstyl.specialdates;

import com.alexstyl.resources.ResourcesModule;
import com.alexstyl.specialdates.addevent.AddEventActivity;
import com.alexstyl.specialdates.addevent.AddEventModule;
import com.alexstyl.specialdates.addevent.EventDatePickerDialogFragment;
import com.alexstyl.specialdates.addevent.ui.ContactSuggestionView;
import com.alexstyl.specialdates.analytics.AnalyticsModule;
import com.alexstyl.specialdates.contact.ContactsModule;
import com.alexstyl.specialdates.dailyreminder.DailyReminderModule;
import com.alexstyl.specialdates.dailyreminder.DailyReminderReceiver;
import com.alexstyl.specialdates.dailyreminder.actions.ContactActionsModule;
import com.alexstyl.specialdates.dailyreminder.actions.PersonActionsActivity;
import com.alexstyl.specialdates.date.DateModule;
import com.alexstyl.specialdates.donate.DonateActivity;
import com.alexstyl.specialdates.donate.DonateModule;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysModule;
import com.alexstyl.specialdates.events.namedays.NamedayModule;
import com.alexstyl.specialdates.events.namedays.activity.NamedaysInADayModule;
import com.alexstyl.specialdates.events.namedays.activity.NamedaysOnADayActivity;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule;
import com.alexstyl.specialdates.facebook.FacebookModule;
import com.alexstyl.specialdates.facebook.FacebookProfileActivity;
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity;
import com.alexstyl.specialdates.facebook.login.FacebookWebView;
import com.alexstyl.specialdates.home.DonationBannerView;
import com.alexstyl.specialdates.home.HomeActivity;
import com.alexstyl.specialdates.images.ImageModule;
import com.alexstyl.specialdates.people.PeopleFragment;
import com.alexstyl.specialdates.people.PeopleModule;
import com.alexstyl.specialdates.permissions.ContactPermissionActivity;
import com.alexstyl.specialdates.person.PersonActivity;
import com.alexstyl.specialdates.person.PersonModule;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.search.SearchModule;
import com.alexstyl.specialdates.settings.DailyReminderFragment;
import com.alexstyl.specialdates.settings.NamedayListPreference;
import com.alexstyl.specialdates.settings.UserSettingsFragment;
import com.alexstyl.specialdates.support.RateDialog;
import com.alexstyl.specialdates.theming.ThemingModule;
import com.alexstyl.specialdates.ui.base.ThemedMementoActivity;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.alexstyl.specialdates.ui.widget.ViewModule;
import com.alexstyl.specialdates.upcoming.UpcomingEventsFragment;
import com.alexstyl.specialdates.upcoming.UpcomingEventsModule;
import com.alexstyl.specialdates.upcoming.widget.RecentUpcomingPeopleEventsModule;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsRemoteViewService;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingAppWidgetProvider;
import com.alexstyl.specialdates.upcoming.widget.list.WidgetRouterActivity;
import com.alexstyl.specialdates.upcoming.widget.today.TodayAppWidgetProvider;
import com.alexstyl.specialdates.upcoming.widget.today.UpcomingWidgetConfigureActivity;
import com.alexstyl.specialdates.wear.WearSyncService;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AndroidApplicationModule.class,
        AnalyticsModule.class,
        ContactActionsModule.class,
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
        SearchModule.class,
        PeopleEventsModule.class,
        BankHolidaysModule.class,
        AddEventModule.class,
        FacebookModule.class,
        PeopleModule.class,
        BankHolidaysModule.class,
        ThemingModule.class,
        RecentUpcomingPeopleEventsModule.class,
        PersonModule.class
})
public interface AppComponent {
    void inject(MementoApplication application);

    void inject(HomeActivity activity);

    void inject(UpcomingEventsFragment fragment);

    void inject(AddEventActivity activity);

    void inject(SearchActivity activity);

    void inject(FacebookProfileActivity activity);

    void inject(FacebookLogInActivity activity);

    void inject(RateDialog activity);

    void inject(UserSettingsFragment fragment);

    void inject(DailyReminderFragment fragment);

    void inject(DonateActivity activity);

    void inject(UpcomingEventsScrollingAppWidgetProvider widgetProvider);

    void inject(TodayAppWidgetProvider widgetProvider);

    void inject(PersonActivity activity);

    void inject(NamedaysOnADayActivity activity);

    void inject(EventDatePickerDialogFragment fragment);

    void inject(UpcomingEventsRemoteViewService viewService);

    void inject(ContactSuggestionView view);

    void inject(NamedayListPreference preference);

    void inject(WearSyncService service);

    void inject(ColorImageView view);

    void inject(UpcomingWidgetConfigureActivity activity);

    void inject(WidgetRouterActivity widgetRouterActivity);

    void inject(DeviceConfigurationUpdatedReceiver receiver);

    void inject(ContactPermissionActivity activity);

    void inject(PeopleFragment peopleFragment);

    void inject(FacebookWebView peopleFragment);

    void inject(ThemedMementoActivity themedMementoActivity);

    void inject(PersonActionsActivity personActionsActivity);

    void inject(DonationBannerView donationBannerView);

    void inject(ReroutingActivity reroutingActivity);

    void inject(@NotNull DailyReminderReceiver dailyReminderReceiver);
}
