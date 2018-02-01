package com.alexstyl.specialdates.people;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.base.MementoFragment;
import com.alexstyl.specialdates.home.HomeNavigator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PeopleFragment extends MementoFragment implements PeopleView {

    @Inject
    ImageLoader imageLoader;
    @Inject
    ContactsProvider contactProvider;

    PeoplePresenter peoplePresenter;

    private PeopleAdapter adapter;
    @Inject
    HomeNavigator navigator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MementoApplication) getActivity().getApplication()).getApplicationModule().inject(this);

        peoplePresenter = new PeoplePresenter(
                contactProvider,
                Schedulers.io(),
                AndroidSchedulers.mainThread());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_people, container, false);

        RecyclerView recyclerView = inflate.findViewById(R.id.people_list);
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
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new PeopleItemDecorator(
                        getResources().getDimensionPixelSize(R.dimen.people_import_bottom_spacing),
                        getResources().getDimensionPixelSize(R.dimen.people_inbetween_spacing)));
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        peoplePresenter.startPresentingInto(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        peoplePresenter.stopPresenting();
    }

    @Override
    public void displayPeople(@NotNull List<PeopleViewModel> viewModels) {
        adapter.updateWith(viewModels);
    }

}
