package com.alexstyl.specialdates.upcoming;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.UpcomingEventsView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;
import com.alexstyl.specialdates.home.HomeNavigator;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.novoda.notils.caster.Views;

import javax.inject.Inject;
import java.util.List;

public class UpcomingEventsFragment extends MementoFragment implements UpcomingListMVPView, UpcomingEventsView {

    private ViewGroup root;
    private ProgressBar progressBar;
    private TextView emptyView;
    private RecyclerView upcomingList;

    private UpcomingEventsAdapter adapter;
    private AskForSupport askForSupport;

    @Inject HomeNavigator navigator;
    @Inject ImageLoader imageLoader;
    @Inject UpcomingEventsViewRefresher refresher;
    @Inject UpcomingEventsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppComponent applicationModule = ((MementoApplication) getActivity().getApplication()).getApplicationModule();
        applicationModule.inject(this);

        askForSupport = new AskForSupport(getActivity());
        refresher.addEventsView(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        root = Views.findById(view, R.id.root);
        progressBar = Views.findById(view, R.id.upcoming_events_progress);
        emptyView = Views.findById(view, R.id.upcoming_events_emptyview);

        upcomingList = Views.findById(view, R.id.upcoming_events_list);
        upcomingList.setHasFixedSize(true);
        upcomingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        upcomingList.addItemDecoration(
                new UpcomingEventsDecorator(
                        getResources().getDimensionPixelSize(R.dimen.upcoming_event_header_vertical_spacing),
                        getResources().getDimensionPixelSize(R.dimen.upcoming_event_vertical_spacing)
                ));

        adapter = new UpcomingEventsAdapter(
                new UpcomingViewHolderFactory(inflater, imageLoader),
                new OnUpcomingEventClickedListener() {

                    @Override
                    public void onContactClicked(Contact contact) {
                        navigator.toContactDetails(contact, getActivity());
                    }

                    @Override
                    public void onNamedayClicked(Date date) {
                        navigator.toDateDetails(date, getActivity());
                    }
                }
        );
        adapter.setHasStableIds(true);
        upcomingList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresher.addEventsView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.startPresentingInto(this);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        upcomingList.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
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

        if (askForSupport.shouldAskForRating()) {
            askForSupport.askForRatingFromUser(getActivity());
        }
    }

    @Override
    public boolean isShowingNoEvents() {
        return upcomingList.getChildCount() == 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        refresher.removeView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refresher.removeView(this);
    }

    @Override
    public void reloadUpcomingEventsView() {
        presenter.refreshEvents();
    }
}
