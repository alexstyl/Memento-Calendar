package com.alexstyl.specialdates.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.android.AndroidStringResources;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.donate.DonateMonitor;
import com.alexstyl.specialdates.donate.DonateMonitor.DonateMonitorListener;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.permissions.PermissionNavigator;
import com.alexstyl.specialdates.settings.EventsSettingsMonitor;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.alexstyl.specialdates.util.ContactsObserver;
import com.novoda.notils.caster.Views;

import java.util.List;

public class UpcomingEventsFragment extends MementoFragment {

    private static final ActionWithParameters action = new ActionWithParameters(Action.INTERACT_CONTACT, "source", "external");

    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView emptyView;
    private EventsSettingsMonitor monitor;
    private UpcomingEventsAsyncProvider upcomingEventsAsyncProvider;
    private boolean mustScrollToPosition = true;
    private Analytics analytics;
    private ContactPermissionRequest permissions;
    private ContactsObserver contactsObserver;
    private UpcomingEventsAdapter adapter;
    private RecyclerView upcomingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = AnalyticsProvider.getAnalytics(getActivity());
        upcomingEventsAsyncProvider = UpcomingEventsAsyncProvider.newInstance(getActivity(), onEventsLoadedListener);
        PermissionChecker checker = new PermissionChecker(getActivity());
        PermissionNavigator navigator = new PermissionNavigator(getActivity(), analytics);
        permissions = new ContactPermissionRequest(navigator, checker, callbacks);
        monitor = new EventsSettingsMonitor(PreferenceManager.getDefaultSharedPreferences(getActivity()), new AndroidStringResources(getResources()));
        monitor.register(new EventsSettingsMonitor.Listener() {
            @Override
            public void onSettingUpdated() {
                mustScrollToPosition = true;
                startLoadingData();
            }
        });
        contactsObserver = new ContactsObserver(getContentResolver(), new Handler());
        contactsObserver.registerWith(new ContactsObserver.Callback() {
            @Override
            public void onContactsUpdated() {
                startLoadingData();
            }
        });

        DonateMonitor.getInstance().addListener(onDonationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        root = Views.findById(view, R.id.root);
        progressBar = Views.findById(view, R.id.upcoming_events_progress);

        upcomingList = Views.findById(view, R.id.upcoming_events_list);
        upcomingList.setHasFixedSize(true);
        upcomingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        upcomingList.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.upcoming_vertical_padding_between_cards), 1));

        adapter = new UpcomingEventsAdapter(new UpcomingViewHolderFactory(inflater, UILImageLoader.createCircleLoader(getResources())));
        upcomingList.setAdapter(adapter);

        emptyView = Views.findById(view, R.id.upcoming_events_emptyview);
        return view;
    }

    private final PermissionCallbacks callbacks = new PermissionCallbacks() {
        @Override
        public void onPermissionGranted() {
            startLoadingData();
        }

        @Override
        public void onPermissionDenied() {
            getActivity().finishAffinity();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (permissions.permissionIsPresent()) {
            startLoadingData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!permissions.permissionIsPresent()) {
            permissions.requestForPermission();
        }
    }

    private void startLoadingData() {
        showLoading();
        upcomingEventsAsyncProvider.reloadData();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        upcomingList.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissions.onActivityResult(requestCode, resultCode, data);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        TransitionManager.beginDelayedTransition(root);
        if (adapter.isDisplayingEvents()) {
            upcomingList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            upcomingList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private final OnUpcomingEventClickedListener listClickListener = new OnUpcomingEventClickedListener() {
        @Override
        public void onContactEventPressed(Contact contact) {
            analytics.trackAction(action);
            contact.displayQuickInfo(getActivity());
        }

        @Override
        public void onCardPressed(UpcomingEventsViewModel viewModel) {
            if (isDisplayingOnlyOneContact(viewModel)) {
                List<ContactEventViewModel> contactViewModels = viewModel.getContactViewModels();
                onContactEventPressed(contactViewModels.get(0).getContact());
            } else {
                analytics.trackScreen(Screen.DATE_DETAILS);
                Intent intent = DateDetailsActivity.getStartIntent(getActivity(), viewModel.getDate());
                startActivity(intent);
            }
        }

        @Override
        public void onMoreButtonPressed(UpcomingEventsViewModel viewModel) {
            onCardPressed(viewModel);
        }

        private boolean isDisplayingOnlyOneContact(UpcomingEventsViewModel viewModel) {
            return viewModel.getContactViewModels().size() == 1
                    && viewModel.getBankHolidayViewModel().isHidden()
                    && viewModel.getNamedaysViewModel().isHidden();
        }
    };

    private final UpcomingEventsAsyncProvider.LoadingListener onEventsLoadedListener = new UpcomingEventsAsyncProvider.LoadingListener() {
        @Override
        public void onUpcomingEventsLoaded(List<UpcomingRowViewModel> dates) {
            adapter.displayUpcomingEvents(dates, listClickListener);
            if (mustScrollToPosition) {
                upcomingList.scrollToPosition(0);
                mustScrollToPosition = false;
            }
            hideLoading();
            showData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        monitor.unregister();
        contactsObserver.unregister();
        DonateMonitor.getInstance().removeListener(onDonationListener);
    }

    private final DonateMonitorListener onDonationListener = new DonateMonitorListener() {
        @Override
        public void onUserDonated() {
            startLoadingData();
        }
    };
}
