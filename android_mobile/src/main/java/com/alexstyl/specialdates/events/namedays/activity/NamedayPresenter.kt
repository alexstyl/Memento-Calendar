package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.NameComparator
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

        val hashMap = HashMapList<CheatName, Contact>()
        for (contact in allContacts) {
            contact.displayName.firstNames.forEach {
                hashMap.addValue(CheatName(it), contact)
            }
        }

        val list = ArrayList<NamedayScreenViewModel>()
        this.forEach {
            val viewModel = namedaysViewModelFactory.viewModelsFor(it)
            list.add(viewModel)

            hashMap.get(CheatName(it))?.forEach {
                contact ->
                list.add(namedaysViewModelFactory.viewModelsFor(contact))
            }
        }
        return list
    }
}

class CheatName(val it: String) {
    override fun hashCode(): Int {
        return it.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as CheatName

        return NameComparator.areTheSameName(it, other.it)
    }
}
