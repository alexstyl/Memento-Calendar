package com.alexstyl.specialdates.dailyreminder.actions

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.person.ContactActions
import com.alexstyl.specialdates.person.ContactActionsProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class ContactActionsPresenter(
        private val personActionsProvider: ContactActionsProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingCallsInto(view: ContactActionsView, contactActions: ContactActions) {
        disposable =
                callActionsFor(contactActions, view.contact())
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe { viewModels ->
                            view.display(viewModels)
                        }

    }

    private fun callActionsFor(contactActions: ContactActions, contact: Contact) =
            Observable.fromCallable { personActionsProvider.callActionsFor(contact, contactActions) }

    fun startPresentingMessagingInto(view: ContactActionsView, contactActions: ContactActions) {
        disposable =
                messagingActionsFor(contactActions, view.contact())
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe { viewModels ->
                            view.display(viewModels)
                        }

    }

    private fun messagingActionsFor(contactActions: ContactActions, contact: Contact) =
            Observable.fromCallable { personActionsProvider.messagingActionsFor(contact, contactActions) }

    fun stopPresenting() {
        disposable?.dispose()
    }

}
