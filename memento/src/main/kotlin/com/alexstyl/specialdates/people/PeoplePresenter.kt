package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.contact.ContactsProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class PeoplePresenter(
        private val contactProvider: ContactsProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private val subject = PublishSubject.create<Int>()
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
                            viewModels
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


