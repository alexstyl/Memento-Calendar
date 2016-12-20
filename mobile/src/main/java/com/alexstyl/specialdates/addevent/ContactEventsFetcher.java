package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.service.PeopleEventsProvider;

import java.util.List;

final class ContactEventsFetcher {

    private final LoaderManager loaderManager;
    private final Context context;
    private final PeopleEventsProvider peopleEventsProvider;
    private final ContactEventViewModelFactory factory;
    private final AddEventViewModelFactory newEventFactory;

    private Optional<Contact> contact;

    private OnDataFetchedCallback callback;

    ContactEventsFetcher(LoaderManager loaderManager,
                         Context context,
                         PeopleEventsProvider peopleEventsProvider,
                         ContactEventViewModelFactory factory,
                         AddEventViewModelFactory newEventFactory) {
        this.loaderManager = loaderManager;
        this.context = context;
        this.peopleEventsProvider = peopleEventsProvider;
        this.factory = factory;
        this.newEventFactory = newEventFactory;
    }

    void load(Contact contact, OnDataFetchedCallback callback) {
        this.contact = new Optional<>(contact);
        this.callback = callback;
        loaderManager.restartLoader(24, null, callbacks);
    }

    void loadEmptyEvents(OnDataFetchedCallback callback) {
        this.contact = Optional.absent();
        this.callback = callback;
        loaderManager.restartLoader(24, null, callbacks);
    }

    private LoaderManager.LoaderCallbacks<List<ContactEventViewModel>> callbacks = new LoaderManager.LoaderCallbacks<List<ContactEventViewModel>>() {
        @Override
        public Loader<List<ContactEventViewModel>> onCreateLoader(int id, Bundle args) {
            return new ContactEventsLoader(context, contact, peopleEventsProvider, factory, newEventFactory);
        }

        @Override
        public void onLoadFinished(Loader<List<ContactEventViewModel>> loader, List<ContactEventViewModel> data) {
            callback.onDataFetched(data);
        }

        @Override
        public void onLoaderReset(Loader<List<ContactEventViewModel>> loader) {

        }
    };

    interface OnDataFetchedCallback {
        void onDataFetched(List<ContactEventViewModel> data);
    }

}
