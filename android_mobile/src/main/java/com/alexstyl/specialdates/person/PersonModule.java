package com.alexstyl.specialdates.person;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateLabelCreator;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.people.PeopleViewModelFactory;
import com.alexstyl.specialdates.permissions.MementoPermissions;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class PersonModule {

    @Provides
    PersonCallProvider callProvider(ContentResolver contentResolver,
                                    Resources resources,
                                    Context context,
                                    PackageManager packageManager,
                                    CrashAndErrorTracker tracker,
                                    Strings strings) {
        return new PersonCallProvider(
                new AndroidContactActionsProvider(
                        contentResolver, resources, context, packageManager, tracker
                ),
                new FacebookContactActionsProvider(strings, resources)
        );
    }

    @Provides
    PersonPresenter presenter(MementoPermissions permissions,
                              PeopleEventsProvider peopleEventsProvider,
                              PeopleViewModelFactory viewModelFactory,
                              CrashAndErrorTracker errorTracker, PersonCallProvider personCallProvider, Strings strings, PeopleEventsPersister peoplePersister, DateLabelCreator dateLabelCreator) {
        return new PersonPresenter(
                peopleEventsProvider,
                personCallProvider,
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                new PersonDetailsViewModelFactory(strings, new AgeCalculator(Date.Companion.today())),
                new EventViewModelFactory(strings, dateLabelCreator),
                peoplePersister
        );

    }
}
