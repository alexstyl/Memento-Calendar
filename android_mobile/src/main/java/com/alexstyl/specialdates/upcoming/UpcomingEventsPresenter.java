package com.alexstyl.specialdates.upcoming;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.donate.DonateMonitor;
import com.alexstyl.specialdates.donate.DonateMonitor.DonateMonitorListener;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.settings.EventsSettingsMonitor;
import com.alexstyl.specialdates.ui.activity.MainNavigator;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.util.List;

class UpcomingEventsPresenter implements OnUpcomingEventClickedListener {

    private static final ActionWithParameters action = new ActionWithParameters(Action.INTERACT_CONTACT, "source", "external");

    private final UpcomingListMVPView view;
    private final Analytics analytics;
    private final UpcomingEventsAsyncProvider upcomingEventsAsyncProvider;
    private final ContactPermissionRequest permissions;
    private final EventsSettingsMonitor monitor;
    private final ContactsObserver contactsObserver;
    private final MainNavigator navigator;
    private final Context context;

    private boolean mustScrollToPosition;

    UpcomingEventsPresenter(UpcomingListMVPView view,
                            Analytics analytics,
                            UpcomingEventsAsyncProvider upcomingEventsAsyncProvider,
                            ContactPermissionRequest permissions,
                            EventsSettingsMonitor monitor,
                            ContactsObserver contactsObserver,
                            MainNavigator navigator, Context context) {
        this.view = view;
        this.analytics = analytics;
        this.upcomingEventsAsyncProvider = upcomingEventsAsyncProvider;
        this.permissions = permissions;
        this.monitor = monitor;
        this.contactsObserver = contactsObserver;
        this.navigator = navigator;
        this.context = context;
    }

    void startPresenting() {
        DonateMonitor.getInstance().addListener(donationListener);
        monitor.register(onSettingsChangedListener);
        contactsObserver.registerWith(new ContactsObserver.Callback() {
            @Override
            public void onContactsUpdated() {
                startLoadingData();
            }
        });
        if (permissions.permissionIsPresent()) {
            startLoadingData();
        }
    }

    private void startLoadingData() {
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
        contactsObserver.unregister();
        DonateMonitor.getInstance().removeListener(donationListener);
    }

    private final DonateMonitorListener donationListener = new DonateMonitorListener() {
        @Override
        public void onUserDonated() {
            startLoadingData();
        }
    };

    private final EventsSettingsMonitor.Listener onSettingsChangedListener = new EventsSettingsMonitor.Listener() {
        @Override
        public void onSettingUpdated() {
            mustScrollToPosition = true;
            startLoadingData();
        }
    };

    void refreshData() {
        startLoadingData();
    }

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
            List<ContactEventViewModel> contactViewModels = viewModel.getContactViewModels();
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
        analytics.trackAction(action);
        contact.displayQuickInfo(context);
    }
}
