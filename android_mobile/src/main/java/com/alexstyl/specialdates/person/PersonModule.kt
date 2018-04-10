package com.alexstyl.specialdates.person

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.support.v7.view.ContextThemeWrapper
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Strings
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
                     strings: Strings): PersonActionsProvider {

        val theme = ThemingPreferences.newInstance(context).selectedTheme
        val wrapper = ContextThemeWrapper(context, theme.androidTheme())

        return PersonActionsProvider(
                AndroidContactActionsProvider(
                        contentResolver, resources, wrapper, packageManager, tracker),
                FacebookContactActionsProvider(strings, resources)
        )
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
                  personActionsProvider: PersonActionsProvider,
                  peoplePersister: PeopleEventsPersister,
                  factory: PersonDetailsViewModelFactory,
                  toEventViewModel: EventViewModelFactory): PersonPresenter {
        return PersonPresenter(
                peopleEventsProvider,
                personActionsProvider,
                factory, toEventViewModel, peoplePersister, Schedulers.io(),
                AndroidSchedulers.mainThread()
        )

    }
}
