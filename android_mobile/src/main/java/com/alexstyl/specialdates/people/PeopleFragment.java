package com.alexstyl.specialdates.people;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.UpcomingEventsView;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;
import com.alexstyl.specialdates.home.HomeActivity;
import com.alexstyl.specialdates.home.HomeNavigator;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.base.MementoFragment;

import javax.inject.Inject;
import java.util.List;

public class PeopleFragment extends MementoFragment implements PeopleView, UpcomingEventsView {

    @Inject ImageLoader imageLoader;
    @Inject HomeNavigator navigator;
    @Inject PeoplePresenter presenter;
    @Inject UpcomingEventsViewRefresher refresher;

    private PeopleAdapter adapter;
    private ProgressBar loadingView;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MementoApplication) getActivity().getApplication()).getApplicationModule().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_people, container, false);

        loadingView = inflate.findViewById(R.id.people_loading);
        recyclerView = inflate.findViewById(R.id.people_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new PeopleAdapter(imageLoader, inflater, new PeopleViewHolderListener() {
            @Override
            public void onPersonClicked(Contact contact) {
                navigator.toContactDetails(contact, getActivity());
            }

            @Override
            public void onFacebookImport() {
                navigator.toFacebookImport(getActivity());
            }

            @Override
            public void onAddContactClicked() {
                navigator.toAddEvent(getActivity(), HomeActivity.CODE_ADD_EVENT);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new PeopleItemDecorator(
                        getResources().getDimensionPixelSize(R.dimen.people_import_bottom_spacing),
                        getResources().getDimensionPixelSize(R.dimen.people_inbetween_spacing)
                ));
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.startPresentingInto(this);
        refresher.addEventsView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopPresenting();
        refresher.removeView(this);
    }

    @Override
    public void displayPeople(List<PeopleRowViewModel> viewModels) {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.updateWith(viewModels);
    }

    @Override
    public void showLoading() {
        if (isDisplayNoData()) {
            recyclerView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isDisplayNoData() {
        return recyclerView.getChildCount() == 0;
    }

    @Override
    public void reloadUpcomingEventsView() {
        presenter.refreshData();
    }
}
