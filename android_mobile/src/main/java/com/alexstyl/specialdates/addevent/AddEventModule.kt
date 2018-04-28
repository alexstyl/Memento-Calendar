package com.alexstyl.specialdates.addevent

import android.content.ContentResolver
import android.content.Context
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.images.ImageDecoder
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class AddEventModule {

    @Provides
    fun presenter(analytics: Analytics,
                  contactOperations: ContactOperations,
                  messageDisplayer: MessageDisplayer,
                  operationsExecutorAndroid: AndroidContactOperationsExecutor,
                  strings: Strings,
                  peopleEventsProvider: PeopleEventsProvider,
                  factory: AddEventViewModelFactory) = AddEventsPresenter(
            analytics,
            contactOperations,
            messageDisplayer,
            operationsExecutorAndroid,
            strings,
            peopleEventsProvider,
            factory,
            Schedulers.io(),
            AndroidSchedulers.mainThread()
    )

    @Provides
    fun factory(dateLabelCreator: DateLabelCreator, strings: Strings) = AddEventViewModelFactory(
            dateLabelCreator, strings, AndroidEventIcons)

    @Provides
    fun messageDisplayer(context: Context): MessageDisplayer = ToastDisplayer(context)


    @Provides
    fun accountsProvider(context: Context) = WriteableAccountsProvider.from(context)


    @Provides
    fun operations() = ContactOperations()

    @Provides
    fun operationsExectutor(contentResolver: ContentResolver, tracker: CrashAndErrorTracker) = AndroidContactOperationsExecutor(contentResolver, tracker)


    @Provides
    fun filePathProvider(context: Context) = FilePathProvider(context)

    @Provides
    fun imageDecoder() = ImageDecoder()
}
