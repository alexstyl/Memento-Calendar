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
    AgeCalculator ageCalculator() {
        return new AgeCalculator(Date.Companion.today());
    }

    @Provides
    PersonDetailsViewModelFactory personDetailsViewModelFactory(Strings strings, AgeCalculator ageCalculator) {
        return new PersonDetailsViewModelFactory(strings, ageCalculator);
    }

    @Provides
    EventViewModelFactory eventViewModelFactory(Strings strings, DateLabelCreator dateLabelCreator) {
        return new EventViewModelFactory(strings, dateLabelCreator);
    }

    @Provides
    PersonPresenter presenter(PeopleEventsProvider peopleEventsProvider,
                              PersonCallProvider personCallProvider,
                              PeopleEventsPersister peoplePersister,
                              PersonDetailsViewModelFactory factory,
                              EventViewModelFactory toEventViewModel) {
        return new PersonPresenter(
                peopleEventsProvider,
                personCallProvider,
                factory, toEventViewModel, peoplePersister, Schedulers.io(),
                AndroidSchedulers.mainThread()
        );

    }
}
