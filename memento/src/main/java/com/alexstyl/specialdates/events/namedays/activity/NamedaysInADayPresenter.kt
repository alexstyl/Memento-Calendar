package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.gsc.SoundComparer
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class NamedaysInADayPresenter(private val namedayCalendar: NamedayCalendar,
                              private val namedaysViewModelFactory: NamedaysViewModelFactory,
                              private val contactsProvider: ContactsProvider,
                              private val namedayUserSettings: NamedayUserSettings,
                              private val workScheduler: Scheduler,
                              private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresenting(into: NamedaysOnADayView, forDate: Date) {
        disposable =
                Observable.fromCallable { namedayCalendar.getAllNamedaysOn(forDate) }
                        .observeOn(workScheduler)
                        .map { findAndCreateViewModelsOf(it.names) }
                        .observeOn(resultScheduler)
                        .subscribe { namedaysViewModel ->
                            into.displayNamedays(namedaysViewModel)
                        }
    }

    fun stopPresenting() = disposable?.dispose()

    private fun findAndCreateViewModelsOf(celebratingNames: List<String>): List<NamedayScreenViewModel> {
        val allContacts = contactsProvider.allContacts

        return celebratingNames.fold(listOf()) { list, celebratingName ->
            val contactsCelebrating = allContacts.findContactsCalled(celebratingName)
            list + namedaysViewModelFactory.viewModelsFor(celebratingName) + contactsCelebrating.map {
                namedaysViewModelFactory.viewModelsFor(it)
            }
        }

    }

    private val DisplayName.names: List<String>
        get() {
            return if (namedayUserSettings.shouldLookupAllNames()) {
                this.allNames
            } else {
                this.firstNames
            }
        }


    private fun List<Contact>.findContactsCalled(celebratingName: String): List<Contact> {
        val list = ArrayList<Contact>()
        this.forEach { contact ->
            contact.displayName.names.forEach {
                if (it.soundsLike(celebratingName)) {
                    list.add(contact)
                    return@forEach
                }
            }
        }
        return list
    }

    private fun String.soundsLike(celebratingName: String): Boolean = SoundComparer.soundTheSame(this, celebratingName)
}

