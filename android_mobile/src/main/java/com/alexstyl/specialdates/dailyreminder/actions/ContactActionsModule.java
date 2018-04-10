package com.alexstyl.specialdates.dailyreminder.actions;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.contact.ContactIntentExtractor;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.person.ContactActionsProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class ContactActionsModule {

    @Provides
    ContactActionsPresenter presenter(ContactActionsProvider personActionsProvider) {
        return new ContactActionsPresenter(personActionsProvider, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Provides
    ContactIntentExtractor extractor(CrashAndErrorTracker tracker, ContactsProvider contactProvider) {
        return new ContactIntentExtractor(tracker, contactProvider);
    }
}
