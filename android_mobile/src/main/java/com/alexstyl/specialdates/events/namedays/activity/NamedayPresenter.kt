package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.gsc.Sound
import com.alexstyl.gsc.SoundRules
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.util.HashMapList
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class NamedayPresenter(private val namedayCalendar: NamedayCalendar,
                       private val namedaysViewModelFactory: NamedaysViewModelFactory,
                       private val contactsProvider: ContactsProvider,
                       private val workScheduler: Scheduler,
                       private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresenting(into: NamedaysMVPView, forDate: Date) {
        disposable = Observable.fromCallable { namedayCalendar.getAllNamedayOn(forDate) }
                .observeOn(workScheduler) // TODO does this mean that the fromCallable will be executed on the workScheduler?
                .map { it.names.asViewModels() }
                .observeOn(resultScheduler)
                .subscribe {
                    namedaysViewModel ->
                    into.displayNamedays(namedaysViewModel)
                }
    }

    fun stopPresenting() {
        disposable?.dispose()
    }

    private fun List<String>.asViewModels(): ArrayList<NamedayScreenViewModel> {
        val allContacts = contactsProvider.allContacts

        val hashMap = HashMapList<PhoneticName, Contact>()
        for (contact in allContacts) {
            contact.displayName.firstNames.forEach {
                hashMap.addValue(it.toSounds(), contact)
            }
        }

        val list = ArrayList<NamedayScreenViewModel>()
        this.forEach {
            val viewModel = namedaysViewModelFactory.viewModelsFor(it)
            list.add(viewModel)

            hashMap.get(it.toSounds())?.forEach {
                contact ->
                list.add(namedaysViewModelFactory.viewModelsFor(contact))
            }
        }
        return list
    }
}

private fun String.toSounds(): PhoneticName {
    val raw = this
    return PhoneticName(ArrayList<Sound>()
            .apply {
                SoundRules.INSTANCE.getNextSound(raw, true).forEach {
                    add(it!!)
                }
            })
}

data class PhoneticName(val it: List<Sound>)
