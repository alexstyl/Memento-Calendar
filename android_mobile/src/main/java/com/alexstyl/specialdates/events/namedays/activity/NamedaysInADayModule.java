package com.alexstyl.specialdates.events.namedays.activity;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.ui.widget.LetterPainter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class NamedaysInADayModule {

    @Provides
    NamedaysViewModelFactory viewModelFactory(LetterPainter letterPainter) {
        return new NamedaysViewModelFactory(letterPainter);
    }

    @Provides
    NamedayPresenter presenter(NamedayCalendar calendar, NamedaysViewModelFactory namedaysViewModelFactory, ContactsProvider contactsProvider) {
        return new NamedayPresenter(calendar, namedaysViewModelFactory, contactsProvider, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
