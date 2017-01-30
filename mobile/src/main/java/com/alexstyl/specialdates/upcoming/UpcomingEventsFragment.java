package com.alexstyl.specialdates.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.support.transition.TransitionManager;
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
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.permissions.PermissionNavigator;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.alexstyl.specialdates.upcoming.view.UpcomingEventsListView;
import com.novoda.notils.caster.Views;

import java.util.List;

public class UpcomingEventsFragment extends MementoFragment {

    private static final ActionWithParameters action = new ActionWithParameters(Action.INTERACT_CONTACT, "source", "external");

    private ViewGroup root;
    private UpcomingEventsListView upcomingEventsListView;
    private ProgressBar progressBar;
    private TextView emptyView;
    private SettingsMonitor monitor;
    private UpcomingEventsProvider upcomingEventsProvider;
    private boolean mustScrollToPosition = true;
    private Analytics analytics;
    private ContactPermissionRequest permissions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = AnalyticsProvider.getAnalytics(getActivity());
        monitor = SettingsMonitor.newInstance(getActivity());
        monitor.initialise();
        upcomingEventsProvider = UpcomingEventsProvider.newInstance(getActivity(), onEventsLoadedListener);
        PermissionChecker checker = new PermissionChecker(getActivity());
        PermissionNavigator navigator = new PermissionNavigator(getActivity(), analytics);
        permissions = new ContactPermissionRequest(navigator, checker, callbacks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        root = Views.findById(view, R.id.root);
        progressBar = Views.findById(view, R.id.upcoming_events_progress);
        upcomingEventsListView = Views.findById(view, R.id.upcoming_eventslist);
        emptyView = Views.findById(view, R.id.upcoming_events_emptyview);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        upcomingEventsListView.setHasFixedSize(true);
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
        checkIfUserSettingsChanged();
        if (!permissions.permissionIsPresent()) {
            permissions.requestForPermission();
        }
    }

    private void checkIfUserSettingsChanged() {
        if (monitor.dataWasUpdated()) {
            mustScrollToPosition = true;
            startLoadingData();
            monitor.refreshData();
        }
    }

    private void startLoadingData() {
        showLoading();
        upcomingEventsProvider.reloadData();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        upcomingEventsListView.setVisibility(View.GONE);
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
        boolean displayingEvents = upcomingEventsListView.isDisplayingEvents();

        TransitionManager.beginDelayedTransition(root);
        if (displayingEvents) {
            upcomingEventsListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            upcomingEventsListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private final OnUpcomingEventClickedListener listClickListener = new OnUpcomingEventClickedListener() {
        @Override
        public void onContactEventPressed(View view, Contact contact) {
            analytics.trackAction(action);
            contact.displayQuickInfo(getActivity(), view);
        }

        @Override
        public void onCardPressed(Date date) {
            analytics.trackScreen(Screen.DATE_DETAILS);
            Intent intent = DateDetailsActivity.getStartIntent(getActivity(), date);
            startActivity(intent);
        }
    };

    private final UpcomingEventsProvider.LoadingListener onEventsLoadedListener = new UpcomingEventsProvider.LoadingListener() {
        @Override
        public void onUpcomingEventsLoaded(List<UpcomingRowViewModel> dates) {
            upcomingEventsListView.updateWith(dates, listClickListener);
            if (mustScrollToPosition) {
                upcomingEventsListView.scrollToPosition(0);
                mustScrollToPosition = false;
            }
            hideLoading();
            showData();
        }
    };

}
