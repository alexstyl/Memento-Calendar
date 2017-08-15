package com.alexstyl.specialdates.events.namedays.activity;

import android.content.res.Resources;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class NamedayInADayFeature {

    private final Resources resources;

    public NamedayInADayFeature(Resources resources) {
        this.resources = resources;
    }

    @Provides
    NamedaysViewModelFactory viewModelFactory() {
        return new NamedaysViewModelFactory(resources);
    }

    @Provides
    NamedayPresenter presenter(NamedayCalendar calendar, NamedaysViewModelFactory namedaysViewModelFactory, ContactsProvider contactsProvider) {
        return new NamedayPresenter(calendar, namedaysViewModelFactory, contactsProvider, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
