package com.alexstyl.specialdates.person

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.support.v7.view.ContextThemeWrapper
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.theming.ThemingPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class PersonModule {

    @Provides
    fun callProvider(contentResolver: ContentResolver,
                     resources: Resources,
                     context: Context,
                     packageManager: PackageManager,
                     tracker: CrashAndErrorTracker,
                     strings: Strings,
                     themePreferences: ThemingPreferences): ContactActionsProvider {

        val wrapper = ContextThemeWrapper(context, themePreferences.selectedTheme.androidTheme())

        return CompositeContactActionsProvider(
                mapOf(
                        Pair(SOURCE_DEVICE, AndroidContactActionsProvider(contentResolver, resources, wrapper, packageManager, tracker)),
                        Pair(SOURCE_FACEBOOK, FacebookContactActionsProvider(strings, resources))
                ))
    }

    @Provides
    fun ageCalculator(): AgeCalculator {
        return AgeCalculator(Date.today())
    }

    @Provides
    fun personDetailsViewModelFactory(strings: Strings, ageCalculator: AgeCalculator): PersonDetailsViewModelFactory {
        return PersonDetailsViewModelFactory(strings, ageCalculator)
    }

    @Provides
    fun eventViewModelFactory(strings: Strings, dateLabelCreator: DateLabelCreator): EventViewModelFactory {
        return EventViewModelFactory(strings, dateLabelCreator)
    }

    @Provides
    fun presenter(peopleEventsProvider: PeopleEventsProvider,
                  compositeContactActionsProvider: ContactActionsProvider,
                  peoplePersister: PeopleEventsPersister,
                  factory: PersonDetailsViewModelFactory,
                  toEventViewModel: EventViewModelFactory): PersonPresenter {
        return PersonPresenter(
                peopleEventsProvider,
                compositeContactActionsProvider,
                factory, toEventViewModel, peoplePersister, Schedulers.io(),
                AndroidSchedulers.mainThread()
        )

    }
}
