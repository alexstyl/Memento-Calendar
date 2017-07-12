package com.alexstyl.specialdates.upcoming;

import android.content.Intent;

import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.donate.DonateMonitor;
import com.alexstyl.specialdates.donate.DonateMonitor.DonateMonitorListener;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.settings.EventsSettingsMonitor;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;

import java.util.List;

class UpcomingEventsPresenter implements OnUpcomingEventClickedListener {

    private final UpcomingListMVPView view;
    private final Analytics analytics;
    private final UpcomingEventsAsyncProvider upcomingEventsAsyncProvider;
    private final ContactPermissionRequest permissions;
    private final EventsSettingsMonitor monitor;
    private PeopleEventsObserver observer;
    private final MainNavigator navigator;
    private final ExternalNavigator externalNavigator;

    private boolean mustScrollToPosition;

    UpcomingEventsPresenter(UpcomingListMVPView view,
                            Analytics analytics,
                            UpcomingEventsAsyncProvider upcomingEventsAsyncProvider,
                            ContactPermissionRequest permissions,
                            EventsSettingsMonitor monitor,
                            PeopleEventsObserver observer,
                            MainNavigator navigator,
                            ExternalNavigator externalNavigator) {
        this.view = view;
        this.analytics = analytics;
        this.upcomingEventsAsyncProvider = upcomingEventsAsyncProvider;
        this.permissions = permissions;
        this.monitor = monitor;
        this.observer = observer;
        this.navigator = navigator;
        this.externalNavigator = externalNavigator;
    }

    void startPresenting() {
        DonateMonitor.getInstance().addListener(donationListener);
        monitor.register(onSettingsChangedListener);
        observer.startObserving(new PeopleEventsObserver.OnPeopleEventsChanged() {
            @Override
            public void onPeopleEventsUpdated() {
                refreshData();
            }
        });
        if (permissions.permissionIsPresent()) {
            refreshData();
        }
    }

    void refreshData() {
        view.showLoading();

        upcomingEventsAsyncProvider.reloadData(new UpcomingEventsAsyncProvider.Callback() {
            @Override
            public void onUpcomingEventsLoaded(List<UpcomingRowViewModel> events) {
                view.display(events);
                if (mustScrollToPosition) {
                    view.showFirstEvent();
                    mustScrollToPosition = false;
                }
            }
        });
    }

    void stopPresenting() {
        monitor.unregister();
        observer.stopObserving();
        DonateMonitor.getInstance().removeListener(donationListener);
    }

    private final DonateMonitorListener donationListener = new DonateMonitorListener() {
        @Override
        public void onUserDonated() {
            refreshData();
        }
    };

    private final EventsSettingsMonitor.Listener onSettingsChangedListener = new EventsSettingsMonitor.Listener() {
        @Override
        public void onSettingUpdated() {
            mustScrollToPosition = true;
            refreshData();
        }
    };

    void requestForPermissionIfNeeded() {
        if (!permissions.permissionIsPresent()) {
            permissions.requestForPermission();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissions.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMoreEventsClicked(UpcomingEventsViewModel viewModel) {
        onEventClicked(viewModel);
    }

    @Override
    public void onEventClicked(UpcomingEventsViewModel viewModel) {
        if (isDisplayingOnlyOneContact(viewModel)) {
            List<UpcomingContactEventViewModel> contactViewModels = viewModel.getContactViewModels();
            onContactClicked(contactViewModels.get(0).getContact());
        } else {
            navigator.toDateDetails(viewModel.getDate());
        }
    }

    private boolean isDisplayingOnlyOneContact(UpcomingEventsViewModel viewModel) {
        return viewModel.getContactViewModels().size() == 1
                && viewModel.getBankHolidayViewModel().isHidden()
                && viewModel.getNamedaysViewModel().isHidden();
    }

    @Override
    public void onContactClicked(Contact contact) {
        analytics.trackAction(new ActionWithParameters(Action.INTERACT_CONTACT, "source", "external"));
        externalNavigator.toContactDetails(contact);
    }
}
