package com.alexstyl.specialdates.events.namedays.activity;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.ui.widget.AndroidLetterPainter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class NamedaysInADayModule {

    @Provides
    NamedaysViewModelFactory viewModelFactory(AndroidLetterPainter letterPainter) {
        return new NamedaysViewModelFactory(letterPainter);
    }

    @Provides
    NamedaysInADayPresenter presenter(NamedayCalendar calendar,
                               NamedaysViewModelFactory namedaysViewModelFactory,
                               ContactsProvider contactsProvider,
                               NamedayUserSettings namedayUserSettings) {
        return new NamedaysInADayPresenter(calendar, namedaysViewModelFactory, contactsProvider, namedayUserSettings, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
