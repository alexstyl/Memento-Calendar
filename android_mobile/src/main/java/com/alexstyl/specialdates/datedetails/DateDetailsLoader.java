package com.alexstyl.specialdates.datedetails;

import android.content.Context;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.util.List;

class DateDetailsLoader extends SimpleAsyncTaskLoader<List<DateDetailsViewModel>> {

    private final Date date;
    private final ContactsObserver contactsObserver;
    private final ContactEventViewModelFactory contactEventViewModelFactory;

    private final PeopleEventsProvider peopleEventsProvider;

    DateDetailsLoader(Context context,
                      Date date,
                      PeopleEventsProvider peopleEventsProvider,
                      ContactsObserver contactsObserver,
                      ContactEventViewModelFactory contactEventViewModelFactory) {
        super(context);
        this.date = date;
        this.contactsObserver = contactsObserver;
        this.peopleEventsProvider = peopleEventsProvider;
        this.contactEventViewModelFactory = contactEventViewModelFactory;

        contactsObserver.registerWith(
                new ContactsObserver.Callback() {
                    @Override
                    public void onContactsUpdated() {
                        onContentChanged();
                    }
                }
        );
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        contactsObserver.unregister();
    }

    @Override
    public List<DateDetailsViewModel> loadInBackground() {
        // TODO append namedays, bankholidays and support
        List<ContactEvent> celebrationDates = peopleEventsProvider.getCelebrationDateOn(date);

        return contactEventViewModelFactory.convertToViewModels(celebrationDates);
    }
}
