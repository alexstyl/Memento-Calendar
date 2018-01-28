package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.contact.ContactsProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class PeoplePresenter(
        private val contactProvider: ContactsProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: PeopleView) {
        disposable =
                Observable.fromCallable {
                    contactProvider.allContacts
                }
                        .observeOn(workScheduler)
                        .map { contacts ->
                            val viewModels = arrayListOf<PeopleViewModel>()
                            contacts.forEach { contact ->
                                viewModels.add(PeopleViewModel(contact,
                                        contact.displayName.toString(),
                                        contact.imagePath,
                                        contact.contactID,
                                        contact.source))
                            }
                            viewModels.sortedWith(compareBy({ it.personName }))
                        }
                        .observeOn(resultScheduler)
                        .subscribe { viewModels ->
                            view.displayPeople(viewModels)
                        }


    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}


