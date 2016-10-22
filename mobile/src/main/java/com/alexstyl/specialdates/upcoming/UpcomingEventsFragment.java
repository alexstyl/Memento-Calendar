package com.alexstyl.specialdates.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.specialdates.Navigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.analytics.Screen;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.alexstyl.specialdates.upcoming.view.UpcomingEventsListView;
import com.alexstyl.specialdates.views.FabPaddingSetter;
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
    private GoToTodayEnabler goToTodayEnabler;
    private Analytics analytics;
    private Navigator navigator;
    private ContactPermissionRequest permissions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = AnalyticsProvider.getAnalytics(getActivity());
        navigator = new Navigator(getActivity(), analytics);
        monitor = SettingsMonitor.newInstance(getActivity());
        monitor.initialise();
        goToTodayEnabler = new GoToTodayEnabler(getMementoActivity());
        upcomingEventsProvider = UpcomingEventsProvider.newInstance(getActivity(), onEventsLoadedListener);
        PermissionChecker checker = new PermissionChecker(getActivity());
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
        new FabPaddingSetter().setBottomPaddingTo(upcomingEventsListView);
        upcomingEventsListView.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_upcoming_dark, menu);
        goToTodayEnabler.reattachTo(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                goToToday();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToToday() {
        analytics.trackAction(Action.GO_TO_TODAY);
        upcomingEventsListView.scrollToToday(true);
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
        public void onContactEventPressed(View view, ContactEvent contact) {
            analytics.trackAction(action);
            contact.getContact().displayQuickInfo(getActivity(), view);
        }

        @Override
        public void onCardPressed(Date date) {
            analytics.trackScreen(Screen.DATE_DETAILS);
            Intent intent = DateDetailsActivity.getStartIntent(getActivity(), date.getDayOfMonth(), date.getMonth(), date.getYear());
            startActivity(intent);
        }
    };

    private final UpcomingEventsProvider.LoadingListener onEventsLoadedListener = new UpcomingEventsProvider.LoadingListener() {
        @Override
        public void onUpcomingEventsLoaded(List<CelebrationDate> dates) {
            upcomingEventsListView.updateWith(dates, listClickListener);
            if (mustScrollToPosition) {
                upcomingEventsListView.scrollToToday(false);
                mustScrollToPosition = false;
            }
            goToTodayEnabler.validateGoToTodayButton(upcomingEventsListView);
            hideLoading();
            showData();
        }
    };
}
