package com.alexstyl.specialdates.addevent

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import com.alexstyl.specialdates.images.ImageDecoder
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class AddEventModule {

    @Provides
    fun presenter(analytics: Analytics,
                  eventsPresenter: EventsPresenter,
                  contactOperations: ContactOperations,
                  messageDisplayer: MessageDisplayer,
                  operationsExecutor: ContactOperationsExecutor,
                  resources: Resources,
                  imageDecoder: ImageDecoder) = AddContactEventsPresenter(
            analytics,
            eventsPresenter,
            contactOperations,
            messageDisplayer,
            operationsExecutor,
            resources,
            imageDecoder
    )

    @Provides
    fun factory(dateLabelCreator: DateLabelCreator) = AddEventContactEventViewModelFactory(dateLabelCreator)

    @Provides
    fun messageDisplayer(context: Context): MessageDisplayer = ToastDisplayer(context)


    @Provides
    fun viewModelFactory(strings: Strings) = AddEventViewModelFactory(strings)

    @Provides
    fun accountsProvider(context: Context) = WriteableAccountsProvider.from(context)


    @Provides
    fun operations(contentResolver: ContentResolver,
                   accountsProvider: WriteableAccountsProvider,
                   peopleEventsProvider: PeopleEventsProvider,
                   shortLabeblCreator: ShortDateLabelCreator) = ContactOperations(
            contentResolver,
            accountsProvider,
            peopleEventsProvider,
            shortLabeblCreator
    )

    @Provides
    fun operationsExectutor(contentResolver: ContentResolver, tracker: CrashAndErrorTracker) = ContactOperationsExecutor(contentResolver, tracker)

    @Provides
    fun eventsPresenter(peopleEventsProvider: PeopleEventsProvider,
                        factory: AddEventContactEventViewModelFactory,
                        addEventFactory: AddEventViewModelFactory) =
            EventsPresenter(peopleEventsProvider, factory, addEventFactory, Schedulers.io(), AndroidSchedulers.mainThread())

    @Provides
    fun filePathProvider(context: Context) = FilePathProvider(context)

    @Provides
    fun imageDecoder() = ImageDecoder()
}
