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
    private final AddEventContactEventViewModelFactory factory;
    private final AddEventViewModelFactory addEventViewModelFactory;

    private Optional<Contact> contact;

    private OnDataFetchedCallback callback;

    ContactEventsFetcher(LoaderManager loaderManager,
                         Context context,
                         PeopleEventsProvider peopleEventsProvider,
                         AddEventContactEventViewModelFactory factory,
                         AddEventViewModelFactory addEventViewModelFactory) {
        this.loaderManager = loaderManager;
        this.context = context;
        this.peopleEventsProvider = peopleEventsProvider;
        this.factory = factory;
        this.addEventViewModelFactory = addEventViewModelFactory;
    }

    void load(Contact contact, OnDataFetchedCallback callback) {
        loadEventsFor(new Optional<>(contact), callback);
    }

    void loadEmptyEvents(OnDataFetchedCallback callback) {
        loadEventsFor(Optional.<Contact>absent(), callback);
    }

    private void loadEventsFor(Optional<Contact> contactOptional, OnDataFetchedCallback callback) {
        this.contact = contactOptional;
        this.callback = callback;
        loaderManager.restartLoader(24, null, callbacks);
    }

    private LoaderManager.LoaderCallbacks<List<AddEventContactEventViewModel>> callbacks = new LoaderManager.LoaderCallbacks<List<AddEventContactEventViewModel>>() {
        @Override
        public Loader<List<AddEventContactEventViewModel>> onCreateLoader(int id, Bundle args) {
            return new ContactEventsLoader(context, contact, peopleEventsProvider, factory, addEventViewModelFactory);
        }

        @Override
        public void onLoadFinished(Loader<List<AddEventContactEventViewModel>> loader, List<AddEventContactEventViewModel> data) {
            callback.onDataFetched(data);
        }

        @Override
        public void onLoaderReset(Loader<List<AddEventContactEventViewModel>> loader) {

        }
    };

    interface OnDataFetchedCallback {
        void onDataFetched(List<AddEventContactEventViewModel> data);
    }

}
