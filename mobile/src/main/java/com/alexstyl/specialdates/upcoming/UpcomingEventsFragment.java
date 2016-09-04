package com.alexstyl.specialdates.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.datedetails.DateDetailsActivity;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.search.SearchActivity;
import com.alexstyl.specialdates.theming.Themer;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.upcoming.ui.UpcomingEventsListView;
import com.alexstyl.specialdates.views.FabPaddingSetter;
import com.novoda.notils.caster.Views;

import java.util.List;

public class UpcomingEventsFragment extends MementoFragment {

    private UpcomingEventsListView upcomingEventsListView;
    private ProgressBar progressBar;
    private TextView emptyView;

    private SettingsMonitor monitor;
    private MonthTitleSetter titleSetter;

    private UpcomingEventsProvider upcomingEventsProvider;

    private boolean mustScrollToPosition = true;
    private GoToTodayEnabler goToTodayEnabler;

    private Themer themer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        themer = Themer.get();

        monitor = SettingsMonitor.newInstance(getActivity());
        monitor.initialise();
        titleSetter = MonthTitleSetter.createSetterFor(getActivity());
        goToTodayEnabler = new GoToTodayEnabler(getMementoActivity());
        upcomingEventsProvider = UpcomingEventsProvider.newInstance(getActivity(), onEventsLoadedListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isUsingDarkIcons()) {
            inflater.inflate(R.menu.menu_upcoming_dark, menu);
        } else {
            inflater.inflate(R.menu.menu_upcoming_light, menu);
        }
        goToTodayEnabler.reattachTo(menu);
    }

    private boolean isUsingDarkIcons() {
        return themer.isActivityUsingDarkIcons(getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                upcomingEventsListView.scrollToToday(true);
                return true;
            case R.id.action_search: {
                onSearchRequested();
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        progressBar = Views.findById(view, R.id.upcoming_events_progress);
        upcomingEventsListView = Views.findById(view, R.id.upcoming_eventslist);
        emptyView = Views.findById(view, R.id.upcoming_events_emptyview);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new FabPaddingSetter().setBottomPaddingTo(upcomingEventsListView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoading();
        refreshData();
    }

    private void refreshData() {
        upcomingEventsProvider.reloadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIfUserSettingsChanged();
    }

    private void checkIfUserSettingsChanged() {
        if (monitor.dataWasUpdated()) {
            mustScrollToPosition = true;
            showLoading();
            refreshData();
            monitor.refreshData();
        }
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        boolean displayingEvents = upcomingEventsListView.isDisplayingEvents();
        if (displayingEvents) {
            upcomingEventsListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            upcomingEventsListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        upcomingEventsListView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    private final UpcomingEventsListView.Listener listClickListener = new UpcomingEventsListView.Listener() {
        @Override
        public void onContactEventPressed(View view, ContactEvent contact) {
            contact.getContact().displayQuickInfo(getActivity(), view);
        }

        @Override
        public void onCardPressed(DayDate date) {
            Intent intent = DateDetailsActivity.getStartIntent(getActivity(), date.getDayOfMonth(), date.getMonth(), date.getYear());
            startActivity(intent);
        }

        @Override
        public void onDifferentMonthScrolled(int month) {
            titleSetter.updateWithMonth(month);
        }
    };

    public boolean onSearchRequested() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
        return true;
    }

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
