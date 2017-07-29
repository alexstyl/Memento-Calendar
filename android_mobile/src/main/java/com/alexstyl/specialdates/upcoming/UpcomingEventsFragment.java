package com.alexstyl.specialdates.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest;
import com.alexstyl.specialdates.permissions.ContactPermissionRequest.PermissionCallbacks;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.permissions.PermissionNavigator;
import com.alexstyl.specialdates.settings.EventsSettingsMonitor;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.ui.widget.SpacesItemDecoration;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;
import java.util.List;

public class UpcomingEventsFragment extends MementoFragment implements UpcomingListMVPView {

    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView emptyView;

    private RecyclerView upcomingList;

    private UpcomingEventsPresenter presenter;
    private UpcomingEventsAdapter adapter;
    @Inject Analytics analytics;
    @Inject StringResources stringResources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppComponent applicationModule = ((MementoApplication) getActivity().getApplication()).getApplicationModule();
        applicationModule.inject(this);
        UpcomingEventsAsyncProvider upcomingEventsAsyncProvider = new UpcomingEventsAsyncProvider(new UpcomingEventsFetcher(getLoaderManager(), getActivity(), Date.today(), stringResources));
        ContactPermissionRequest permissions = new ContactPermissionRequest(new PermissionNavigator(getActivity(), analytics), new PermissionChecker(getActivity()), permissionCallbacks);
        EventsSettingsMonitor monitor = new EventsSettingsMonitor(PreferenceManager.getDefaultSharedPreferences(getActivity()), stringResources);
        MainNavigator navigator = new MainNavigator(analytics, getActivity(), stringResources, FacebookPreferences.newInstance(getActivity()));
        presenter = new UpcomingEventsPresenter(
                this,
                analytics,
                upcomingEventsAsyncProvider,
                permissions,
                monitor,
                new PeopleEventsObserver(getContentResolver()),
                navigator,
                new ExternalNavigator(getActivity(), analytics)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        root = Views.findById(view, R.id.root);
        progressBar = Views.findById(view, R.id.upcoming_events_progress);
        emptyView = Views.findById(view, R.id.upcoming_events_emptyview);

        upcomingList = Views.findById(view, R.id.upcoming_events_list);
        upcomingList.setHasFixedSize(true);
        upcomingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        upcomingList.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.upcoming_vertical_padding_between_cards), 1));

        adapter = new UpcomingEventsAdapter(
                new UpcomingViewHolderFactory(inflater, UILImageLoader.createCircleLoader(getResources())),
                presenter
        );
        upcomingList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.startPresenting();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.requestForPermissionIfNeeded();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        upcomingList.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void display(List<UpcomingRowViewModel> events) {
        TransitionManager.beginDelayedTransition(root);

        progressBar.setVisibility(View.GONE);
        adapter.displayUpcomingEvents(events);

        if (events.size() > 0) {
            upcomingList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            upcomingList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showFirstEvent() {
        upcomingList.scrollToPosition(0);
    }

    @Override
    public boolean isDisplayingNoData() {
        return upcomingList.getChildCount() == 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }

    private final PermissionCallbacks permissionCallbacks = new PermissionCallbacks() {
        @Override
        public void onPermissionGranted() {
            presenter.refreshData();
        }

        @Override
        public void onPermissionDenied() {
            getActivity().finishAffinity();
        }
    };
}
